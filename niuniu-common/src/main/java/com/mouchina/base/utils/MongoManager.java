package com.mouchina.base.utils;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

public class MongoManager
{
    private static String database;
    private static String username;
    private static String password;
    private static int poolsize;
    private static String host;
    private static int port;
    private static int blocksize;
    private static Mongo mongo = null;

    public MongoManager(  )
    {
    }

    /**
     * 根据名称获取DB，相当于是连接
     *
     * @param dbName
     * @return
     */
    public static DB getDB( String dbName )
    {
        if ( mongo == null )
        {
            // 初始化
            init(  );
        }

        DB db = mongo.getDB( dbName );

        if ( ! db.isAuthenticated(  ) )
        {
            db.authenticate( username,
                             password.toCharArray(  ) );
        }

        return db;
    }

    public static DB getDB(  )
    {
        return getDB( database );
    }

    /**
     * 初始化连接池，设置参数。
     */
    private static void init(  )
    {
        /*
                connectionsPerHost：每个主机的连接数
                threadsAllowedToBlockForConnectionMultiplier：线程队列数，它以上面connectionsPerHost值相乘的结果就是线程队列最大值。如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
                maxWaitTime:最大等待连接的线程阻塞时间
                connectTimeout：连接超时的毫秒。0是默认和无限
                socketTimeout：socket超时。0是默认和无限
                autoConnectRetry：这个控制是否在一个连接时，系统会自动重试
        */

        // 其他参数根据实际情况进行添加
        try
        {
            mongo = new Mongo( host, port );

            MongoOptions opt = mongo.getMongoOptions(  );
            opt.connectionsPerHost = poolsize;
            opt.threadsAllowedToBlockForConnectionMultiplier = blocksize;
        } catch ( Exception e )
        {
            e.printStackTrace(  );
        }
    }

    public static String getDatabase(  )
    {
        return database;
    }

    public static void setDatabase( String database )
    {
        MongoManager.database = database;
    }

    public static String getUsername(  )
    {
        return username;
    }

    public static void setUsername( String username )
    {
        MongoManager.username = username;
    }

    public static String getPassword(  )
    {
        return password;
    }

    public static void setPassword( String password )
    {
        MongoManager.password = password;
    }

    public static int getPoolsize(  )
    {
        return poolsize;
    }

    public static void setPoolsize( int poolsize )
    {
        MongoManager.poolsize = poolsize;
    }

    public static String getHost(  )
    {
        return host;
    }

    public static void setHost( String host )
    {
        MongoManager.host = host;
    }

    public static int getPort(  )
    {
        return port;
    }

    public static void setPort( int port )
    {
        MongoManager.port = port;
    }

    public static int getBlocksize(  )
    {
        return blocksize;
    }

    public static void setBlocksize( int blocksize )
    {
        MongoManager.blocksize = blocksize;
    }
}
