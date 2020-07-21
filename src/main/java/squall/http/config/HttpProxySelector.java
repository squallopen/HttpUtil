package squall.http.config;

import org.apache.http.HttpHost;


import java.util.Map;
import java.util.Set;


/**
 * 代理的抽象描述虚类，子类需要将defaultProxy,proxyListMap,ignoreProxyHosts注入
 * 还是为了和spring解耦
 * @author squall
 * @version 0.1.0
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

    /**
     * 返回默认的代理
     * @return 默认代理的HttpHost描述
     */
    protected abstract HttpHost getDefaultProxy();

    /**
     * 返回特定的目标的代理
     * @return 用特殊代理的target HttpHost集合
     */
    protected abstract Map<HttpHost, HttpHost> getSpecialProxyMap();

    /**
     * 不使用代理的traget
     * @return 不使用代理的target HttpHost集合
     */
    protected abstract Set<HttpHost> getIgnoreHosts();

}
