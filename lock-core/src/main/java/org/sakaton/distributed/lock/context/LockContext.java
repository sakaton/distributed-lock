package org.sakaton.distributed.lock.context;

/**
 * @author zhengshijun
 * @version created on 2020/11/14.
 */
public abstract class LockContext {

	protected static String OPEN = "'";

	protected static String CLOSE = "'";

	protected static String PREFIX = "sample:";

	protected static String LOCK_EXP = PREFIX + "lock:handle:";

	public static String FIRST_SAMPLE_LOCK_KEY = formatLockKey("sample:lock");

	public static String formatLockKey(String name) {
		return OPEN + LOCK_EXP + name + CLOSE;
	}
}
