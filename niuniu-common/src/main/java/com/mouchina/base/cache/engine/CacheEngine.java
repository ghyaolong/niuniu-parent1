package com.mouchina.base.cache.engine;

import net.rubyeye.xmemcached.exception.MemcachedException;

import java.util.concurrent.TimeoutException;

/**
 * 缓存引擎接口
 *
 * @author
 *
 */
public interface CacheEngine
{
    /**
     * Inits the cache engine.
     */
    public void init();

    /**
     * Stops the cache engine
     */
    public void stop();

    /**
     * Adds a new object to the cache.
     *
     * @param key
     *            The key to associate with the object.
     * @param value
     *            The object to cache
     */
    public void add(String key, Object value)
             throws TimeoutException, InterruptedException, MemcachedException;

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
    public void add(String key, Object value, int liveSec)
             throws TimeoutException, InterruptedException, MemcachedException;

    /**
     * Gets some object from the cache.
     *
     * @param key
     *            The key to get
     * @return The cached object, or <code>null</code> if no entry was found
     */
    public Object get(String key)
               throws TimeoutException, InterruptedException, MemcachedException;

    /**
     * Remove an entry from the cache.
     *
     * @param key
     *            The key to remove
     */
    public void remove(String key)
                throws TimeoutException, InterruptedException, MemcachedException;

    /**
     * Removes all entry from the cache
     *
     */
    public void removeAll()
                   throws TimeoutException, InterruptedException, MemcachedException;
}
