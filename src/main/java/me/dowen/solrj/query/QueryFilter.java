package me.dowen.solrj.query;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * 查询条件填充过滤器
 * @author liufl / 2014年3月18日
 */
public interface QueryFilter {

	/**
	 * 过滤操作。以传入的查询条件作为基础，增加或修改条件成新的查询条件返回。不要求也不保证返回的是原对象。
	 * @param query 基础查询条件
	 * @return 增加或修改条件后的查询条件
	 */
	SolrQuery filter(SolrQuery query);

}
