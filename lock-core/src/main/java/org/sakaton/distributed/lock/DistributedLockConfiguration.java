package org.sakaton.distributed.lock;

import org.sakaton.distributed.lock.core.DistributedLockTemplate;
import org.sakaton.distributed.lock.core.LockAspectInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author sakaton
 * @version created on 2020/11/14.
 */
@Configuration
public class DistributedLockConfiguration {


	/**
	 * 分布式锁模板
	 *
	 * @param template 模板
	 * @return 返回
	 */
	@Bean
	public DistributedLockTemplate distributedLock(StringRedisTemplate template) {

		return new DistributedLockTemplate(template);
	}

	/**
	 * 分布式锁拦截器
	 *
	 * @param template 模板
	 * @return 返回
	 */
	@Bean
	public LockAspectInterceptor lockAspectInterceptor(DistributedLockTemplate template) {

		return new LockAspectInterceptor(template);
	}
}
