package squall.http.config;

import org.apache.http.HttpHost;


import java.util.Map;
import java.util.Set;


/**
 * 代理的抽象描述虚类，子类需要将defaultProxy,proxyListMap,ignoreProxyHosts注入
 * 还是为了和spring解耦
 * @author squall
 * @version 0.1.0
 * @Description
 * @create 2020-07-16 20:21
 * @since 0.1.0
 **/
public abstract class HttpProxySelector {




    /**
     * 获取指定的HttpHost(traget)获取使用的Proxy
     * 如果是已经在忽略列表中返回null
     * 如果使用特殊代理返回特殊代理
     * 其余返回默认代理
     * @param target 需要访问的目标
     * @return 代理的HttpHost对象
     */
    public HttpHost getProxy(HttpHost target) {
        Set<HttpHost> ignoreProxyHosts= getIgnoreHosts();
        Map<HttpHost, HttpHost> proxyListMap = getSpecialProxyMap();
        if(ignoreProxyHosts!= null && ignoreProxyHosts.contains(target)) {
            return null;
        }
        HttpHost proxy =  null;
        if(proxyListMap != null && proxyListMap.size() != 0) {
            proxy = proxyListMap.get(target);
        }
        if(proxy == null) {
            proxy = getDefaultProxy();
        }
        return proxy;
    }

    protected abstract HttpHost getDefaultProxy();

    protected abstract Map<HttpHost, HttpHost> getSpecialProxyMap();

    protected abstract Set<HttpHost> getIgnoreHosts();

}
