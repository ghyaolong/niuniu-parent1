package com.mouchina.base.cache.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存引擎的默认实现，数据直接存储在jvm的内存中。
 *
 * @author
 */
public class DefaultCacheEngine
    implements CacheEngine
{
    /**
     * 数据存储区，key:完全限定名，value:完全限定名下的数据。
     */
    private Map<String, Object> cache = new ConcurrentHashMap<String, Object>(  );

    /**
     * Inits the cache engine.
     */
    @Override
    public void init(  )
    {
        this.cache = new ConcurrentHashMap<String, Object>(  );
    }

    /**
     * Stops the cache engine
     */
    @Override
    public void stop(  )
    {
        this.cache = null;
    }

    /**
     * Adds a new object to the cache.
     *
     * @param key
     *            The key to associate with the object.
     * @param value
     *            The object to cache
     */
    @Override
    public void add( String key, Object value )
    {
        this.add( key, value );
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
     *
     */
    @Override
    public void add( String key, Object value, int liveSec )
    {
        cache.put( key, value );
    }

    /**
     * Gets some object from the cache.
     *
     * @param key
     *            The key to get
     * @return The cached object, or <code>null</code> if no entry was found
     */
    @Override
    public Object get( String key )
    {
        return cache.get( key );
    }

    /**
     * Removes an entry from the cache.
     *
     * @param key
     *            The key to remove
     */
    @Override
    public void remove( String key )
    {
        cache.remove( key );
    }

    /**
     * Removes all entry from the cache
     *
     */
    @Override
    public void removeAll(  )
    {
        cache.clear(  );
    }
}
