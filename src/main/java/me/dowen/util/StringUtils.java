package me.dowen.util;

/**
 * 字符串工具类
 * @author liufl / 2014年3月18日
 */
public abstract class StringUtils {

	/**
	 * 字符串是空的
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * 字符串是空白
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return isEmpty(str) || "".equals(str.trim());
	}

	/**
	 * 字符串不是空白
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

}
