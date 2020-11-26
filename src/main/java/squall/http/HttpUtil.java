package squall.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import squall.http.bean.ClientAuthCert;
import squall.http.bean.HttpBean;
import squall.http.help.HttpHelper;
import squall.http.utils.HttpClientUtil;
import squall.http.utils.HttpURIBuilder;
import squall.http.utils.RequestConfigUtil;

/**
 * @author squall
 * @version 0.1.0
 * @since 0.1.0
 **/
public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	


	
	/**
	 * 请求指定的URL，并返回返回的字符串
	 * 
	 * @param url 访问的地址
	 * @param clientAuthCert 如果是客户端需要认证的SSL双向连接，则需要此项
	 * @return 返回的字符串
	 * @throws URISyntaxException url格式错误
	 */
	public static String doGet(String url, ClientAuthCert... clientAuthCert) throws URISyntaxException {
		return doGet(url, (List<NameValuePair>) null, clientAuthCert);
	}
	
	/**
	 * get请求带参数
	 *
	 * @param url    访问的URL
	 * @param params 参数
	 * @param clientAuthCert 如果是客户端需要认证的SSL双向连接，则需要此项
	 * @return 返回的页面
	 * @throws URISyntaxException url错误
	 */
	public static String doGet(String url, List<NameValuePair> params, ClientAuthCert... clientAuthCert) throws URISyntaxException {
		HttpURIBuilder uriBuilder = new HttpURIBuilder(url, StandardCharsets.UTF_8);
		if (params != null)
			uriBuilder.setParameters(params);
		URI uri = uriBuilder.build();
		HttpGet httpGet = new HttpGet(uri);
		RequestConfigUtil.setRequestConfigForRequest(httpGet, uriBuilder.getTargetHostURIStr());
		String returnStr = "";
		try (CloseableHttpClient client = HttpClientUtil.getHttpClient(uri,clientAuthCert);
				CloseableHttpResponse response = client.execute(httpGet)) {

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				returnStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			logger.error("调用失败 URL [" + url + "] 参数 [" + params + "]\n", e);
		}
		return returnStr;
	}
	
	/**
	 * get请求带参数
	 *
	 * @param url    访问的URL
	 * @param params 参数
	 * @param clientAuthCert 如果是客户端需要认证的SSL双向连接，则需要此项
	 * @return 返回的页面
	 * @throws URISyntaxException url错误
	 */
	public static String doGet(String url, Map<String,String> params,ClientAuthCert... clientAuthCert) throws URISyntaxException {
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		if (params != null && params.size() != 0)
		{
			for(Map.Entry<String, String> entry : params.entrySet()) {
				paramsList.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));	
			}
		}
		if(paramsList.size() == 0) {
			paramsList = null;
		}
		return doGet(url, paramsList,clientAuthCert);

	}



	/**
	 * 发送指定的json到指定的url
	 * @param url 发送的目标
	 * @param json json字符串
     * @param clientAuthCert 如果是客户端需要认证的SSL双向连接，则需要此项
	 * @return 返回的json字符串
	 * @throws URISyntaxException url格式错误
	 */
	public static String doPost(String url, String json,ClientAuthCert... clientAuthCert) throws URISyntaxException {
		HttpURIBuilder uriBuilder = new HttpURIBuilder(url, StandardCharsets.UTF_8);
		URI uri = uriBuilder.build();
		HttpPost httpPost = new HttpPost(uri);
		RequestConfigUtil.setRequestConfigForRequest(httpPost, uriBuilder.getTargetHostURIStr());
		StringEntity entityParam = new StringEntity(json, StandardCharsets.UTF_8);
		httpPost.setEntity(entityParam);
		String returnStr = "";
		try (CloseableHttpClient client = HttpClientUtil.getHttpClient(uri,clientAuthCert);
				CloseableHttpResponse response = client.execute(httpPost)) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				returnStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			logger.error("调用失败 URL [" + url + "] JSON [" + json + "]\n", e);
		}
		return returnStr;
	}
	
	/**
	 * 发送指定的json到指定的url
	 * @param url 发送的目标
	 * @param json json字符串
	 * @param headers 要设置的header头
	 * @param clientAuthCert 如果是客户端需要认证的SSL双向连接，则需要此项
	 * @return 返回的json字符串
	 * @throws URISyntaxException url格式错误
	 */
	public static String doPost(String url, String json, Map<String,String> headers,ClientAuthCert... clientAuthCert) throws URISyntaxException {
		HttpURIBuilder uriBuilder = new HttpURIBuilder(url, StandardCharsets.UTF_8);
		URI uri = uriBuilder.build();
		HttpPost httpPost = new HttpPost(uri);
		RequestConfigUtil.setRequestConfigForRequest(httpPost, uriBuilder.getTargetHostURIStr());
		HttpHelper.addHeader(httpPost, headers);
		StringEntity entityParam = new StringEntity(json, StandardCharsets.UTF_8);
		httpPost.setEntity(entityParam);
		String returnStr = "";
		try (CloseableHttpClient client = HttpClientUtil.getHttpClient(uri,clientAuthCert);
				CloseableHttpResponse response = client.execute(httpPost)) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				returnStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			logger.error("调用失败 URL [" + url + "] JSON [" + json + "]\n", e);
		}
		return returnStr;
	}

	/**
	 * 调用Post方法，使用指定的参数
	 * @param url 访问的地址
	 * @param params 参数
	 * @param clientAuthCert 如果是客户端需要认证的SSL双向连接，则需要此项
	 * @return 返回的字符串
	 * @throws UnsupportedEncodingException 报文字符集错误
	 * @throws URISyntaxException url格式错误
	 */
	public static String doPost(String url, List<NameValuePair> params,ClientAuthCert... clientAuthCert)
			throws UnsupportedEncodingException, URISyntaxException {
		HttpURIBuilder uriBuilder = new HttpURIBuilder(url, StandardCharsets.UTF_8);
		URI uri = uriBuilder.build();
		HttpPost httpPost = new HttpPost(uri);
		RequestConfigUtil.setRequestConfigForRequest(httpPost, uriBuilder.getTargetHostURIStr());
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		String returnStr = "";
		try (CloseableHttpClient client = HttpClientUtil.getHttpClient(uri,clientAuthCert);
				CloseableHttpResponse response = client.execute(httpPost)) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				returnStr = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			logger.error("调用失败 URL [" + url + "] 參數 [" + params + "]\n", e);
		}
		return returnStr;
	}
	
	public static HttpBean doPost(String url, HttpBean request, ClientAuthCert... clientAuthCert) throws Exception {
		HttpURIBuilder uriBuilder = new HttpURIBuilder(url, StandardCharsets.UTF_8);
		URI uri = uriBuilder.build();
		HttpPost httpPost = HttpHelper.getHttpPost(uri, request);
		RequestConfigUtil.setRequestConfigForRequest(httpPost, uriBuilder.getTargetHostURIStr());
		HttpBean responseBean = null;
		try (CloseableHttpClient client = HttpClientUtil.getHttpClient(uri,clientAuthCert);
				CloseableHttpResponse response = client.execute(httpPost)) {
			responseBean = HttpHelper.getHttpBean(response);
		} catch (Exception e) {
			logger.error("调用失败 URL [" + url + "] 參數 [" + request + "]\n", e);
		}
		return responseBean;
	}
}
