package squall.http.help;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import squall.http.bean.HttpBean;
import squall.http.config.HttpProxySelector;
import squall.http.utils.HttpURIBuilder;


/**
 * @author squall
 * @version 0.1.0
 * @since 0.1.0
 **/
public class HttpHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    /**
     * 从url获取目标的HttpHost
     *
     * @param url 目标url
     * @return 对应的HttpHost，如果url有问题则返回null
     */
    public static HttpHost getHttpHostByStr(String url) {
        HttpHost target = null;
        if (url != null && url.length() != 0) {
            try {
                HttpURIBuilder httpURIBuilder = new HttpURIBuilder(url);
                target = httpURIBuilder.getHttpHost();
            } catch (URISyntaxException e) {
                logger.error("URL + [" + url + "] parse HttpHost Error", e);
            }
        }

        return target;
    }

    /**
     * 从url获取目标的HttpHost
     *
     * @param uri 目标uri
     * @return 对应的HttpHost，如果url有问题则返回null
     */
    public static HttpHost getHttpHostByURI(URI uri) {
        HttpHost target = null;
        if (uri != null) {
            HttpURIBuilder httpURIBuilder = new HttpURIBuilder(uri);
            target = httpURIBuilder.getHttpHost();
        }

        return target;
    }

    /**
     * 根据target和代理选择器返回实际的HttpRoute
     *
     * @param target        目标Host
     * @param proxySelector 代理选择器
     * @return 如果不走代理则HttpRoute实际为HttpHost，如果走代理则返回包含代理信息的HttpRoute
     */
    public static HttpRoute getHttpRoute(HttpHost target, HttpProxySelector proxySelector) {
        HttpRoute httpRoute = null;
        HttpHost proxy = proxySelector.getProxy(target);
        if (proxy != null)
            httpRoute = new HttpRoute(target, proxy);
        else {
            httpRoute = new HttpRoute(target);
        }
        return httpRoute;

    }
    
    /**
     * 加Header用的
     * @param request 请求
     * @param params 添加参数
     */
    public static void addHeader(HttpRequest request, Map<String,String> params) {
    	if(request != null && params != null && params.size() != 0)
    	for(Map.Entry<String, String> entry : params.entrySet()) {
    		request.addHeader(entry.getKey(),entry.getValue());
    	}
    	
    }


    /**
     * 根据url和代理选择器构建HttpRoute
     * @param url 目标url
     * @param proxySelector 代理选择器
     * @return 目标的HttpRoute
     */
    public static HttpRoute getHttpRoute(String url, HttpProxySelector proxySelector) {
        HttpHost target = getHttpHostByStr(url);
        return getHttpRoute(target, proxySelector);
    }
    
    /**
     * 转换为Post对象
     * @param uri 访问的URI的描述
     * @param bean 请求的参数
     * @return HttpPost对象
     * @throws UnsupportedEncodingException 
     */
    public static HttpPost getHttpPost(URI uri,HttpBean bean) throws UnsupportedEncodingException {
    	HttpPost post = new HttpPost(uri);
    	Map<String,List<String>> headers = bean.getHeaders();
    	if( headers != null && headers.size() != 0) {
    		for(Map.Entry<String, List<String>> entry : headers.entrySet()) {
    			for(String value:entry.getValue())
    			{
    			    post.addHeader(entry.getKey(),value);
    			}
    		}
    	}
    	String jsonBody = bean.getJsonBody();
    	Map<String,List<String>> params = bean.getParameterBody();
    	if(jsonBody != null && !"".equals(jsonBody)) {
	    	StringEntity entityParam = new StringEntity(bean.getJsonBody(), StandardCharsets.UTF_8);
	    	post.setEntity(entityParam);
    	}else if(params != null && params.size()!=0) {
    		
    		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
    		for(Map.Entry<String, List<String>> entry : params.entrySet()) {
    			for(String value:entry.getValue())
    			{
    				paramsList.add(new BasicNameValuePair(entry.getKey(),value));
    			}
    		}
    		post.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));
    	}
    	return post;
    	
    }
    
    public static HttpBean getHttpBean(HttpResponse response) throws ParseException, IOException {
    	HttpBean bean = new HttpBean();
    	Header[] heads =  response.getAllHeaders();
    	if(heads != null && heads.length != 0) {
    		Map<String,List<String>> headMap = new HashMap<String,List<String>>();
	    	for(Header head : heads) {
	    		if(headMap.containsKey(head.getName()))
	    		{
	    			headMap.get(head.getName()).add(head.getValue());
	    		}else {
		    		List<String> list = new ArrayList<String>();
		    		list.add(head.getValue());
		    		headMap.put(head.getName(),list);
	    		}
	    	}
	    	bean.setHeaders(headMap);
    	}
    	HttpEntity entity = response.getEntity();
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			bean.setJsonBody(EntityUtils.toString(entity, "UTF-8"));
		}else {
			bean.setJsonBody("");
		}
		EntityUtils.consume(entity);
		return bean;
    	
    }
}
