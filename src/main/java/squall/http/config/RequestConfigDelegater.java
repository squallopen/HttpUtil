package squall.http.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;

import java.net.URI;
import java.util.Map;

/**
 * @author squall
 * @version 0.1.0
 * @Description
 * @create 2020-07-18 11:20
 * @since 0.1.0
 **/
public abstract class RequestConfigDelegater {


    /**
     * 返回目标的RequestConfig
     * @return 目标的RequestConfig
     */
    public RequestConfig getRequestConfig(HttpHost target) {
        RequestConfig requestConfig = null;
        Map<HttpHost,RequestConfig> specicalTmeoutConfig  = getSpecialTimeoutConfig();
        if(specicalTmeoutConfig != null && specicalTmeoutConfig.size() != 0) {
            requestConfig = specicalTmeoutConfig.get(target);
        }
        if(requestConfig == null) {
            requestConfig = getDefaultTimeoutConfig();
        }
        return requestConfig;
    }

    public abstract RequestConfig getDefaultTimeoutConfig();

    public abstract Map<HttpHost,RequestConfig> getSpecialTimeoutConfig();

}
