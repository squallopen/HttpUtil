package squall.http;

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
import squall.http.utils.HttpConnectionUtil;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
     */
    public static String doGet(String url, List<NameValuePair> params) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url, StandardCharsets.UTF_8);
        if (params != null)
            uriBuilder.setParameters(params);
        String returnStr = "";
        URI uri =uriBuilder.build();
        HttpGet httpGet = new HttpGet(uri);
        try (CloseableHttpClient client = HttpConnectionUtil.getHttpClient(uri);
             CloseableHttpResponse response = client.execute(httpGet)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                returnStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.error("调用失败 URL [" + url + "] 参数 [" + params + "]\n", e);
        }
        return returnStr;
    }

    public static String doGet(String url) throws URISyntaxException {
        return doGet(url, null);
    }

    public String doPost(String url, String json) throws URISyntaxException {
        String returnStr = "";
        URI uri = new URIBuilder(url, StandardCharsets.UTF_8).build();
        HttpPost httpPost = new HttpPost(uri);
        StringEntity entityParam = new StringEntity(json, StandardCharsets.UTF_8);
        httpPost.setEntity(entityParam);
        try (CloseableHttpClient client = HttpConnectionUtil.getHttpClient(uri);
             CloseableHttpResponse response = client.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                returnStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.error("调用失败 URL [" + url + "] JSON [" + json + "]\n", e);
        }
        return returnStr;
    }


    public String doPost(String url, List<NameValuePair> params) throws UnsupportedEncodingException, URISyntaxException {
        String returnStr = "";
        URI uri = new URIBuilder(url, StandardCharsets.UTF_8).build();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        try (CloseableHttpClient client = HttpConnectionUtil.getHttpClient(uri);
             CloseableHttpResponse response = client.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                returnStr = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.error("调用失败 URL [" + url + "] 參數 [" + params + "]\n", e);
        }
        return returnStr;
    }


}
