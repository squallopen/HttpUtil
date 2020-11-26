
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import squall.http.HttpUtil;
import squall.http.config.PoolConfig;
import squall.http.utils.HttpClientUtil;


/**
 * @author squall
 * @version 0.1.0
 * @Description
 * @create 2020-07-19 19:31
 * @since 0.1.0
 **/
public class PoolTest {
    private static Logger logger = LoggerFactory.getLogger(PoolTest.class);
    public static void main(String args[]) throws URISyntaxException {
    	HttpClientUtil.initPool(new PoolConfig() {
            @Override
            public int getMaxTotal() {
                return 10;
            }

            @Override
            public int getDefaultMaxPerRoute() {
                return 2;
            }

            @Override
            public Map<String, Integer> getSpecHostsMax() {
                return new ConcurrentHashMap<>();
            }
        });
        /*String response = null;
        try {
            response = HttpUtil.doGet("http://www.apache.org");
            logger.info("1:" + response);
        } catch (URISyntaxException e) {
            logger.error("",e);
        }
        long begin = System.currentTimeMillis();
        for(int i = 0 ; i < 20; ++i){
            // System.err.println(i);
            String responseI = null;
                responseI = HttpUtil.doGet("http://www.apache.org");

        }
        long end = System.currentTimeMillis();
        logger.info("du " + (end - begin));*/
		List<NameValuePair> var = new ArrayList<>();
		var.add(new BasicNameValuePair("a", "123"));
		var.add(new BasicNameValuePair("a", "345"));
		var.add(new BasicNameValuePair("b", "你好"));
		long begin = System.currentTimeMillis();
		String response = null;
		for (int i = 0; i < 1; ++i) {
			response = HttpUtil.doGet("http://www.apache.org");
		}
		long end = System.currentTimeMillis();
		logger.info("du " + (end - begin));
		logger.info(response);
		response = HttpUtil.doGet("https://www.baidu.com");
		
		logger.info("\n\n\n\n{}",response);
		
response = HttpUtil.doGet("https://www.baidu.com");
		
		logger.info("\n\n\n\n{}",response);
    }
}
