import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import squall.http.HttpUtil;
import squall.http.config.HttpProxySelector;
import squall.http.config.impl.HttpProxySelectorByProperties;
import squall.http.config.impl.PoolConfigByProperties;
import squall.http.config.impl.RequestConfigByProperties;
import squall.http.utils.HttpConnectionUtil;

/**
 * @author squall
 * @version 0.1.0
 * @Description
 * @create 2020-07-19 19:31
 * @since 0.1.0
 **/
public class PoolProxyTest {
    private static Logger logger = LoggerFactory.getLogger(PoolProxyTest.class);
    public static void main(String args[]) {

        HttpConnectionUtil.initPool(new PoolConfigByProperties());
        HttpProxySelector httpProxySelector = new HttpProxySelectorByProperties();
        HttpConnectionUtil.setRequestConfigDelegater(new RequestConfigByProperties());
        HttpConnectionUtil.initProxy(httpProxySelector);
        String response = null;
        try {
            response = HttpUtil.doGet("http://www.apache.org",null);
            logger.info("1:" + response);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        long begin = System.currentTimeMillis();
        for(int i = 0 ; i < 5; ++i){
            // System.err.println(i);
            try {
                String responseI = HttpUtil.doGet("http://www.apache.org",null);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        logger.info("du " + (end - begin));
    }
}
