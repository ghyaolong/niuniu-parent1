package com.mouchina.base.cache;

import com.mouchina.base.cache.engine.CacheEngine;

import org.springframework.stereotype.Component;

/**
 * 缓存访问入口
 *
 * @author
 *
 */
public class Cache
{
    /**
     * 默认的缓存引擎
     */
    public CacheEngine engine;

    /**
     * 缓存引擎是否已经启动.
     */
    public static boolean started = false;

    /**
     * 缓存访问入口初始化
     */
    public void init(  )
    {
        if ( started )
        {
            return;
        }

        engine.init(  );
        started = true;
    }

    /**
     * 缓存访问入口停止
     */
    public void stop(  )
    {
        engine.stop(  );
        started = false;
    }

    /**
     * 得到缓存引擎
     *
     * @return the engine
     */
    public CacheEngine getEngine(  )
    {
        return engine;
    }

    /**
     * 设置缓存引擎
     *
     * @param engine
     *            the engine to set
     */
    public void setEngine( CacheEngine engine )
    {
        this.engine = engine;
    }
}
