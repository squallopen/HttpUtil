package squall.http.config.impl;

import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squall.http.config.RequestConfigDelegater;
import squall.http.utils.HttpURIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author squall
 * @version 0.1.0
 * @since 0.1.0
 **/
public class RequestConfigByProperties extends RequestConfigDelegater {

    private static Logger logger = LoggerFactory.getLogger(RequestConfigByProperties.class);
    private static ConcurrentHashMap<String, String> properties = null;
    static {
        properties = new ConcurrentHashMap<>();
        Properties proxyProperties = new Properties();
        try {
            proxyProperties.load(ClassLoader.getSystemResourceAsStream("request.properties"));
        } catch (IOException e) {
            logger.error("request.properties not found:", e);
        }
        Enumeration<?> fileName = proxyProperties.propertyNames();
        while (fileName.hasMoreElements()) {
            String strKey = (String) fileName.nextElement();
            String strValue = proxyProperties.getProperty(strKey);
            properties.put(strKey, strValue);
        }
    }


    /**
     * 构造方法
     */
    public RequestConfigByProperties() {
        logger.debug("request properties: {}" +properties);
        this.specialTimeoutConfig = new ConcurrentHashMap<>();
        Set<String> ketSet = properties.keySet();
        /*连接池获取连接等待时间毫秒
         */
        Integer connectionRequestTimeout = null;

        /*连接target等待时间毫秒
         */
        Integer connectTimeout = null;

        /*读取返回等待时间毫秒
         */
        Integer socketTimeout = null;

        boolean redirectsEnabled = true;
        for (String name : ketSet) {
            if (name.equals("connectionRequestTimeout")) {
                connectionRequestTimeout = Integer.valueOf(properties.get(name));
            } else if (name.equals("connectTimeout")) {
                connectTimeout = Integer.valueOf(properties.get(name));
            } else if (name.equals("socketTimeout")) {
                socketTimeout = Integer.valueOf(properties.get(name));
            } else if (name.equals("redirectsEnabled")) {
                int i = Integer.valueOf(properties.get(name));
                if (i == 0) {
                    redirectsEnabled = false;
                } else {
                    redirectsEnabled = true;
                }
            } else {
                /*特殊的*/
                try {
                    String target = new HttpURIBuilder(name).getTargetHostURIStr();
                    String requestStr = properties.get(name);
                    specialTimeoutConfig.put(target, genRequestConfigByStr(requestStr));
                } catch (URISyntaxException e) {
                    logger.error("specail " + name + " parse error",e);
                }

            }
        }
        /*构造默认*/
        RequestConfig.Builder builder = RequestConfig.custom();
        if (connectionRequestTimeout != null)
            builder.setConnectionRequestTimeout(connectionRequestTimeout);
        if (connectTimeout != null)
            builder.setConnectTimeout(connectTimeout);
        if (socketTimeout != null)
            builder.setSocketTimeout(socketTimeout);
        builder.setRedirectsEnabled(redirectsEnabled);
        defaultTimeoutConfig = builder.build();
    }

    @Override
    public RequestConfig getDefaultRequestConfig() {
        return defaultTimeoutConfig;
    }

    @Override
    public Map<String, RequestConfig> getSpecialRequestConfig() {
        return specialTimeoutConfig;
    }

    @Override
    public String toString() {
        return "TimeoutConfigByProperties{" +
                "defaultTimeoutConfig=" + defaultTimeoutConfig +
                ", specialTimeoutConfig=" + specialTimeoutConfig +
                '}';
    }

    private static RequestConfig genRequestConfigByStr(String str) {
        String[] strArr = str.split(":");
        Integer lConnectionRequestTimeout = null;
        Integer lConnectTimeout = null;
        Integer lSocketTimeout = null;
        int iRedirectsEnabled = 1;
        if (strArr[0] != null && !"".equals(strArr[0])) {
            lConnectionRequestTimeout = Integer.valueOf(strArr[0]);
        }
        if (strArr[1] != null && !"".equals(strArr[1])) {
            lConnectTimeout = Integer.valueOf(strArr[1]);
        }
        if (strArr[2] != null && !"".equals(strArr[2])) {
            lSocketTimeout = Integer.valueOf(strArr[2]);
        }
        if (strArr[3] != null && !"".equals(strArr[3])) {
            iRedirectsEnabled = Integer.valueOf(strArr[3]);
        }
        boolean lRedirectsEnabled = true;
        if (iRedirectsEnabled == 0) {
            lRedirectsEnabled = false;
        } else {
            lRedirectsEnabled = true;
        }
        RequestConfig.Builder builder = RequestConfig.custom();
        if (lConnectionRequestTimeout != null)
            builder.setConnectionRequestTimeout(lConnectionRequestTimeout);
        if (lConnectTimeout != null)
            builder.setConnectTimeout(lConnectTimeout);
        if (lSocketTimeout != null)
            builder.setSocketTimeout(lSocketTimeout);
        builder.setRedirectsEnabled(lRedirectsEnabled);
        return builder.build();
    }




    private RequestConfig defaultTimeoutConfig = null;

    private Map<String, RequestConfig> specialTimeoutConfig = null;



}
