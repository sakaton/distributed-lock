package org.sakaton.distributed.lock.sample;

import org.sakaton.distributed.lock.DistributedLockConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author sakaton
 * @version created on 2020/11/14.
 */
@SpringBootApplication
@Import(DistributedLockConfiguration.class)
public class LockSampleBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(LockSampleBootstrap.class,args);
	}
}
