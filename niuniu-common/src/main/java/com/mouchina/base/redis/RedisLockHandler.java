package com.mouchina.base.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Component;

import org.springframework.util.CollectionUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

@Component
public class RedisLockHandler {
	private static Logger logger = LogManager.getLogger("RedisBillLockHandler");
	private static final int DEFAULT_SINGLE_EXPIRE_TIME = 30;
	private static final int DEFAULT_BATCH_EXPIRE_TIME = 20;
	@Resource
	private RedisServer redisServer;

	/**
	 * 获取锁 如果锁可用 立即返回true， 否则返回false
	 * 
	 * @author http://blog.csdn.net/java2000_wl
	 * @see com.fx.platform.components.lock.IBillLockHandler#tryLock(com.fx.platform.components.lock.String)
	 * @param billIdentify
	 * @return
	 */
	public boolean tryLock(String billIdentify) {
		return tryLock(billIdentify, 0L, null);
	}
	
	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
	 * 
	 * @author http://blog.csdn.net/java2000_wl
	 * @see com.fx.platform.components.lock.IBillLockHandler#tryLock(com.fx.platform.components.lock.String)
	 * @param billIdentify
	 * @return
	 */
	public boolean tryLock(String billIdentify,long timeout) {
		return tryLock(billIdentify, timeout, null);
	}

	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
	 * 
	 * @author http://blog.csdn.net/java2000_wl
	 * @see com.fx.platform.components.lock.IBillLockHandler#tryLock(com.fx.platform.components.lock.String,
	 *      long, java.util.concurrent.TimeUnit)
	 * @param billIdentify
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public boolean tryLock(String key, long timeout, TimeUnit unit) {
		ShardedJedisPool pool = null;
		ShardedJedis jedis = null;

		try {
			pool = redisServer.getPool();
			jedis = pool.getResource();

			long nano = System.nanoTime();

			do {
				logger.debug("try lock key: " + key);

				Long i = jedis.setnx(key, key);

				if (i == 1) {
					//key值不存在，设置成功
					jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);
					logger.debug("get lock, key: " + key + " , expire in "
							+ DEFAULT_SINGLE_EXPIRE_TIME + " seconds.");

					return Boolean.TRUE;
				} else { // 存在锁

					if (logger.isDebugEnabled()) {
						String desc = jedis.get(key);
						logger.debug("key: " + key
								+ " locked by another business：" + desc);
					}
				}

				if (timeout == 0) {
					break;
				}

				Thread.sleep(300);
			} while ((System.nanoTime() - nano) < unit.toNanos(timeout));

			return Boolean.FALSE;
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
//			returnBrokenResource(pool, jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(pool, jedis);
		}

		return Boolean.FALSE;
	}

	public boolean tryLock(String key, long timeout,long millis) {
		ShardedJedisPool pool = null;
		ShardedJedis jedis = null;

		try {
			pool = redisServer.getPool();
			jedis = pool.getResource();

			long nano = System.nanoTime();

			do {
				logger.debug("try lock key: " + key);

				Long i = jedis.setnx(key, key);

				if (i == 1) {
					jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);
					logger.debug("get lock, key: " + key + " , expire in "
							+ DEFAULT_SINGLE_EXPIRE_TIME + " seconds.");

					return Boolean.TRUE;
				} else { // 存在锁

					if (logger.isDebugEnabled()) {
						String desc = jedis.get(key);
						logger.debug("key: " + key
								+ " locked by another business：" + desc);
					}
				}

				if (timeout == 0) {
					break;
				}

				Thread.sleep(millis);
			} while ((System.nanoTime() - nano) < TimeUnit.MILLISECONDS.toNanos(timeout));

			return Boolean.FALSE;
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
//			returnBrokenResource(pool, jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(pool, jedis);
		}

		return Boolean.FALSE;
	}

	
	/**
	 * 如果锁空闲立即返回 获取失败 一直等待
	 * 
	 * @author http://blog.csdn.net/java2000_wl
	 * @see com.fx.platform.components.lock.IBillLockHandler#lock(com.fx.platform.components.lock.String)
	 * @param billIdentify
	 */
	public void lock(String key) {
		ShardedJedisPool pool = null;
		ShardedJedis jedis = null;

		try {
			pool = redisServer.getPool();
			jedis = pool.getResource();

			do {
				logger.debug("lock key: " + key);

				Long i = jedis.setnx(key, key);

				if (i == 1) {
					jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);
					logger.debug("get lock, key: " + key + " , expire in "
							+ DEFAULT_SINGLE_EXPIRE_TIME + " seconds.");

					return;
				} else {
					if (logger.isDebugEnabled()) {
						String desc = jedis.get(key);
						logger.debug("key: " + key
								+ " locked by another business：" + desc);
					}
				}

				Thread.sleep(300);
			} while (true);
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
//			returnBrokenResource(pool, jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(pool, jedis);
		}
	}

	/**
	 * 释放锁
	 * 
	 * @author http://blog.csdn.net/java2000_wl
	 * @see com.fx.platform.components.lock.IBillLockHandler#unLock(com.fx.platform.components.lock.String)
	 * @param billIdentify
	 */
	public void unLock(String billIdentify) {
		List<String> list = new ArrayList<String>();
		list.add(billIdentify);
		unLock(list);
	}

	/**
	 * 批量获取锁 如果全部获取 立即返回true, 部分获取失败 返回false
	 * 
	 * @author http://blog.csdn.net/java2000_wl
	 * @date 2013-7-22 下午10:27:44
	 * @see com.fx.platform.components.lock.IBatchBillLockHandler#tryLock(java.util.List)
	 * @param billIdentifyList
	 * @return
	 */
	public boolean tryLock(List<String> billIdentifyList) {
		return tryLock(billIdentifyList, 0L, null);
	}

	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
	 * 
	 * @author http://blog.csdn.net/java2000_wl
	 * @param billIdentifyList
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public boolean tryLock(List<String> billIdentifyList, long timeout,
			TimeUnit unit) {
		ShardedJedisPool pool = null;
		ShardedJedis jedis = null;

		try {
			pool = redisServer.getPool();
			jedis = pool.getResource();

			List<String> needLocking = new CopyOnWriteArrayList<String>();
			List<String> locked = new CopyOnWriteArrayList<String>();
			long nano = System.nanoTime();

			do {
				// 构建pipeline，批量提交
				ShardedJedisPipeline pipeline = jedis.pipelined();

				for (String key : billIdentifyList) {
					needLocking.add(key);
					pipeline.setnx(key, key);
				}

				logger.debug("try lock keys: " + needLocking);

				// 提交redis执行计数
				List<Object> results = pipeline.syncAndReturnAll();

				for (int i = 0; i < results.size(); ++i) {
					Long result = (Long) results.get(i);
					String key = needLocking.get(i);

					if (result == 1) { // setnx成功，获得锁
						jedis.expire(key, DEFAULT_BATCH_EXPIRE_TIME);
						locked.add(key);
					}
				}

				needLocking.removeAll(locked); // 已锁定资源去除

				if (CollectionUtils.isEmpty(needLocking)) {
					return true;
				} else {
					// 部分资源未能锁住
					logger.debug("keys: " + needLocking
							+ " locked by another business：");
				}

				if (timeout == 0) {
					break;
				}

				Thread.sleep(500);
			} while ((System.nanoTime() - nano) < unit.toNanos(timeout));

			// 得不到锁，释放锁定的部分对象，并返回失败
			if (!CollectionUtils.isEmpty(locked)) {
				String[] keyArray = locked.toArray(new String[0]);

				for (String key : keyArray) {
					jedis.del(key);
					logger.debug("release lock, key :" + key);
				}
			}

			return false;
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
//			returnBrokenResource(pool, jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(pool, jedis);
		}

		return true;
	}

	/**
	 * 批量释放锁
	 * 
	 * @author http://blog.csdn.net/java2000_wl
	 * @see com.fx.platform.components.lock.IBatchBillLockHandler#unLock(java.util.List)
	 * @param billIdentifyList
	 */
	public void unLock(List<String> billIdentifyList) {
		ShardedJedisPool pool = null;
		ShardedJedis jedis = null;
		List<String> keys = new CopyOnWriteArrayList<String>();

		for (String identify : billIdentifyList) {
			keys.add(identify);
		}

		try {
			pool = redisServer.getPool();
			jedis = pool.getResource();

			String[] keyArray = keys.toArray(new String[0]);

			for (String key : keyArray) {
				jedis.del(key);
				logger.debug("release lock, key :" + key);
			}
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
//			returnBrokenResource(pool, jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(pool, jedis);
		}
	}

	/**
	 * 销毁连接
	 * 
	 * @author http://blog.csdn.net/java2000_wl
	 * @param jedis
	 */
	private void returnBrokenResource(ShardedJedisPool pool, ShardedJedis jedis) {
		if (jedis == null) {
			return;
		}

		try {
			// 容错
			pool.returnBrokenResource(jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @author http://blog.csdn.net/java2000_wl
	 * @param jedis
	 */
	private void returnResource(ShardedJedisPool pool, ShardedJedis jedis) {
		if (jedis == null) {
			return;
		}

		try {
			pool.returnResource(jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
