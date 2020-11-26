import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import squall.http.HttpUtil;

/**
 * @author squall
 * @version 0.1.0
 * @since 0.1.0
 **/
public class SimpleTest {
	private static Logger logger = LoggerFactory.getLogger(SimpleTest.class);

	public static void main(String args[]) throws URISyntaxException, UnsupportedEncodingException {
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
		/*
		 * logger.info(HttpUtil.doPost("http://127.0.0.1:9999/testClient", "吃饭啊"));
		 * for(int i = 0; i < 100; ++i){ String I =
		 * HttpClientPoolUtil.doGet("http://www.apache.org",null); }
		 * System.out.println("end " + System.currentTimeMillis()); List<NameValuePair>
		 * list = new ArrayList<>();
		 * 
		 * BasicNameValuePair vp = new BasicNameValuePair("abc", "还在加班");
		 * 
		 * BasicNameValuePair vp2 = new BasicNameValuePair("你好", "后来的你好"); list.add(vp);
		 * list.add(vp2); HttpUtil.doPost("http://127.0.0.1:9999/testClient", list);
		 */

	}
}
