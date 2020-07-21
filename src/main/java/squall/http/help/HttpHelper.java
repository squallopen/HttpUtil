package squall.http.help;


import org.apache.http.HttpHost;

import org.apache.http.conn.routing.HttpRoute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squall.http.config.HttpProxySelector;
import squall.http.utils.HttpURIBuilder;


import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author squall
 * @version 0.1.0
 * @Description
 * @create 2020-07-16 21:10
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


    public static HttpRoute getHttpRoute(String url, HttpProxySelector proxySelector) {
        HttpHost target = getHttpHostByStr(url);
        return getHttpRoute(target, proxySelector);
    }

}
