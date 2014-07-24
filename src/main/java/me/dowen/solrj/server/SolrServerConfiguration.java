package me.dowen.solrj.server;

import java.util.HashSet;
import java.util.Set;

import me.dowen.util.Assert;

import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
import org.apache.solr.client.solrj.request.RequestWriter;

/**
 * SolrJ API SolrServer配置，用于向{@link SolrServerFactory}提供配置参数
 * 
 * @author liufl / 2014年3月16日
 */
public class SolrServerConfiguration {

	/**
	 * Factory类型
	 * @author liufl / 2014年7月24日
	 */
	public static enum Type {
		/**
		 * 通用类型
		 */
		COMMON() {
			@Override
			public boolean accept(Class<?> solrServerClass) {
				if (solrServerClass == null) {
					return false;
				}
				if (HttpSolrServer.class.equals(solrServerClass)) {
					return true;
				}
				if (this.accept(solrServerClass.getSuperclass())) {
					return true;
				}
				return false;
			}
		},
		/**
		 * 索引更新用类型
		 */
		UPDATE() {
			@Override
			public boolean accept(Class<?> solrServerClass) {
				if (solrServerClass == null) {
					return false;
				}
				if (ConcurrentUpdateSolrServer.class.equals(solrServerClass)) {
					return true;
				}
				if (HttpSolrServer.class.equals(solrServerClass)) {
					return true;
				}
				if (this.accept(solrServerClass.getSuperclass())) {
					return true;
				}
				return false;
			}
		},
		/**
		 * 索引查询用类型
		 */
		QUERY() {
			@Override
			public boolean accept(Class<?> solrServerClass) {
				if (solrServerClass == null) {
					return false;
				}
				if (LBHttpSolrServer.class.equals(solrServerClass)) {
					return true;
				}
				if (HttpSolrServer.class.equals(solrServerClass)) {
					return true;
				}
				if (this.accept(solrServerClass.getSuperclass())) {
					return true;
				}
				return false;
			}
		};
		/**
		 * 判断此类型ServerFactory是否支持产生指定类型的SolrServer
		 * @param solrServerClass SolrServer实现类
		 * @return
		 */
		public abstract boolean accept(Class<?> solrServerClass);
	}

	/**
	 * 连接等待时长
	 */
	public static final int CONNECTION_TIMEOUT = 500;
	/**
	 * 每个服务器最多并发连接数
	 */
	public static final int MAX_CONNS_PER_HOST = 32;
	/**
	 * 从此客户端发出的最多并发连接数
	 */
	public static final int MAX_TATAL_CONNS = 128;
	/**
	 * 等待响应时长
	 */
	public static final int READ_TIMEOUT = 1500;
	/**
	 * 服务器活动检测时间间隔
	 */
	public static final int ALIVE_CHECK_INTERVAL = 5000;

	private Set<String> serverUrls = new HashSet<String>(); // 服务器连接地址列表
	private Type type = Type.COMMON;
	private int connectionTimeout = CONNECTION_TIMEOUT;
	private ResponseParser responseParser = new BinaryResponseParser();
	private RequestWriter requestWriter = new BinaryRequestWriter();
	private int maxConnectionsPerHost = MAX_CONNS_PER_HOST;
	private boolean retry = false;
	private int maxTotalConnections = MAX_TATAL_CONNS;
	private int readTimeout = READ_TIMEOUT;
	private int aliveCheckInterval = ALIVE_CHECK_INTERVAL;

	/**
	 * get the urls/url of the solr core(s) query for.
	 * 
	 * @return string set that keeps the urls/url of solr core(s)
	 */
	public Set<String> getServerUrls() {
		return serverUrls;
	}

	/**
	 * set the urls/url of the solr core(s) query for.
	 * 
	 * @param serverUrls
	 *            string set that keeps the urls/url of solr core(s).must be
	 *            <strong>NOT EMPTY</strong> set
	 */
	public void setServerUrls(Set<String> serverUrls) {
		Assert.notEmpty(serverUrls, "url string set of solr core(s) is null or empty");
		this.serverUrls = serverUrls;
	}

	/**
	 * get the usage type of SolrServer
	 * 
	 * @return
	 */
	public Type getType() {
		return type;
	}

	/**
	 * get the usage type of SolrServer<br/>
	 * default {@link SolrServerConfiguration.Type#COMMON}
	 * 
	 * @param type
	 */
	public void setType(Type type) {
		Assert.notNull(type, "the Type of SolrServer should not be empty");
		this.type = type;
	}

	/**
	 * Connection timeout in milliseconds when try to connect to solr server.
	 * 
	 * @return timeout in milliseconds
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * set connection timeout in milliseconds when try to connect to solr
	 * server.
	 * 
	 * @param connectionTimeout
	 *            timeout in milliseconds. must be <strong>positive</strong>
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		Assert.isPositive(connectionTimeout, "timeout must be positive");
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * the response parser the SolrServer to be build use as default.
	 * 
	 * @return
	 */
	public ResponseParser getResponseParser() {
		return responseParser;
	}

	/**
	 * set the response parser the SolrServer to be build use as default.
	 * 
	 * @param responseParser
	 *            the response parser the SolrServer to be build use as
	 *            default.<strong>NOT BE NULL</strong>
	 */
	public void setResponseParser(ResponseParser responseParser) {
		if (responseParser == null) {
			throw new IllegalAccessError("response parset should not be null");
		}
		this.responseParser = responseParser;
	}

	public RequestWriter getRequestWriter() {
		return requestWriter;
	}

	public void setRequestWriter(RequestWriter requestWriter) {
		this.requestWriter = requestWriter;
	}

	/**
	 * the max connnections per host
	 * 
	 * @return
	 */
	public int getMaxConnectionsPerHost() {
		return maxConnectionsPerHost;
	}

	/**
	 * set the max connections per host
	 * 
	 * @param maxConnectionsPerHost
	 *            max connections per host. must be <strong>positive</strong>
	 */
	public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
		Assert.isPositive(maxConnectionsPerHost, "connection num must be positive");
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	/**
	 * whether retry a query when it fails.
	 * 
	 * @return
	 */
	public boolean isRetry() {
		return retry;
	}

	/**
	 * whether retry a query when it fails.
	 * 
	 * @param retry
	 *            if true, retry once when fails.
	 */
	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	/**
	 * the max connections of this client.
	 * 
	 * @return
	 */
	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	/**
	 * set the max connections this client can make.<br/>
	 * this setting while limit maxConnectionsPerHost if it is smaller than
	 * maxConnectionsPerHost.
	 * 
	 * @param maxTotalConnections
	 *            connection num.must be <strong>positive</strong>.
	 */
	public void setMaxTotalConnections(int maxTotalConnections) {
		Assert.isPositive(maxTotalConnections, "connection num must be positive");
		this.maxTotalConnections = maxTotalConnections;
	}

	/**
	 * the time length in milliseconds waiting for result after a request is
	 * commited.
	 * 
	 * @return
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * set the result read waiting time in milliseconds.
	 * 
	 * @param readTimeout
	 *            waiting time in milliseoonds. must be
	 *            <strong>positive</strong>
	 */
	public void setReadTimeout(int readTimeout) {
		Assert.isPositive(readTimeout, "timeout must be positive");
		this.readTimeout = readTimeout;
	}

	/**
	 * {@code **}<strong>ONLY for {@link LBHttpSolrServer}</strong>{@code **}<br/>
	 * LBHttpSolrServer use this for keeping pinging the dead servers at fixed
	 * interval to find if it is alive
	 * 
	 * @return pinging interval in milliseconds
	 */
	public int getAliveCheckInterval() {
		return aliveCheckInterval;
	}

	/**
	 * {@code **}<strong>ONLY for {@link LBHttpSolrServer}</strong>{@code **}<br/>
	 * LBHttpSolrServer use this for keeping pinging the dead servers at fixed
	 * interval to find if it is alive.<br/>
	 * this is the setting method.
	 * 
	 * @param aliveCheckInterval
	 *            pinging interval in milliseconds. must be
	 *            <strong>positive</strong>
	 */
	public void setAliveCheckInterval(int aliveCheckInterval) {
		Assert.isPositive(aliveCheckInterval, "timeout must be positive");
		this.aliveCheckInterval = aliveCheckInterval;
	}

}
