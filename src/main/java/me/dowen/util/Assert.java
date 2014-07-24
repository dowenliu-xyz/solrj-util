package me.dowen.util;

import java.util.Collection;

/**
 * 断言工具类
 * @author liufl / 2014年3月18日
 */
public abstract class Assert {

	/**
	 * 对象不是{@code null}
	 * @param obj 对象
	 * @param message 断言失败信息
	 */
	public static void notNull(Object obj, String message) {
		if (obj == null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 对象不是{@code null}
	 * @param obj 对象
	 */
	public static void notNull(Object obj) {
		notNull(obj, "The object is null");
	}

	/**
	 * 集合不是空的
	 * @param collection 集合
	 * @param message 断言失败信息
	 */
	public static void notEmpty(Collection<?> collection, String message) {
		if (collection == null || collection.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 集合不是空的
	 * @param collection 集合
	 */
	public static void notEmpty(Collection<?> collection) {
		notEmpty(collection, "The collection is empty");
	}

	/**
	 * 是正数
	 * @param value 数
	 * @param massage 断言失败信息
	 */
	public static void isPositive(int value, String massage) {
		if (value <= 0) {
			throw new IllegalArgumentException(massage);
		}
	}

	/**
	 * 是正数
	 * @param value
	 */
	public static void isPositive(int value) {
		isPositive(value, value + " is not positive.");
	}

	/**
	 * 字符串不是空白
	 * @param str 字符串
	 * @param message 断言失败信息
	 */
	public static void notBlank(String str, String message) {
		if (str == null || "".equals(str.trim())) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 字符串不是空白
	 * @param str 字符串
	 */
	public static void notBlank(String str) {
		notBlank(str, "blank string");
	}

	/**
	 * 字符串不是空的
	 * @param str 字符串
	 * @param message 断言失败信息
	 */
	public static void notEmpty(String str, String message) {
		if (str == null || "".equals(str)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 字符串不是空的
	 * @param str
	 */
	public static void notEmpty(String str) {
		notBlank(str, "empty string");
	}

}
