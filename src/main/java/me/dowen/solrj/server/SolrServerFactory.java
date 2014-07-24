package me.dowen.solrj.server;

import java.net.MalformedURLException;
import java.util.Iterator;

import me.dowen.solrj.server.SolrServerConfiguration.Type;
import me.dowen.util.Assert;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SolrJ API SolrServer工厂类
 * @author liufl / 2014年3月16日
 */
public class SolrServerFactory {

	private Logger log = LoggerFactory.getLogger(getClass());

	private SolrServer httpSolrServer; // 通用SolrServer
	private SolrServer updateSolrServer; // 更新用SolrServer
	private boolean updateSolrServerBuilt; // 更新用SolrServer生成标志
	private SolrServer querySolrServer; // 查询用SolrServer
	private boolean querySolrServerBuilt; // 查询用SolrServer生成标志

	private SolrServerConfiguration conf; // 配置信息

	/**
	 * SolrServer 配置
	 * @return
	 */
	public SolrServerConfiguration getConf() {
		return conf;
	}

	/**
	 * 设置SolrServer配置
	 * @param conf
	 */
	public void setConf(SolrServerConfiguration conf) {
		validConf(conf); // 校验
		this.conf = conf;
	}

	private void validConf() {
		if (this.conf == null) {
			throw new IllegalStateException("not configed!");
		}
	}

	private void validConf(SolrServerConfiguration conf) {
		Assert.notNull(conf, "conf is null"); // 对象不是null
		Assert.notEmpty(conf.getServerUrls(), "no serverUrl is provided"); // 至少提供一条Server地址
	}

	/**
	 * 是否单服务器节点访问
	 * @return
	 */
	private boolean isSingle() {
		return this.conf.getServerUrls().size() == 1;
	}

	/**
	 * 获取此solr core的更新用SolrServer实例<br/>
	 * 如果有可能，优先返回ConcurrentUpdateSolrServer
	 * @return
	 */
	public SolrServer getUpdateSolrServer() {
		if (!this.updateSolrServerBuilt) {
			this.buildUpdateSolrServer();
		}
		return this.updateSolrServer;
	}

	/**
	 * 获取此solr core的更新用SolrServer实例
	 * @param standard true:HttpSolrServer;false:与getUpdateSolrServer()行为相同
	 * @return
	 */
	public SolrServer getUpdateSolrServer(boolean standard) {
		if (!this.updateSolrServerBuilt) {
			this.buildUpdateSolrServer();
		}
		return standard ? this.httpSolrServer : this.updateSolrServer;
	}

	/**
	 * 建造更新用Server
	 */
	private void buildUpdateSolrServer() {
		this.validConf(); // 必须是已配置的对象
		if (this.isSingle()) { // 必须是单服务器节点
			Type type = this.conf.getType();
			// 标准SolrServer
			if (type.accept(HttpSolrServer.class)) {
				this.httpSolrServer = this.buildHttpSolrServer();
			}
			// 更新用SolrServer
			if (type.accept(ConcurrentUpdateSolrServer.class)) {
				this.updateSolrServer = this.buildConcurrentUpdateSolrServer();
			} else if (type.accept(HttpSolrServer.class)) {
				this.updateSolrServer = this.httpSolrServer;
			}
		}
		this.updateSolrServerBuilt = true;
	}

	/**
	 * 获取此solr core的查询用SolrServer实例
	 * @return
	 */
	public SolrServer getQuerySolrServer() {
		if (!this.querySolrServerBuilt) {
			this.buildQuerySolrServer();
		}
		return this.querySolrServer;
	}

	/**
	 * 建造查询用SolrServer
	 */
	private void buildQuerySolrServer() {
		this.validConf(); // 必须是已配置的对象
		Type type = this.conf.getType();
		if (this.isSingle()) { // 单服务器节点
			if (type.accept(HttpSolrServer.class)) {
				this.querySolrServer = this.buildHttpSolrServer();
				this.httpSolrServer = this.querySolrServer;
			}
		}
		if (this.querySolrServer == null) { // 负载均衡SolrServer
			if (type.accept(LBHttpSolrServer.class)) {
				this.querySolrServer = this.buildLBHttpSolrServer();
			}
		}
		this.querySolrServerBuilt = true;
	}

	private SolrServer buildConcurrentUpdateSolrServer() {
		String serverUrl = this.conf.getServerUrls().iterator().next();
		ConcurrentUpdateSolrServer solrServer = new ConcurrentUpdateSolrServer(serverUrl, 256, 8);
		solrServer.setConnectionTimeout(this.conf.getConnectionTimeout());
		solrServer.setParser(this.conf.getResponseParser());
		solrServer.setSoTimeout(this.conf.getReadTimeout());
		solrServer.setRequestWriter(this.conf.getRequestWriter());
		return solrServer;
	}

	private SolrServer buildHttpSolrServer() {
		String serverUrl = this.conf.getServerUrls().iterator().next();
		HttpSolrServer solrServer = new HttpSolrServer(serverUrl);
		solrServer.setConnectionTimeout(this.conf.getConnectionTimeout());
		solrServer.setDefaultMaxConnectionsPerHost(this.conf.getMaxConnectionsPerHost());
		solrServer.setFollowRedirects(false);
		solrServer.setMaxRetries(this.conf.isRetry() ? 1 : 0);
		solrServer.setMaxTotalConnections(this.conf.getMaxTotalConnections());
		solrServer.setParser(this.conf.getResponseParser());
		solrServer.setSoTimeout(this.conf.getReadTimeout());
		solrServer.setRequestWriter(this.conf.getRequestWriter());
		return solrServer;
	}

	private SolrServer buildLBHttpSolrServer() {
		Iterator<String> ite = this.conf.getServerUrls().iterator();
		String url = null;
		if (ite.hasNext()) {
			url = ite.next();
		}
		LBHttpSolrServer solrServer = null;
		try {
			solrServer = new LBHttpSolrServer(url);
			while (ite.hasNext()) {
				solrServer.addSolrServer(ite.next());
			}
		} catch (MalformedURLException e) {
			log.error("Illegal URL", e);
			solrServer = null;
			return solrServer;
		}
		solrServer.setAliveCheckInterval(this.conf.getAliveCheckInterval());
		solrServer.setConnectionTimeout(this.conf.getConnectionTimeout());
		solrServer.setParser(this.conf.getResponseParser());
		solrServer.setSoTimeout(this.conf.getReadTimeout());
		solrServer.setRequestWriter(this.conf.getRequestWriter());
		return solrServer;
	}

}
