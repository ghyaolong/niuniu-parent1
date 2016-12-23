package com.mouchina.base.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mouchina.base.common.page.ListRange;

/**
 * 缓存管理器,系统访问入口.
 *
 * @author
 *
 */
public class CacheManager
{
    private static Logger logger = LogManager.getLogger( "CacheManager" );
    private Cache cache;

    /**
     * 添加缓存(以主键字段作为key)
     *
     * @param primaryKey
     * @param object
     *            缓存中的类型
     *
     */
    public void addEntityByPrimaryKey( Serializable primaryKey, Object object )
    {
        if ( object == null )
        {
            return;
        }

        String key = CacheKey.getEntityKeyByPrimaryKey( object.getClass(  ),
                                                        primaryKey );
        logger.debug( "addEntityByPrimaryKey key:" + key );

        try
        {
            cache.getEngine(  ).add( key, object );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }

    /**
     * 添加缓存(以主键字段作为key)
     *
     * @param primaryKey
     * @param object
     *            缓存中的类型
     *
     */
    public void addEntityByPrimaryKey( Serializable primaryKey, Object object, int liveSec )
    {
        if ( object == null )
        {
            logger.debug( "addEntityByPrimaryKey object is null " );

            return;
        }

        String key = CacheKey.getEntityKeyByPrimaryKey( object.getClass(  ),
                                                        primaryKey );
        logger.debug( "getEntityByPrimaryKey key:" + key );

        try
        {
            cache.getEngine(  ).add( key, object, liveSec );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }

    /**
     * 添加缓存(以主键字段作为key)
     *
     * @param primaryKey
     * @param object
     *            缓存中的类型
     *
     */
    public void addEntityKey( Serializable primaryKey, Object object, int liveSec )
    {
        if ( object == null )
        {
            logger.debug( "addEntityByPrimaryKey object is null " );

            return;
        }

        String key = CacheKey.getEntityKeyByForeignKey( object.getClass(  ),
                                                        primaryKey );
        logger.debug( "getEntityByPrimaryKey key:" + key );

        try
        {
            cache.getEngine(  ).add( key, object, liveSec );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }

    /**
     * 获取缓存(以主键字段作为key)
     *
     * @param clazz
     *            缓存中的类型
     * @param primaryKey
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public <T> T getEntityByPrimaryKey( Class<T> clazz, Serializable primaryKey )
    {
        String key = CacheKey.getEntityKeyByPrimaryKey( clazz, primaryKey );
        logger.debug( "getEntityByPrimaryKey key:" + key );

        try
        {
            return (T) cache.getEngine(  ).get( key );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }

        return null;
    }

    /**
     * 删除缓存(以主键字段作为key)
     *
     * @param clazz
     *            缓存中的类型
     * @param primaryKey
     */
    @SuppressWarnings( "rawtypes" )
    public void deleteEntityByPrimaryKey( Class clazz, Serializable primaryKey )
    {
        String key = CacheKey.getEntityKeyByPrimaryKey( clazz, primaryKey );
        logger.debug( "deleteEntityByPrimaryKey key:" + key );

        try
        {
            cache.getEngine(  ).remove( key );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }

    /**
     * 添加缓存(以主键外的其他字段作为key)
     *
     * @param key
     * @param object
     *            對象
     */
    public void addEntityByKey( Serializable key, Object object )
    {
        if ( object == null )
        {
            logger.debug( "addEntityByKey object is null " );

            return;
        }

        String keys = CacheKey.getEntityKeyByForeignKey( object.getClass(  ),
                                                         key );
        logger.debug( "addEntityByKey key:" + key );

        try
        {
            cache.getEngine(  ).add( keys, object );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }

    /**
     * 添加缓存(以主键外的其他字段作为key)
     *
     * @param key
     * @param object
     *            對象
     */
    public void addEntityByKey( Serializable key, Object object, int alive )
    {
        if ( object == null )
        {
            logger.debug( "addEntityByKey object is null " );

            return;
        }

        String keys = CacheKey.getEntityKeyByForeignKey( object.getClass(  ),
                                                         key );
        logger.debug( "addEntityByKey key:" + key );

        try
        {
            cache.getEngine(  ).add( keys, object, alive );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }

    public <T> ListRange<T> getListRangeByMap( Class<T> clazz,Serializable listKey, Map<String, Object> map )
    {
        String key = CacheKey.getListKeyByMap( clazz,listKey, map );
        logger.debug( "getListByMap key:" + key );

        try
        {
            return (ListRange<T>) cache.getEngine(  ).get( key );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }

        return null;
    }
    /**
     * 获取缓存(以主键外的其他字段作为key)
     *
     * @param clazz
     *            缓存中的类型
     * @param key
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public <T> T getEntityByKey( Class<T> clazz, Serializable key )
    {
        String keys = CacheKey.getEntityKeyByForeignKey( clazz, key );
        logger.debug( "getEntityByKey key:" + keys );

        try
        {
            return (T) cache.getEngine(  ).get( keys );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }

        return null;
    }

    /**
     * 删除缓存(以主键外的其他字段作为key)
     *
     * @param clazz
     *            缓存中的类型
     * @param key
     */
    @SuppressWarnings( "rawtypes" )
    public void deleteEntityByKey( Class clazz, Serializable key )
    {
        String keys = CacheKey.getEntityKeyByForeignKey( clazz, key );
        logger.debug( "deleteEntityByKey key:" + key );

        try
        {
            cache.getEngine(  ).remove( keys );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }

    /**
     * 获取缓存(以查询条件map作为key)
     *
     * @param clazz
     *            缓存中集合的类型
     * @param map
     *            查询条件
     * @return
     */
    public <T> List<T> getListByMap( Class<T> clazz, Map<String, Object> map )
    {
        String key = CacheKey.getListKeyByMap( clazz, map );
        logger.debug( "getListByMap key:" + key );

        try
        {
            return (List<T>) cache.getEngine(  ).get( key );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }

        return null;
    }

    /**
     * 添加缓存(以查询条件map作为key)
     *
     * @param clazz
     *            集合中的类型
     * @param map
     *            查询条件
     * @param list
     *            集合
     */
    @SuppressWarnings( "rawtypes" )
    public void addListByMap( Class clazz, Map<String, Object> map, List list )
    {
        if ( ( list == null ) && ( list.size(  ) > 0 ) )
        {
            logger.debug( "addListByMap object is null " );

            return;
        }

        String key = CacheKey.getListKeyByMap( clazz, map );
        logger.debug( "addListByMap key:" + key );

        try
        {
            cache.getEngine(  ).add( key, list );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }

    /**
     * 删除缓存(以查询条件map作为key)
     *
     * @param clazz
     *            集合中的类型
     * @param map
     *            查询条件
     */
    @SuppressWarnings( "rawtypes" )
    public void deleteListByMap( Class clazz, Map<String, Object> map )
    {
        String key = CacheKey.getListKeyByMap( clazz, map );
        logger.debug( "deleteListByMap key:" + key );

        try
        {
            cache.getEngine(  ).remove( key );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }

    public Cache getCache(  )
    {
        return cache;
    }

    public void setCache( Cache cache )
    {
        this.cache = cache;
    }

    /**
     * 获取缓存(以查询条件map作为key)
     *
     * @param clazz
     *            缓存中集合的类型
     * @param map
     *            查询条件
     * @return
     */
    public <E, T> Map<E, T> getMapByMap( Class<E> mapKey, Class<T> clazz, Map<String, Object> map )
    {
        String key = CacheKey.getMapKeyByMap( mapKey, clazz, map );
        logger.debug( "getMapByMap key:" + key );

        try
        {
            return (Map<E, T>) cache.getEngine(  ).get( key );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }

        return null;
    }

    /**
     * 添加缓存(以查询条件map作为key)
     *
     * @param clazz
     *            集合中的类型
     * @param map
     *            查询条件
     * @param list
     *            集合
     */
    @SuppressWarnings( "rawtypes" )
    public void addMapByMap( Class mapKey, Class clazz, Map<String, Object> map, Map mapValue )
    {
        if ( ( mapValue == null ) && ( mapValue.size(  ) > 0 ) )
        {
            logger.debug( "addMapByMap object is null " );

            return;
        }

        String key = CacheKey.getMapKeyByMap( mapKey, clazz, map );
        logger.debug( "addMapByMap key:" + key );

        try
        {
            cache.getEngine(  ).add( key, mapValue );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }

    /**
     * 删除缓存(以查询条件map作为key)
     *
     * @param clazz
     *            集合中的类型
     * @param map
     *            查询条件
     */
    @SuppressWarnings( "rawtypes" )
    public void deleteMapByMap( Class mapKey, Class clazz, Map<String, Object> map )
    {
        String key = CacheKey.getMapKeyByMap( mapKey, clazz, map );
        logger.debug( "deleteMapByMap key:" + key );

        try
        {
            cache.getEngine(  ).remove( key );
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }
    @SuppressWarnings( "rawtypes" )
    public void addListRangeByMap( Class clazz,Serializable listKey, Map<String, Object> map, ListRange listRange ,int timeOut)
    {
        if ( ( listRange == null ) && ( listRange.getData().size(  ) > 0 ) )
        {
            logger.debug( "addListByMap object is null " );

            return;
        }

        String key = CacheKey.getListKeyByMap( clazz,listKey, map );
        logger.debug( "addListByMap key:" + key );

        try
        {
            cache.getEngine(  ).add( key, listRange ,timeOut);
        } catch ( TimeoutException e )
        {
            e.printStackTrace(  );
        } catch ( InterruptedException e )
        {
            e.printStackTrace(  );
        } catch ( MemcachedException e )
        {
            e.printStackTrace(  );
        }
    }
    public static void main( String[] args )
    {
    }
}
