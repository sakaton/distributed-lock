package org.sakaton.distributed.lock.context;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.BridgeMethodResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author sakaton
 * @version created on 2020/11/14.
 */

public class OperationMetadata {
	private final Method method;

	private final Class<?> targetClass;

	private final Method targetMethod;

	private final AnnotatedElementKey methodKey;

	private MethodSignature methodSignature;

	private final Object[] args;

	private final Object target;

	private final String expression;

	public OperationMetadata(ProceedingJoinPoint point, String expression) {
		methodSignature = MethodSignature.class.cast(point.getSignature());
		Method method = methodSignature.getMethod();
		this.method = BridgeMethodResolver.findBridgedMethod(method);
		this.targetClass = point.getTarget().getClass();
		this.targetMethod = (!Proxy.isProxyClass(targetClass) ?
				AopUtils.getMostSpecificMethod(method, targetClass) : this.method);
		this.methodKey = new AnnotatedElementKey(this.targetMethod, targetClass);

		this.args = point.getArgs();
		this.target = point.getTarget();
		this.expression = expression;
	}

	public Method getMethod() {
		return method;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}

	public AnnotatedElementKey getMethodKey() {
		return methodKey;
	}

	public MethodSignature getMethodSignature() {
		return methodSignature;
	}

	public Object[] getArgs() {
		return args;
	}

	public Object getTarget() {
		return target;
	}

	public String getExpression() {
		return expression;
	}
}
