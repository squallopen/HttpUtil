import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import squall.http.config.RequestConfigDelegater;
import squall.http.config.impl.PoolConfigByProperties;
import squall.http.config.impl.RequestConfigByProperties;

/**
 * @author squall
 * @version 0.1.0
 * @Description
 * @create 2020-07-20 19:28
 * @since 0.1.0
 **/
public class PropertiesTest {
    private static Logger logger = LoggerFactory.getLogger(PropertiesTest.class);
    public static void main(String[] args){
    	PoolConfigByProperties pool = new PoolConfigByProperties();
        logger.info("pool {}",pool);
        RequestConfigDelegater requestConfigDelegater = new RequestConfigByProperties();
        logger.info("timeoutConfig: " + requestConfigDelegater);

    }
}
