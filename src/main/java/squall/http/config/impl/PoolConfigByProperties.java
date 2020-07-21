package squall.http.config.impl;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squall.http.config.PoolConfig;
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
 * @Description
 * @create 2020-07-21 09:06
 * @since 0.1.0
 **/
public class PoolConfigByProperties implements PoolConfig {

    private static Logger logger = LoggerFactory.getLogger(PoolConfigByProperties.class);
    private static ConcurrentHashMap<String, String> properties = null;
    static {
        properties = new ConcurrentHashMap<>();
        Properties proxyProperties = new Properties();
        try {
            proxyProperties.load(ClassLoader.getSystemResourceAsStream("pool.properties"));
        } catch (IOException e) {
            logger.error("pool.properties not found:", e);
        }
        Enumeration fileName = proxyProperties.propertyNames();
        while (fileName.hasMoreElements()) {
            String strKey = (String) fileName.nextElement();
            String strValue = proxyProperties.getProperty(strKey);
            properties.put(strKey, strValue);
        }
    }

    public PoolConfigByProperties() {
        logger.debug("pool properties: " +properties);
        this.specialPoolConfig = new ConcurrentHashMap<>();
        Set<String> ketSet = properties.keySet();
        /*连接池获取连接等待时间毫秒
         */
        Integer lMaxTotal = null;

        /*连接target等待时间毫秒
         */
        Integer lDefaultMaxPerRoute = null;
        for (String name : ketSet) {
            if (name.equals("maxToal")) {
                lMaxTotal = Integer.valueOf(properties.get(name));
                if(lMaxTotal == null || lMaxTotal.intValue() <= 0)
                    maxTotal = DEFAULT_MAX_TOTAL;
                else
                    maxTotal = lMaxTotal.intValue();
            } else if (name.equals("connectTimeout")) {
                lDefaultMaxPerRoute = Integer.valueOf(properties.get(name));
                if(lDefaultMaxPerRoute == null || lDefaultMaxPerRoute.intValue() <= 0)
                    defaultMaxPerRoute = DEFAULT_MAX_PERTOUTE;
                else
                    defaultMaxPerRoute = lDefaultMaxPerRoute.intValue();
            }  else {
                /*特殊的*/
                    String specialMax = properties.get(name);
                    Integer lSpecialMax  = Integer.valueOf(specialMax);
                    if(lSpecialMax == null || lSpecialMax.intValue() <= 0) {
                        lSpecialMax = DEFAULT_MAX_PERTOUTE;
                    }
                    specialPoolConfig.put(name, lSpecialMax);

            }
        }
    }


    @Override
    public int getMaxTotal() {
        return maxTotal;
    }

    @Override
    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    @Override
    public Map<String, Integer> getSpecHostsMax() {
        return null;
    }


    private int maxTotal = 0;

    private int defaultMaxPerRoute = 0;

    private Map<String, Integer> specialPoolConfig = null;

    private static int DEFAULT_MAX_TOTAL = 400;
    private static int DEFAULT_MAX_PERTOUTE = 20;
}
