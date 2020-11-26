package squall.http.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import squall.http.bean.ClientAuthCert;
import squall.http.bean.HttpClientKey;
import squall.http.config.HttpProxySelector;
import squall.http.config.PoolConfig;
import squall.http.config.RequestConfigDelegater;
import squall.http.help.HttpHelper;

public class HttpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	// private static boolean usePool = false;

	/**
	 * 设置连接池参数
	 * 
	 * @param poolConfig 连接池配置
	 * @return null为了继续连着调用
	 */
	public static HttpClientUtil initPool(PoolConfig poolConfig) {
		if (poolConfig != null) {
			SSLContext sslContext = null;
			try {
				sslContext = SSLContexts.custom().setProtocol("TLSv1.2").loadTrustMaterial(null, new TrustStrategy() {
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;
					}
				}).build();
			} catch (KeyManagementException e) {
				logger.error("KeyManagementException:", e);
			} catch (NoSuchAlgorithmException e) {
				logger.error("NoSuchAlgorithmException:", e);
			} catch (KeyStoreException e) {
				logger.error("KeyStoreException:", e);
			}

			ConnectionSocketFactory plainsf = new PlainConnectionSocketFactory();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);

			Registry<ConnectionSocketFactory> connectionSocketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory>create().register("http", plainsf).register("https", sslsf).build();
			phcm = new PoolingHttpClientConnectionManager(connectionSocketFactoryRegistry);
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
			poolConfigHolder = poolConfig;
		}
		return null;
	}

	private static PoolingHttpClientConnectionManager genPoolManager(SSLContext sslContext) {
		PoolingHttpClientConnectionManager clientAuthSSLPoolManager = null;
		PoolConfig poolConfig = poolConfigHolder;
		if (poolConfig != null) {
			ConnectionSocketFactory plainsf = new PlainConnectionSocketFactory();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);

			Registry<ConnectionSocketFactory> connectionSocketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory>create().register("http", plainsf).register("https", sslsf).build();
			clientAuthSSLPoolManager = new PoolingHttpClientConnectionManager(connectionSocketFactoryRegistry);
			/* 没必要设置整个池子大小无法共用 */
			int maxConnectionNum = poolConfig.getDefaultMaxPerRoute();
			clientAuthSSLPoolManager.setDefaultMaxPerRoute(poolConfig.getDefaultMaxPerRoute());
			Map<String, Integer> specRoutes = poolConfig.getSpecHostsMax();
			if (specRoutes != null && specRoutes.size() != 0) {
				for (Map.Entry<String, Integer> entry : specRoutes.entrySet()) {
					String hostUri = entry.getKey();
					HttpRoute httpRoute = HttpHelper.getHttpRoute(hostUri, httpProxySelector);
					int specMax = entry.getValue();
					clientAuthSSLPoolManager.setMaxPerRoute(httpRoute, specMax);
					maxConnectionNum = (specMax > maxConnectionNum) ? specMax : maxConnectionNum;
				}
			}
			clientAuthSSLPoolManager.setMaxTotal(maxConnectionNum);
		}
		return clientAuthSSLPoolManager;
	}

	/**
	 * 
	 * @param httpProxySelector http代理选择器
	 * @return null为了继续连着调用
	 */
	public static HttpClientUtil initProxy(HttpProxySelector httpProxySelector) {
		HttpClientUtil.httpProxySelector = httpProxySelector;
		return null;
	}

	/*
	 * 获取http连接
	 * 
	 * @param uri target的URL
	 * 
	 * @return http连接
	 */
	/*
	 * public static CloseableHttpClient getHttpClient(URI uri) {
	 * CloseableHttpClient result = null; //if(poolConfigHolder != null &&
	 * poolConfigHolder.isShareConnection() == true) result =
	 * normalClientsHolder.get(uri); if (result == null) { HttpClientBuilder
	 * httpClientBuilder = HttpClients.custom();
	 * 
	 * 是否使用代理 HttpHost target = HttpHelper.getHttpHostByURI(uri); if
	 * (httpProxySelector != null) { HttpHost proxy =
	 * httpProxySelector.getProxy(target); if (proxy != null) {
	 * DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
	 * httpClientBuilder.setRoutePlanner(routePlanner); } }
	 * 
	 * 是否使用连接池 if (phcm != null) { httpClientBuilder.setConnectionManager(phcm); }
	 * //if(poolConfigHolder != null && poolConfigHolder.isShareConnection() ==
	 * true) httpClientBuilder.setConnectionManagerShared(true); 超时设置
	 * 效率改进，置换到构造HttpRequest后添加
	 * 
	 * if (requestConfigDelegater != null) { RequestConfig requestConfig =
	 * requestConfigDelegater.getRequestConfig(target);
	 * httpClientBuilder.setDefaultRequestConfig(requestConfig); }
	 * 
	 * 
	 * normalClientsHolder.put(uri, httpClientBuilder.build()); result =
	 * normalClientsHolder.get(uri); } return result;
	 * 
	 * }
	 */

	public static CloseableHttpClient getHttpClient(URI uri, ClientAuthCert... clientAuthCert) throws Exception {
		CloseableHttpClient result = null;
		if (clientAuthCert == null || clientAuthCert.length == 0) {
			result = normalClientsHolder.get(uri);
			if (result == null) {
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
					httpClientBuilder.setConnectionManager(phcm);
				}
				// if(poolConfigHolder != null && poolConfigHolder.isShareConnection() == true)
				httpClientBuilder.setConnectionManagerShared(true);
				/* 超时设置 效率改进，置换到构造HttpRequest后添加 */
				/*
				 * if (requestConfigDelegater != null) { RequestConfig requestConfig =
				 * requestConfigDelegater.getRequestConfig(target);
				 * httpClientBuilder.setDefaultRequestConfig(requestConfig); }
				 */

				normalClientsHolder.put(uri, httpClientBuilder.build());
				result = normalClientsHolder.get(uri);
			}
		} else {
			HttpHost target = HttpHelper.getHttpHostByURI(uri);
			HttpClientKey httpClientKey = new HttpClientKey(target, clientAuthCert[0]);

			result = clientAuthSSLClientsHolder.get(httpClientKey);
			// 尚未有该client
			if (result == null) {
				HttpClientBuilder httpClientBuilder = HttpClients.custom();
				if (httpProxySelector != null) {
					HttpHost proxy = httpProxySelector.getProxy(target);
					if (proxy != null) {
						DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
						httpClientBuilder.setRoutePlanner(routePlanner);
					}
				}

				KeyStore keyStore = KeyStore.getInstance(clientAuthCert[0].getCertType());
				try (InputStream in = new ByteArrayInputStream(clientAuthCert[0].getKeyStoreByteArr())) {
					keyStore.load(in, clientAuthCert[0].getPassArr());
				}
				SSLContext sslContext = SSLContexts.custom().setProtocol("TLSv1.2")
						.loadKeyMaterial(keyStore, clientAuthCert[0].getPassArr())
						.loadTrustMaterial(null, new TrustStrategy() {
							public boolean isTrusted(X509Certificate[] chain, String authType)
									throws CertificateException {
								return true;
							}
						}).build();
				/* 判断是否池化 */
				if (poolConfigHolder != null) {
					PoolingHttpClientConnectionManager clientAuthPhcm4ThisUri = genPoolManager(sslContext);
					httpClientBuilder.setConnectionManager(clientAuthPhcm4ThisUri);
				} else {
					httpClientBuilder.setSSLContext(sslContext);
				}
				httpClientBuilder.setConnectionManagerShared(true);
				CloseableHttpClient client4ThisUri = httpClientBuilder.build();
				// 未做二次判断，这种连接应该目前不会太多，性能优先
				clientAuthSSLClientsHolder.put(httpClientKey, client4ThisUri);
				result = clientAuthSSLClientsHolder.get(httpClientKey);
			}
		}
		return result;
	}

	public static void setRequestConfigDelegater(RequestConfigDelegater requestConfigDelegater) {
		RequestConfigUtil.setRequestConfigDelegater(requestConfigDelegater);
	}

	private static ConcurrentHashMap<URI, CloseableHttpClient> normalClientsHolder = new ConcurrentHashMap<URI, CloseableHttpClient>();

	private static ConcurrentHashMap<HttpClientKey, CloseableHttpClient> clientAuthSSLClientsHolder = new ConcurrentHashMap<HttpClientKey, CloseableHttpClient>();

	/**
	 * HTTP连接池
	 */
	private static PoolingHttpClientConnectionManager phcm = null;

	/**
	 * 代理设置接口
	 */
	private static HttpProxySelector httpProxySelector = null;

	private static PoolConfig poolConfigHolder = null;

	// private static CloseableHttpClient defaultHttpClient = null;

}
