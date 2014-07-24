package me.dowen.solrj.query;

import me.dowen.util.Assert;

/**
 * 基础查询。用于生成基础查询语句
 * @author liufl / 2014年3月18日
 */
public class BaseQuery {

	/**
	 * 条件联结方式
	 * @author liufl / 2014年7月23日
	 */
	public static enum Operator {
		/**
		 * 与
		 */
		AND,
		/**
		 * 或
		 */
		OR
	}

	private String query; // 基本查询文本
	private BaseQuery left; // 联合查询左部
	private Operator operator; // 联合查询联结方式
	private BaseQuery right; // 联合查询右部
	private final boolean basic; // 是否基本查询

	/**
	 * 无参构造器，效果相当于{@code BaseQuery("*")}
	 */
	public BaseQuery() {
		this("*");
	}

	/**
	 * 使用指定基本查询语句构建查询
	 * @param query 查询语句。
	 * 若为空，则相当于使用“*”作为基本语句。
	 * 若语句中包含“or”、“and”（不区分大小写）则认为此查询不是基本查询，是联合查询
	 */
	public BaseQuery(String query) {
		if (query == null || "".equals(query.trim())) {
			query = "*";
		}
		this.query = query;
		this.query = this.query.replaceAll("\\s+", " ");
		this.basic = !(this.query.contains(" or ") ||
				this.query.contains(" OR ") || 
				this.query.contains(" and ") ||
				this.query.contains(" AND "));
	}

	/**
	 * 构建联合查询
	 * @param left 查询左部
	 * @param operator 联结方式
	 * @param right 查询右部
	 */
	public BaseQuery(BaseQuery left, Operator operator, BaseQuery right) {
		Assert.notNull(left, "Left query part is null.");
		Assert.notNull(operator, "Query operator is null.");
		Assert.notNull(right, "Right query part is null.");
		this.left = left;
		this.operator = operator;
		this.right = right;
		this.basic = false;
	}

	/**
	 * 是否基本查询
	 * @return
	 */
	public boolean isBasic() {
		return basic;
	}

	/**
	 * 生成查询对象对应的查询语句。
	 */
	@Override
	public String toString() {
		if (this.query != null) {
			// 有原基础查询语句，不用解析直接输出
			return this.query;
		}
		StringBuilder sb = new StringBuilder();
		// 左部
		if (this.left.isBasic()) { // 左部是基本查询
			sb.append(this.left.toString());
		} else { // 左部是联合查询
			sb.append('(').append(this.left.toString()).append(')');
		}
		// 联结方式
		sb.append(' ').append(this.operator).append(' ');
		// 右部
		if (this.right.isBasic()) { // 右部是基本查询
			sb.append(this.right.toString());
		} else { // 右部是联合查询
			sb.append('(').append(this.right.toString()).append(')');
		}
		return sb.toString();
	}

}
