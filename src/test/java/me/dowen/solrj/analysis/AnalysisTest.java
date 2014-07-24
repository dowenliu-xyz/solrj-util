package me.dowen.solrj.analysis;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import me.dowen.solrj.server.SolrServerFactory;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.AnalysisPhase;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.TokenInfo;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse.Analysis;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author liufl / 2014年6月17日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test_appCtx.xml")
public class AnalysisTest {

	@Autowired
	private SolrServerFactory masterFactory;

	@Test
	@Ignore
	public void test() throws SolrServerException, IOException {
		SolrServer solrServer = this.masterFactory.getQuerySolrServer();
		FieldAnalysisRequest request = new FieldAnalysisRequest();
		request.addFieldName("keywordBreaker");
		request.setFieldValue("耐克男鞋");
		FieldAnalysisResponse response = request.process(solrServer);
		Analysis analysis = response.getFieldNameAnalysis("keywordBreaker");
		Iterable<AnalysisPhase> analysisCollection = analysis.getIndexPhases();
		Iterator<AnalysisPhase> ite = analysisCollection.iterator();
		while (ite.hasNext()) {
			AnalysisPhase ap = ite.next();
			List<TokenInfo> tokenInfos = ap.getTokens();
			for (TokenInfo tokenInfo : tokenInfos) {
				String token = tokenInfo.getText();
				System.out.println(token);
			}
			System.out.println("------------------");
		}
	}

}
