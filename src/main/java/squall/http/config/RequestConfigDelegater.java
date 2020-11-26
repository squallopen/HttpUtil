package squall.http.config;

import org.apache.http.client.config.RequestConfig;

import java.util.Map;

/**
 * Request的配置类
 * @author squall
 * @version 0.1.0
 * @since 0.1.0
 **/
public abstract class RequestConfigDelegater {


    /**
     * 返回目标的RequestConfig
     * @param target 目标的HttpHost的uri
     * @return 目标的RequestConfig
     */
    public RequestConfig getRequestConfig(String target) {
        RequestConfig requestConfig = null;
        Map<String,RequestConfig> specicalTmeoutConfig  = getSpecialRequestConfig();
        if(specicalTmeoutConfig != null && specicalTmeoutConfig.size() != 0) {
            requestConfig = specicalTmeoutConfig.get(target);
        }
        if(requestConfig == null) {
            requestConfig = getDefaultRequestConfig();
        }
        return requestConfig;
    }

    /**
     * 获取默认的
     * @return 默认的
     */
    public abstract RequestConfig getDefaultRequestConfig();

    public abstract Map<String,RequestConfig> getSpecialRequestConfig();

}
