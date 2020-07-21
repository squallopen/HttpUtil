package squall.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import squall.http.help.HttpHelper;
import squall.http.utils.HttpConnectionUtil;

/**
 * @author squall
 * @version 0.1.0
 * @since 0.1.0
 **/
public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	/**
	 * get请求带参数
	 *
	 * @param url    访问的URL
	 * @param params 参数
	 * @return 返回的页面
	 * @throws URISyntaxException url错误
	 */
	public static String doGet(String url, List<NameValuePair> params) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url, StandardCharsets.UTF_8);
		if (params != null)
			uriBuilder.setParameters(params);
		String returnStr = "";
		URI uri = uriBuilder.build();
		HttpGet httpGet = new HttpGet(uri);
		try (CloseableHttpClient client = HttpConnectionUtil.getHttpClient(uri);
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
	 * 请求指定的URL，并返回返回的字符串
	 * 
	 * @param url 访问的地址
	 * @return 返回的字符串
	 * @throws URISyntaxException url格式错误
	 */
	public static String doGet(String url) throws URISyntaxException {
		return doGet(url, null);
	}

	/**
	 * 发送指定的json到指定的url
	 * @param url 发送的目标
	 * @param json json字符串
	 * @return 返回的json字符串
	 * @throws URISyntaxException url格式错误
	 */
	public static String doPost(String url, String json) throws URISyntaxException {
		String returnStr = "";
		URI uri = new URIBuilder(url, StandardCharsets.UTF_8).build();
		HttpPost httpPost = new HttpPost(uri);
		StringEntity entityParam = new StringEntity(json, StandardCharsets.UTF_8);
		httpPost.setEntity(entityParam);
		try (CloseableHttpClient client = HttpConnectionUtil.getHttpClient(uri);
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
	 * @return 返回的json字符串
	 * @throws URISyntaxException url格式错误
	 */
	public static String doPost(String url, String json, Map<String,String> headers) throws URISyntaxException {
		String returnStr = "";
		URI uri = new URIBuilder(url, StandardCharsets.UTF_8).build();
		HttpPost httpPost = new HttpPost(uri);
		HttpHelper.addHeader(httpPost, headers);
		StringEntity entityParam = new StringEntity(json, StandardCharsets.UTF_8);
		httpPost.setEntity(entityParam);
		try (CloseableHttpClient client = HttpConnectionUtil.getHttpClient(uri);
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
	 * @return 返回的字符串
	 * @throws UnsupportedEncodingException 报文字符集错误
	 * @throws URISyntaxException url格式错误
	 */
	public static String doPost(String url, List<NameValuePair> params)
			throws UnsupportedEncodingException, URISyntaxException {
		String returnStr = "";
		URI uri = new URIBuilder(url, StandardCharsets.UTF_8).build();
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		try (CloseableHttpClient client = HttpConnectionUtil.getHttpClient(uri);
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

}
