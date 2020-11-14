package org.sakaton.distributed.lock.core;


import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.sakaton.distributed.lock.context.AbstractAspectSupport;
import org.sakaton.distributed.lock.context.OperationMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Objects;
import java.util.concurrent.locks.Lock;

/**
 *
 *    {@link TransactionInterceptor}
 * @author sakaton
 * @version created on 2019/11/14.
 */
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE + 100)
public class LockAspectInterceptor extends AbstractAspectSupport {

	private final static Logger log = LoggerFactory.getLogger(LockAspectInterceptor.class);

	private final DistributedLockTemplate lockTemplate;

	public LockAspectInterceptor(DistributedLockTemplate lockTemplate) {
		this.lockTemplate = lockTemplate;

	}

	/**
	 * 环绕
	 *
	 * @param point     参数
	 * @param mutexLock 注解
	 * @return 返回结果
	 */
	@Around("@annotation(mutexLock)")
	public Object execute(ProceedingJoinPoint point, MutexLock mutexLock) throws Throwable {


		MethodSignature methodSignature = ((MethodSignature) point.getSignature());
		Object object;
		Lock lock = null;
		boolean unlock = Boolean.TRUE;
		try {
			String key = format(new OperationMetadata(point, mutexLock.key()));
			log.info("lockKey :{}, targetMethod:{}", key, methodSignature.getName());
			lock = lockTemplate.getRedisLock(key, mutexLock.expire(), mutexLock.unit());
			if (mutexLock.isTry()) {
				if (!lock.tryLock(mutexLock.time(), mutexLock.unit())) {
					unlock = Boolean.FALSE;
					Class<?> clazz = methodSignature.getReturnType();
					 if (Boolean.class.equals(clazz)) {
						return Boolean.FALSE;
					}
					log.info("proxy target method returnType :{}", clazz);
					return null;
				}
			} else {
				lock.lock();
			}
			object = point.proceed();
		} catch (Throwable e) {
			log.error(StringUtils.EMPTY, e);
			throw e;
		} finally {
			if (unlock && Objects.nonNull(lock)) {
				lock.unlock();
			}
		}
		return object;
	}

}
