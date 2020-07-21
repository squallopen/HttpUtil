package squall.http.config.impl;

import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squall.http.config.HttpProxySelector;
import squall.http.utils.HttpURIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author squall
 * @version 0.1.0
 * @Description
 * @create 2020-07-20 19:21
 * @since 0.1.0
 **/
public class HttpProxySelectorByProperties extends HttpProxySelector {

    private static Logger logger = LoggerFactory.getLogger(HttpProxySelectorByProperties.class);

    private static ConcurrentHashMap<String, String> properties = null;


    static {
        properties = new ConcurrentHashMap<>();
        Properties proxyProperties = new Properties();
        try {
            proxyProperties.load(ClassLoader.getSystemResourceAsStream("proxy.properties"));
        } catch (IOException e) {
            logger.error("proxy.properties not found",e);
        }
        Enumeration fileName = proxyProperties.propertyNames();
        while (fileName.hasMoreElements()) {
            String strKey = (String) fileName.nextElement();
            String strValue = proxyProperties.getProperty(strKey);
            properties.put(strKey, strValue);
        }
    }


    public HttpProxySelectorByProperties() {
        logger.debug("proxy properties:",properties);
        this.specialProxyMap = new ConcurrentHashMap<>();
        this.ignoreHosts = ConcurrentHashMap.newKeySet();
        Set<String> ketSet = properties.keySet();
        for (String name : ketSet) {
            if (name.equals("defaultProxy")) {
                try {
                    this.defaultProxy = new HttpURIBuilder(properties.get(name)).getHttpHost();
                } catch (URISyntaxException e) {
                    logger.error("defaultProxy parse error:",e);
                }
            } else if (name.equals("ignoreHosts")) {
                String[] ignores = properties.get(name).split(";");
                for (String s : ignores) {
                    try {
                        ignoreHosts.add(new HttpURIBuilder(s).getHttpHost());
                    } catch (URISyntaxException e) {
                        logger.error("ignoreHosts parse error:",e);
                    }
                }
            } else {
                try {
                    HttpHost target = new HttpURIBuilder(name).getHttpHost();
                    HttpHost proxy = new HttpURIBuilder(properties.get(name)).getHttpHost();
                    specialProxyMap.put(target, proxy);
                } catch (URISyntaxException e) {
                    logger.error("specail " + name + " parse error",e);
                }
            }

        }
    }

    @Override
    protected HttpHost getDefaultProxy() {
        return defaultProxy;
    }

    @Override
    protected Map<HttpHost, HttpHost> getSpecialProxyMap() {
        return specialProxyMap;
    }

    @Override
    protected Set<HttpHost> getIgnoreHosts() {
        return ignoreHosts;
    }

    @Override
    public String toString() {
        return "HttpProxySelectorByProperties{" +
                "defaultProxy=" + defaultProxy +
                ", specialProxyMap=" + specialProxyMap +
                ", ignoreHosts=" + ignoreHosts +
                '}';
    }

    /**
     * 默认的代理，如果为null则默认不使用代理
     */
    protected HttpHost defaultProxy = null;

    /**
     * 维护一个target对应的proxy的map,K是target,V是proxy
     * 注意：如果后续可能实时变更实现请用ConcurrentMap
     */
    protected Map<HttpHost, HttpHost> specialProxyMap = null;

    /**
     * 不使用代理的HttpHost target集合，
     * 注意：如果后续可能实时变更请使用ConcurrentHashMap.newKeySey()
     */
    protected Set<HttpHost> ignoreHosts = null;
}
