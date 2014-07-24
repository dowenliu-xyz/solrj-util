package me.dowen.solrj.query.filters;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;

import me.dowen.solrj.query.BaseQuery;
import me.dowen.solrj.query.QueryFilter;
import me.dowen.util.StringUtils;

/**
 * 层面分析条件过滤器
 * @author liufl / 2014年7月22日
 */
public class FacetFilter implements QueryFilter {

	private List<String> facetQueries = new LinkedList<String>(); // 层面分析条件列表
	private int limit = -1; // 返回数限制
	private String prefix = null; // 前缀过滤
	private String sort = FacetParams.FACET_SORT_COUNT; // 分析结果排序方式
	private int mincount = 1; // 统计数下限
	private boolean missing = false; // 统计空值
	private LinkedHashMap<String, FacetFieldParam> facetFields =
			new LinkedHashMap<String, FacetFilter.FacetFieldParam>(); // 层面分析fields条件 

	public SolrQuery filter(SolrQuery query) {
		// 处理层面分析查询
		for (String facetQuery : this.facetQueries) {
			query.addFacetQuery(facetQuery);
		}
		// （对每个field）结果返回数限制
		query.setFacetLimit(this.limit);
		// （对每个field）返回以指定前缀名开头的结果
		query.setFacetPrefix(this.prefix);
		// （对每个field）返回结果排序方式
		query.setFacetSort(this.sort);
		// （对每个field）返回结果统计数下限约束
		query.setFacetMinCount(this.mincount);
		// （对第个field）返回结果是否包括空值统计
		query.setFacetMissing(this.missing );
		// 对各field返回结果做单独设定
		for (FacetFieldParam ff : this.getFacetFields()) {
			if (ff != null) {
				String field = ff.getField();
				query.addFacetField(field);
				query.setFacetPrefix(field, ff.getPrefix());
				query.set("f." + field + "." + FacetParams.FACET_LIMIT, ff.getLimit());
				query.set("f." + field + "." + FacetParams.FACET_MINCOUNT, ff.getMincount());
				query.set("f." + field + "." + FacetParams.FACET_MISSING, ff.isMissing());
				query.set("f." + field + "." + FacetParams.FACET_SORT, ff.getSort());
			}
		}
		return query;
	}

	/**
	 * 取出层面分析查询列表
	 * @return
	 */
	public List<String> getFacetQueries() {
		return facetQueries;
	}

	/**
	 * 设置层面分析查询列表
	 * @param facetQueries
	 * @return
	 */
	public FacetFilter setFacetQueries(List<String> facetQueries) {
		this.facetQueries = facetQueries;
		return this;
	}

	/**
	 * 增加层面分析查询
	 * @param facetQuery
	 * @return
	 */
	public FacetFilter addFacetQuery(String facetQuery) {
		if (StringUtils.isNotBlank(facetQuery)) {
			this.facetQueries.add(facetQuery);
		}
		return this;
	}


	/**
	 * 增加层面分析查询
	 * @param facetQuery
	 * @return
	 */
	public FacetFilter addFacetQuery(BaseQuery facetQuery) {
		if (facetQuery != null) {
			this.facetQueries.add(facetQuery.toString());
		}
		return this;
	}

	/**
	 * 取出返回数限制。
	 * @return 返回数上限。-1表示无限制。
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 设置返回数限制
	 * @param limit 返回数上限。-1表示无限制。
	 * @return
	 */
	public FacetFilter setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * 设置返回结果前缀过滤
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * 设置返回结果前缀过滤
	 * @param prefix
	 * @return
	 */
	public FacetFilter setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	/**
	 * 取出结果排序方式
	 * @return
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * 设置结果排序方式
	 * @param sort
	 * @return
	 */
	public FacetFilter setSort(String sort) {
		this.sort = sort;
		return this;
	}

	/**
	 * 取出结果统计下限过滤。分析结果中count值低于此值的结果不会返回
	 * @return
	 */
	public int getMincount() {
		return mincount;
	}

	/**
	 * 设置结果统计下限过滤。分析结果中count值低于给定值的结果不会返回
	 * @param mincount
	 * @return
	 */
	public FacetFilter setMincount(int mincount) {
		this.mincount = mincount;
		return this;
	}

	/**
	 * 是否返回空值统计结果
	 * @return
	 */
	public Boolean getMissing() {
		return missing;
	}

	/**
	 * 设置是否返回空值统计结果
	 * @param missing
	 * @return
	 */
	public FacetFilter setMissing(boolean missing) {
		this.missing = missing;
		return this;
	}

	/**
	 * 取出各field的分析条件
	 * @return
	 */
	public Collection<FacetFieldParam> getFacetFields() {
		return facetFields.values();
	}

	/**
	 * 增加分析field
	 * @param field field名称
	 * @return
	 */
	public FacetFilter addFacetField(String field) {
		if (StringUtils.isNotBlank(field)) {
			this.facetFields.put(field, new FacetFieldParam(field));
		}
		return this;
	}

	/**
	 * 增加分析field
	 * @param field field分析条件
	 * @return
	 */
	public FacetFilter addFacetField(FacetFieldParam field) {
		if (field != null) {
			this.facetFields.put(field.getField(), field);
		}
		return this;
	}

	/**
	 * field层面分析参数
	 * @author liufl / 2014年7月24日
	 */
	public static class FacetFieldParam {
		private final String field; // field名
		private int limit = -1; // 返回数限制
		private String prefix = null; // 返回结果前缀限制
		private String sort = FacetParams.FACET_SORT_COUNT; // 结果排序方式
		private int mincount = 1; // 返回结果统计数下限约束
		private boolean missing = false; // 是否返回空值统计

		/**
		 * 构造器
		 * @param field field名
		 */
		public FacetFieldParam(String field) {
			this.field = field;
		}

		/**
		 * 取出返回数量限制
		 * @return
		 */
		public int getLimit() {
			return limit;
		}

		/**
		 * 设置返回数量限制
		 * @param limit
		 * @return
		 */
		public FacetFieldParam setLimit(int limit) {
			this.limit = limit;
			return this;
		}

		/**
		 * 取出结果前缀限制
		 * @return
		 */
		public String getPrefix() {
			return prefix;
		}

		/**
		 * 设置结果前缀限制
		 * @param prefix
		 * @return
		 */
		public FacetFieldParam setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		/**
		 * 取出结果排序方式
		 * @return
		 */
		public String getSort() {
			return sort;
		}

		/**
		 * 设置结果排序方式
		 * @param sort
		 * @return
		 */
		public FacetFieldParam setSort(String sort) {
			this.sort = sort;
			return this;
		}

		/**
		 * 取出结果统计数下限约束
		 * @return
		 */
		public int getMincount() {
			return mincount;
		}

		/**
		 * 设置结果统计数下限约束
		 * @param mincount
		 * @return
		 */
		public FacetFieldParam setMincount(int mincount) {
			this.mincount = mincount;
			return this;
		}

		/**
		 * 是否返回空值统计
		 * @return
		 */
		public boolean isMissing() {
			return missing;
		}

		/**
		 * 设置是否返回空值统计
		 * @param missing
		 * @return
		 */
		public FacetFieldParam setMissing(boolean missing) {
			this.missing = missing;
			return this;
		}

		/**
		 * 取出field名
		 * @return
		 */
		public String getField() {
			return field;
		}
	}

}
