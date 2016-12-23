package com.mouchina.base.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.List;

import javax.annotation.Resource;

@Component
public class RedisHelper
{
	private static Logger logger = LogManager.getLogger("RedisHelper");
    @Resource
    private RedisServer redisServer;

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public String get( String key )
    {
        String value = null;

        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            value = jedis.get( key );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
        	logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }

        return value;
    }

    public void set( String key, String value )
    {
        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            value = jedis.set( key, value );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
        	logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }
    }

    public void remove( String key )
    {
        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            jedis.del( key );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
        	logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }
    }

    public void lPush( String key, String value )
    {
        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            jedis.lpush( key, value );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
            logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }
    }

    public void lPushs( String key, String[] value )
    {
        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            jedis.lpush( key, value );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
            logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }
    }

    public void rPop( String key )
    {
        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            jedis.rpop( key );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
            logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }
    }

    public Long llen( String key )
    {
        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;
        Long len = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            len = jedis.llen( key );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
        	logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }

        return len;
    }

    public List lrange( String key, int start, int top )
    {
        List list = null;
        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            list = jedis.lrange( key, start, top );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
            logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }

        return list;
    }

    public void lrem( String key, int count, String value )
    {
        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            jedis.lrem( key, count, value );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
            logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }
    }

    public static void main( String[] args )
    {
        RedisHelper helper = new RedisHelper(  );
        helper.set( "test", "aa" );

        String test = helper.get( "test" );

        System.out.println( "test=" + test );

        helper.lPush( "list", "111" );
        helper.lPush( "list", "222" );
        helper.lPush( "list", "333" );

        List list = helper.lrange( "list", 0, 10 );

        for ( Object object : list )
        {
            System.out.println( "list  obj=" + object );
        }

        System.out.println( "list  llen =" + helper.llen( "list" ) );
        helper.rPop( "list" );
        helper.lPush( "list", "444" );
        helper.lPush( "list", "555" );
        helper.lPush( "list", "6666" );
        System.out.println( "list  llen =" + helper.llen( "list" ) );
        list = helper.lrange( "list", 0, 10 );

        for ( Object object : list )
        {
            System.out.println( "list  obj=" + object );
        }
    }

    public Long getIncr( String key, long integer )
    {
        Long value = null;

        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            value = jedis.incrBy( key, integer );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
            logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }

        return value;
    }

    public Long getIncr( String key )
    {
        Long value = null;

        ShardedJedisPool pool = null;
        ShardedJedis jedis = null;

        try
        {
            pool = redisServer.getPool(  );
            jedis = pool.getResource(  );
            value = jedis.incr( key );
        } catch ( Exception e )
        {
            // 释放redis对象
//            pool.returnBrokenResource( jedis );
            logger.error("获取连接池中的jedis连接出错!");
            e.printStackTrace(  );
        } finally
        {
            // 返还到连接池
            redisServer.returnResource( pool, jedis );
        }

        return value;
    }
}
