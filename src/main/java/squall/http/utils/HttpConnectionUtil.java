package squall.http.utils;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squall.http.config.HttpProxySelector;
import squall.http.config.PoolConfig;
import squall.http.config.RequestConfigDelegater;
import squall.http.help.HttpHelper;

import java.net.URI;
import java.util.Map;


public class HttpConnectionUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpConnectionUtil.class);

	// private static boolean usePool = false;

	/**
	 * 设置连接池参数
	 * 
	 * @param poolConfig 连接池配置
	 * @return null为了继续连着调用
	 */
	public static HttpConnectionUtil initPool(PoolConfig poolConfig) {
		if (poolConfig != null) {
			phcm = new PoolingHttpClientConnectionManager();
			phcm.setMaxTotal(poolConfig.getMaxTotal());
			phcm.setDefaultMaxPerRoute(poolConfig.getDefaultMaxPerRoute());
			Map<String, Integer> specRoutes = poolConfig.getSpecHostsMax();
			if (specRoutes != null && specRoutes.size() != 0) {
				for (Map.Entry<String, Integer> entry : specRoutes.entrySet()) {
					String hostUri = entry.getKey();
					HttpRoute httpRoute = HttpHelper.getHttpRoute(hostUri, httpProxySelector);
					phcm.setMaxPerRoute(httpRoute, entry.getValue());
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param httpProxySelector
	 * @return null为了继续连着调用
	 */
	public static HttpConnectionUtil initProxy(HttpProxySelector httpProxySelector) {
		HttpConnectionUtil.httpProxySelector = httpProxySelector;
		return null;
	}

	/**
	 * 设置请求默认值
	 * 
	 * @param requestConfigDelegater
	 * @return null为了继续连着调用
	 */
	public static HttpConnectionUtil setRequestConfigDelegater(RequestConfigDelegater requestConfigDelegater) {
		HttpConnectionUtil.requestConfigDelegater = requestConfigDelegater;
		return null;
	}

	/**
	 * 获取http连接
	 * 
	 * @param uri
	 * @return http连接
	 */
	public static CloseableHttpClient getHttpClient(URI uri) {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();

		/* 是否使用代理 */
		HttpHost target = HttpHelper.getHttpHostByURI(uri);
		if (httpProxySelector != null) {
			HttpHost proxy = httpProxySelector.getProxy(target);
			if (proxy != null) {
				DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
				httpClientBuilder.setRoutePlanner(routePlanner);
			}
		}

		/* 是否使用连接池 */
		if (phcm != null) {
			httpClientBuilder.setConnectionManager(phcm).setConnectionManagerShared(true);
		}

		/* 超时设置 */
		if (requestConfigDelegater != null) {
			RequestConfig requestConfig = requestConfigDelegater.getRequestConfig(target);
			httpClientBuilder.setDefaultRequestConfig(requestConfig);
		}
		return httpClientBuilder.build();

	}

	/**
	 * HTTP连接池
	 */
	private static PoolingHttpClientConnectionManager phcm = null;

	/**
	 * 代理设置接口
	 */
	private static HttpProxySelector httpProxySelector = null;

	/**
	 * Request设置
	 */
	private static RequestConfigDelegater requestConfigDelegater = null;

}
