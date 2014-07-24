package me.dowen.solrj.server;

import static org.junit.Assert.*;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author liufl
 * @date 2014年3月18日
 * @email hawkdowen@126.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test_appCtx.xml")
public class SolrServerFactoryTest {

	@Autowired
	private SolrServerConfiguration masterServerConf;
	@Autowired
	private SolrServerFactory masterFactory;
	@Autowired
	private SolrServerConfiguration slavesServerConf;
	@Autowired
	private SolrServerFactory slavesFactory;

	@Test
	@Ignore
	public void test() {
		assertNotNull(masterServerConf);
		assertNotNull(slavesServerConf);
	}

	@Test
	@Ignore
	public void testFactory() {
		assertNotNull(masterFactory);
		SolrServer solrServer = this.masterFactory.getUpdateSolrServer();
		assertTrue(solrServer instanceof ConcurrentUpdateSolrServer);
		solrServer = this.masterFactory.getUpdateSolrServer(true);
		assertTrue(solrServer.getClass().equals(HttpSolrServer.class));
		solrServer = this.masterFactory.getQuerySolrServer();
		assertTrue(solrServer.getClass().equals(HttpSolrServer.class));
		assertNotNull(slavesFactory);
		solrServer = this.slavesFactory.getUpdateSolrServer();
		assertTrue(solrServer == null);
		solrServer = this.slavesFactory.getUpdateSolrServer(true);
		assertTrue(solrServer == null);
		solrServer = this.slavesFactory.getQuerySolrServer();
		assertTrue(solrServer instanceof LBHttpSolrServer);
	}

}
