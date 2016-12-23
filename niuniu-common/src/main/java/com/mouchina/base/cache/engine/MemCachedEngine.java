package com.mouchina.base.cache.engine;

import com.mouchina.base.utils.StringUtil;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

/**
 * 缓存引擎MemCached实现
 *
 * @author
 *
 */
public class MemCachedEngine
    implements CacheEngine
{
    private static Logger logger = LogManager.getLogger( "MemCachedEngine" );

    /**
     * 正则
     */
    private final static Pattern pattern =
        Pattern.compile( "^(25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|\\d):(\\d{4,5})$" );

    /**
     * MemCachedClient
     */
    private MemcachedClient client = null;

    /**
     * MemCached服务器端的地址列表，以,分隔' '默认连接到当前系统。如 192.168.0.2:11211 192.168.0.3:11211
     */
    private String serverList = new String( "192.168.101.101:12000" );

    /**
     * MemCached服务器数组
     */
    private String[] servers = null;

    /**
     * 连接池大小
     */
    private int poolSize = 10;

    /**
     * 默认生存时间(单位秒)
     */
    private int alive = 24 * 60 * 60 * 7;

    /**
     * Inits the cache engine.
     */
    @Override
    public synchronized void init(  )
    {
        if ( StringUtil.isEmpty( serverList ) )
        {
            logger.info( "No Memcached Server IP!" );
        }

        servers = serverList.split( "," );

        for ( final String server : servers )
        {
            if ( ! pattern.matcher( server ).find(  ) )
            {
                logger.info( "Memcached Server IP error!" );
            }
        }

        MemcachedClientBuilder builder = new XMemcachedClientBuilder( AddrUtil.getAddresses( serverList ) );
        // 设置连接池大小，即客户端个数
        builder.setConnectionPoolSize( poolSize );
        // 宕机报警
        builder.setFailureMode( true );

        // 使用二进制文件
        // builder.setCommandFactory(new BinaryCommandFactory());
        try
        {
            client = builder.build(  );
        } catch ( IOException e )
        {
            logger.info( "Memcached start error!" );
            e.printStackTrace(  );
        }
    }

    /**
     * Stops the cache engine
     */
    @Override
    public void stop(  )
    {
        try
        {
            client.shutdown(  );
        } catch ( IOException e )
        {
            logger.info( "Memcached stop error!" );
            e.printStackTrace(  );
        }
    }

    /**
     * Adds a new object to the cache.
     *
     * @param key
     *            The key to associate with the object.
     * @param value
     *            The object to cache
     * @throws net.rubyeye.xmemcached.exception.MemcachedException
     * @throws InterruptedException
     * @throws java.util.concurrent.TimeoutException
     */
    @Override
    public void add( String key, Object value )
             throws TimeoutException, InterruptedException, MemcachedException
    {
        if ( client.get( key ) != null )
        {
            client.replace( key, alive, value );
        } else
        {
            client.add( key, alive, value );
        }
    }

    /**
     * Adds a new object to the cache.
     *
     * @param key
     *            The key to associate with the object.
     * @param value
     *            The object to cache
     * @param liveSec
     *            生存时间（秒）。最大为30 * 24 * 60 * 60
     * @throws net.rubyeye.xmemcached.exception.MemcachedException
     * @throws InterruptedException
     * @throws java.util.concurrent.TimeoutException
     *
     */
    @Override
    public void add( String key, Object value, int liveSec )
             throws TimeoutException, InterruptedException, MemcachedException
    {
        if ( client.get( key ) != null )
        {
            client.replace( key, liveSec, value );
        } else
        {
            client.add( key, liveSec, value );
        }
    }

    /**
     * Gets some object from the cache.
     *
     * @param key
     *            The key to get
     * @return The cached object, or <code>null</code> if no entry was found
     * @throws net.rubyeye.xmemcached.exception.MemcachedException
     * @throws InterruptedException
     * @throws java.util.concurrent.TimeoutException
     */
    @Override
    public Object get( String key )
               throws TimeoutException, InterruptedException, MemcachedException
    {
        return client.get( key );
    }

    /**
     * Removes an entry from the cache.
     *
     * @param key
     *            The key to remove
     * @throws net.rubyeye.xmemcached.exception.MemcachedException
     * @throws InterruptedException
     * @throws java.util.concurrent.TimeoutException
     */
    @Override
    public void remove( String key )
                throws TimeoutException, InterruptedException, MemcachedException
    {
        client.delete( key );
    }

    /**
     * Removes all entry from the cache
     *
     * @throws net.rubyeye.xmemcached.exception.MemcachedException
     * @throws InterruptedException
     * @throws java.util.concurrent.TimeoutException
     *
     */
    @Override
    public void removeAll(  )
                   throws TimeoutException, InterruptedException, MemcachedException
    {
        client.flushAll(  );
    }

    /**
     * 得到服务器端的地址列表
     *
     * @return the serverList
     */
    public String getServerList(  )
    {
        return serverList;
    }

    /**
     * 设置服务器端的地址列表
     *
     * @param serverList
     *            the serverList to set
     */
    public void setServerList( final String serverList )
    {
        this.serverList = serverList;
    }

    /**
     * 得到连接池大小
     *
     * @return
     */
    public int getPoolSize(  )
    {
        return poolSize;
    }

    /**
     * 设置连接池大小
     *
     * @param poolSize
     */
    public void setPoolSize( int poolSize )
    {
        this.poolSize = poolSize;
    }

    /**
     * 得到生存时间
     *
     * @return 生存时间
     */
    public int getAlive(  )
    {
        return alive;
    }

    /**
     * 设置生存时间
     *
     * @param alive
     *            生存时间
     */
    public void setAlive( int alive )
    {
        this.alive = alive;
    }

    public static void main( final String[] args )
                     throws TimeoutException, InterruptedException, MemcachedException
    {
        final String a = "42.96.151.103:15001";

        if ( StringUtil.isEmpty( a ) )
        {
            System.out.println( "error" );
        }

        final String[] servers = a.split( "[ ]" );
        final Pattern pattern =
            Pattern.compile( "^(25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|\\d):(\\d{4,5})$" );

        for ( final String server : servers )
        {
            if ( ! pattern.matcher( server ).find(  ) )
            {
                System.out.println( "error" );
            }

            System.out.println( server );
        }

        MemcachedClientBuilder builder = new XMemcachedClientBuilder( AddrUtil.getAddresses( a ) );
        // 设置连接池大小，即客户端个数
        builder.setConnectionPoolSize( 50 );
        // 宕机报警
        builder.setFailureMode( true );

        // 使用二进制文件
        // builder.setCommandFactory(new BinaryCommandFactory());
        MemcachedClient mem = null;

        try
        {
            mem = builder.build(  );
        } catch ( IOException e )
        {
            e.printStackTrace(  );
        }

        mem.add( "java.util.ArrayList$selectListByBusiness_B1334417865",
                 1,
                 new Date(  ) );
        // mem.add("key2", 1, "key2 value");
        // System.out.println(mem.get("key1"));
        // System.out.println(mem.get("key2"));
        // mem.delete("key1");
        // mem.delete("key2");
        //
        System.out.println( mem.get( "java.util.ArrayList$selectListByBusiness_B1334417865" ) );

        // System.out.println(mem.get("key2"));
        MemCachedEngine engine = new MemCachedEngine(  );
        engine.init(  );
        engine.add( "java.util.ArrayList$selectListByBusiness_B1334417865",
                    new Date(  ) );
        System.out.println( "$$$$$$$" + engine.get( "java.util.ArrayList$selectListByBusiness_B1334417865" ) );
    }
}
