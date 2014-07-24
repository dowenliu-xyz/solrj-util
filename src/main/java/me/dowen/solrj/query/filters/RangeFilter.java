package me.dowen.solrj.query.filters;

import org.apache.solr.client.solrj.SolrQuery;

import me.dowen.solrj.query.QueryFilter;

/**
 * 结果区间（翻页）过滤器
 * @author liufl / 2014年3月18日
 */
public class RangeFilter implements QueryFilter {

	private int start; // 起始偏移量
	private int rows; // 返回行数

	/**
	 * 构造器
	 * @param start 起始偏移量
	 * @param rows 返回行数
	 */
	public RangeFilter(int start, int rows) {
		this.start = start;
		this.rows = rows;
	}

	/**
	 * 取出起始偏移量
	 * @return
	 */
	public int getStart() {
		return start;
	}

	/**
	 * 设置起始偏移量
	 * @param start
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * 取出返回行数
	 * @return
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * 设置返回行数
	 * @param rows
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	public SolrQuery filter(SolrQuery query) {
		return query.setStart(this.start).setRows(this.rows);
	}

}
