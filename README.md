solrj-util
==========

Solrj使用工具集，方便Solrj集成＆Solr查询构建

Spring集成示例
--------------

	<bean id="productSolrConf" class="me.dowen.solrj.server.SolrServerConfiguration">
		<property name="serverUrls">
			<set>
				<value>http://192.168.200.124:6180/solr/product</value>
				<value>http://192.168.200.124:7180/solr/product</value>
				<value>http://192.168.200.125:5180/solr/product</value>
				<value>http://192.168.200.125:6180/solr/product</value>
				<value>http://192.168.200.126:5180/solr/product</value>
				<value>http://192.168.200.126:6180/solr/product</value>
				<value>http://192.168.200.124:12180/solr/product</value>
				<value>http://192.168.200.125:12180/solr/product</value>
				<value>http://192.168.200.126:12180/solr/product</value>
			</set>
		</property>
		<property name="type" value="QUERY"/>
	</bean>
	<bean id="productSolrServerFactory" class="me.dowen.solrj.server.SolrServerFactory">
		<property name="conf" ref="productSolrConf"/>
	</bean>

	@Autowired
	private SolrServerFactory productSolrServerFactory;

QueryBuild示例
--------------

#BaseQuery

	BaseQuery baseQuery = new BaseQuery("text:" + key);
	baseQuery = new BaseQuery(baseQuery, Operator.OR,
						new BaseQuery("article:" + key));

#QueryBuilder

	SolrQuery baseQuery = ...
	QueryBuilder builder = new QueryBuilder(baseQuery);
	RangeFilter rangeFilter = new RangeFilter(2, 40);
	builder.addFilter(rangeFilter);
	query = builder.build();
