package org.sakaton.distributed.lock.sample.handler;

import org.sakaton.distributed.lock.core.DistributedLockTemplate;
import org.sakaton.distributed.lock.sample.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

/**
 * @author zhengshijun
 * @version created on 2020/11/14.
 */
@Component
public class StartProcessHandler implements ApplicationRunner {

	@Autowired
	private ProcessService processService;

	@Autowired
	private DistributedLockTemplate lockTemplate;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// processService.execute(System.currentTimeMillis());

		Lock lock = lockTemplate.getRedisLock("test-"+System.currentTimeMillis());
		lock.lockInterruptibly();
		try {
			System.out.println(Thread.currentThread().getName());
		} finally {
			lock.unlock();
		}
	}
}
