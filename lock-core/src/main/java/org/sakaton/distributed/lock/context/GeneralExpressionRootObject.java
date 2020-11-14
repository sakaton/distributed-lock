package org.sakaton.distributed.lock.context;

import java.lang.reflect.Method;

/**
 * @author sakaton
 * @version created on 2020/11/14.
 */

class GeneralExpressionRootObject {


	private final Method method;

	private final Object[] args;

	private final Object target;

	private final Class<?> targetClass;


	GeneralExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass) {
		this.method = method;
		this.target = target;
		this.targetClass = targetClass;
		this.args = args;
	}


	public Method getMethod() {
		return method;
	}

	public Object[] getArgs() {
		return args;
	}

	public Object getTarget() {
		return target;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}
}
