package org.sakaton.distributed.lock.core;

import org.apache.commons.lang3.StringUtils;

/**
 * @author sakaton
 * @version created on 2019/8/24.
 */
public class LuaScriptRepository {

	final static String UNLOCK_LUA_SCRIPT = getUnlockLuaScript();

	public final static String LOCK_LUA_SCRIPT = getLockLuaScript();

	private static String getUnlockLuaScript() {
		return "if redis.call(\"get\",KEYS[1]) == ARGV[1] then" +
				StringUtils.LF +
				StringUtils.repeat(StringUtils.SPACE, 2) + "return redis.call(\"del\",KEYS[1])" +
				StringUtils.LF +
				"else" +
				StringUtils.LF +
				StringUtils.repeat(StringUtils.SPACE, 2) + "return 0" +
				StringUtils.LF +
				"end";

	}

	private static String getLockLuaScript() {
		return "if redis.call(\"setnx\",KEYS[1], ARGV[1]) == 1 then" +
				StringUtils.LF +
				StringUtils.repeat(StringUtils.SPACE, 2) + "return redis.call(\"expire\", KEYS[1], ARGV[2])..''" +
				StringUtils.LF +
				"else" +
				StringUtils.LF +
				StringUtils.repeat(StringUtils.SPACE, 2) + "return redis.call(\"get\",KEYS[1])" +
				StringUtils.LF +
				"end";

	}

}

