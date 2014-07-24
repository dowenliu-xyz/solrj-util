package me.dowen.solrj.query.filters;

import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;

import me.dowen.solrj.query.QueryFilter;
import me.dowen.util.Assert;

/**
 * 返回字段设置
 * @author liufl / 2014年3月18日
 */
public class ReturnFieldFilter implements QueryFilter {

	private boolean all = true; // 是否返回全部字段
	private List<String> fields = new LinkedList<String>(); // 返回字段列表

	/**
	 * 是否返回全部字段
	 * @return
	 */
	public boolean isAll() {
		return all;
	}

	/**
	 * 设置是否返回全部字段
	 * @param all 只允许{@code true}。若要设置为{@code false},使用{@link #addField(String)}或{@link #setFields(List)}方法。
	 */
	public void setAll(boolean all) {
		if (!all)
			throw new UnsupportedOperationException("use addField or setField method insted");
		this.all = all;
		this.fields.clear();
	}

	/**
	 * 取出返回字段列表
	 * @return
	 */
	public List<String> getFields() {
		return fields;
	}

	/**
	 * 增加返回字段
	 * @param field
	 * @return
	 */
	public ReturnFieldFilter addField(String field) {
		this.fields.add(field);
		this.all = this.fields.isEmpty();
		return this;
	}

	/**
	 * 设置返回字段列表
	 * @param fields 字段列表。不允许为空。
	 */
	public void setFields(List<String> fields) {
		Assert.notEmpty(fields, "no fields is set.");
		this.fields = fields;
		this.all = false;
	}

	public SolrQuery filter(SolrQuery query) {
		if (this.isAll()) { // 返回全部字段
			query.setFields((String[])null);
		} else { // 返回指定字段
			for (String field : this.fields) {
				query.addField(field);
			}
		}
		return query;
	}

}
