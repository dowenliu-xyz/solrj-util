package me.dowen.solrj.query.filters;

import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;

import me.dowen.solrj.query.BaseQuery;
import me.dowen.solrj.query.QueryFilter;

/**
 * 查询结果约束
 * @author liufl / 2014年3月18日
 */
public class FilterQueryFilter implements QueryFilter {

	List<String> fqs = new LinkedList<String>(); // 结果过滤查询语句列表

	/**
	 * 增加过滤语句
	 * @param fq 过滤语句
	 * @return
	 */
	public FilterQueryFilter addFilterQuery(String fq) {
		this.fqs.add(fq);
		return this;
	}

	/**
	 * 增加过滤查询
	 * @param fq
	 * @return
	 */
	public FilterQueryFilter addFilterQuery(BaseQuery fq) {
		this.fqs.add(fq.toString());
		return this;
	}

	public SolrQuery filter(SolrQuery query) {
		// 注入过滤
		for (String fq : this.fqs) {
			query.addFilterQuery(fq);
		}
		return query;
	}

}
