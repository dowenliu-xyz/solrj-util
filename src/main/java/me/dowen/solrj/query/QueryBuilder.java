package me.dowen.solrj.query;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import me.dowen.util.Assert;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * 复杂查询建造器
 * @author liufl / 2014年3月18日
 */
public class QueryBuilder {

	private final SolrQuery query; // 原始基础查询
	private List<QueryFilter> filters = new LinkedList<QueryFilter>(); // 条件填充过滤器

	/**
	 * 从空白查询开始组装的建造器构造器
	 */
	public QueryBuilder() {
		this(new SolrQuery());
	}

	/**
	 * 从指定查询为基础开始组装的建造器构造器
	 * @param solrQuery 基础查询
	 */
	public QueryBuilder(SolrQuery solrQuery) {
		Assert.notNull(solrQuery, "A basic query is needed.");
		this.query = solrQuery;
	}

	/**
	 * 取出基础查询
	 * @return
	 */
	public SolrQuery getBaseQuery() {
		return query;
	}

	/**
	 * 取出条件填充过滤器列表
	 * @return
	 */
	public List<QueryFilter> getFilters() {
		return filters;
	}

	/**
	 * 增加条件填充过滤器
	 * @param filter
	 * @return
	 */
	public QueryBuilder addFilter(QueryFilter filter) {
		this.filters.add(filter);
		return this;
	}

	/**
	 * 移除条件填充过滤器
	 * @param filter
	 * @return
	 */
	public QueryBuilder removeFilter(QueryFilter filter) {
		this.filters.remove(filter);
		return this;
	}

	/**
	 * 建造目标查询。返回的不是原基础查询
	 * @return 
	 */
	public SolrQuery build() {
		Iterator<QueryFilter> ite = this.filters.iterator();
		SolrQuery query = this.query.getCopy();
		while (ite.hasNext()) {
			QueryFilter filter = ite.next();
			filter.filter(query);
		}
		return query;
	}

	/**
	 * 以指定查询为基础建造目标查询。返回对象为新建对象
	 * @param solrQuery 指定的外部基础查询
	 * @return
	 */
	public SolrQuery build(SolrQuery solrQuery) {
		Iterator<QueryFilter> ite = this.filters.iterator();
		SolrQuery query = solrQuery.getCopy();
		while (ite.hasNext()) {
			QueryFilter filter = ite.next();
			filter.filter(query);
		}
		return query;
	}

}
