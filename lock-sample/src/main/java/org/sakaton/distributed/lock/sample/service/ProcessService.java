package org.sakaton.distributed.lock.sample.service;

import org.sakaton.distributed.lock.core.MutexLock;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author zhengshijun
 * @version created on 2020/11/14.
 */
@Service
public class ProcessService {


	@MutexLock(key = "'demo-'+#id", expire = 15, unit = TimeUnit.SECONDS)
	public void execute(Long id) {


	}
}
