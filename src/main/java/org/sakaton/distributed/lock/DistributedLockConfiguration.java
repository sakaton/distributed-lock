package org.sakaton.distributed.lock;

import org.sakaton.distributed.lock.core.DistributedLockTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author sakaton
 * @version created on 2020/11/14.
 */
@Configuration
public class DistributedLockConfiguration {


	@Bean
	public DistributedLockTemplate distributedLock(StringRedisTemplate redisTemplate){

		return new DistributedLockTemplate(redisTemplate);
	}
}
