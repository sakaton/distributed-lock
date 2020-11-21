package org.sakaton.distributed.lock.core;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author sakaton
 * @version created on 2019/8/23.
 */

public class DistributedLock implements Lock {

	private static final RedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>(LuaScriptRepository.UNLOCK_LUA_SCRIPT, Long.class);

	private static final RedisScript<CharSequence> LOCK_SCRIPT = new DefaultRedisScript<>(LuaScriptRepository.LOCK_LUA_SCRIPT, CharSequence.class);

	private final static Logger log = LoggerFactory.getLogger(DistributedLock.class);

	private final static ThreadLocal<Map<String, AtomicInteger>> REENTRANT_MARK = new InheritableThreadLocal<Map<String, AtomicInteger>>(){
		@Override
		protected Map<String, AtomicInteger> initialValue() {
			return new ConcurrentHashMap<>(16);
		}
	};

	private final String key;

	private final StringRedisTemplate template;

	private final String value;

	private final long expire;

	private final TimeUnit unit;

	private volatile boolean interrupted;

	DistributedLock(String key, long expire, TimeUnit unit, StringRedisTemplate template) {
		this.key = key;
		this.expire = expire;
		this.unit = unit;
		this.template = template;
		this.value = UUID.randomUUID().toString();
	}

	DistributedLock(String key, long expire, TimeUnit unit, String applicationId, StringRedisTemplate template) {
		this.key = key;
		this.expire = expire;
		this.unit = unit;
		this.template = template;
		this.value = applicationId + "#" + Thread.currentThread().getId();
		log.info("分布式锁 key :{},value:{}", key, value);
	}

	@Override
	public void lock() {
		while (!interrupted && !tryLock()) {
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException ignored) {
				throw new RuntimeException(ignored);
			}
		}

	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		while (!tryLock()) {

			TimeUnit.MILLISECONDS.sleep(50);

			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
		}
	}

	@Override
	public boolean tryLock() {
		if (StringUtils.isNumeric(value)) {
			throw new IllegalArgumentException();
		}
		CharSequence result = template.execute(LOCK_SCRIPT, ImmutableList.of(key), value, String.valueOf(unit.toSeconds(expire)));

		if (StringUtils.isNumeric(result)) {
			return Boolean.TRUE;
		} else {
			return StringUtils.equals(result, value);
		}
	}

	@Override
	public boolean tryLock(long time, @NonNull TimeUnit unit) throws InterruptedException {

		if (time == 0) {
			return tryLock();
		} else {
			long currentTime = System.currentTimeMillis();
			long waitTime = unit.toMillis(time);
			while (!tryLock()) {
				if (System.currentTimeMillis() > (currentTime + waitTime)) {
					return Boolean.FALSE;
				}
				TimeUnit.MILLISECONDS.sleep(50);
			}
			return Boolean.TRUE;
		}
	}

	@Override
	public void unlock() {

		Long aLong = template.execute(UNLOCK_SCRIPT, ImmutableList.of(key), value);
		log.info("释放锁 key :{} , aLong:{}", key, aLong);
		if (!Objects.equals(aLong, 1L)) {
			throw new IllegalArgumentException();
		}
	}


	@NonNull
	@Override
	public Condition newCondition() {
		throw new IllegalArgumentException();
	}

}

