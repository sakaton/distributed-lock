package org.sakaton.distributed.lock.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author sakaton
 * @version created on 2019/9/7.
 */
public class DistributedLockTemplate {

	private final static Logger log = LoggerFactory.getLogger(DistributedLockTemplate.class);

	private final StringRedisTemplate redisTemplate;

	public DistributedLockTemplate(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Lock getRedisLock(String name, long expire, TimeUnit unit) {
		return new DistributedLock(name, expire, unit, redisTemplate);
	}


	public Lock getRedisLock(String name, String value, long expire, TimeUnit unit) {
		return new DistributedLock(name, expire, unit, value, redisTemplate);
	}

	public Lock getReentrantLock(String name){

		return null;
	}


	@MutexLock(expire = 15, unit = TimeUnit.SECONDS)
	private static void defaultMutexLock(){

	}

	public Lock getRedisLock(String name) {

		MutexLock mutexLock;
		try {
			mutexLock = DistributedLockTemplate.class.getDeclaredMethod("defaultMutexLock").getAnnotation(MutexLock.class);
		} catch (NoSuchMethodException e) {
			log.error("getRedisLock no such ", e);
			throw new IllegalArgumentException();
		}
		return new DistributedLock(name, mutexLock.expire(), mutexLock.unit(), redisTemplate);
	}
}

