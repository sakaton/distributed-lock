package org.sakaton.distributed.lock.core;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author sakaton
 * @version created on 2019/9/16.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MutexLock {


	String key() default StringUtils.EMPTY;

	/**
	 * 是否尝试
	 *
	 * @return bool
	 */
	boolean isTry() default true;

	/**
	 * 失效时间
	 *
	 * @return 时间
	 */
	long expire() default 3000;

	/**
	 * 等待时间
	 *
	 * @return 时间
	 */
	long time() default 3000;

	/**
	 * 单位 默认为毫秒
	 *
	 * @return 单位
	 */
	TimeUnit unit() default TimeUnit.MILLISECONDS;


}

