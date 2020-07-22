# HttpUtil
http请求的封装类，目前使用的HttpClient的实现，施工中
支持配置 连接池 代理 超时参数等，支持对以上参数对于不同的访问目标网站进行不同的设置
目前只封装了http的实现，后续会添加，所有的配置请查看 .properties文件，也可以自己实现相关接口即可。
参考代码请查看 src/test下的代码。

如果使用过程中有疑问或者发现bug，可以加QQ群1127385601

目前版本 0.1.1 release，后续会继续实现其他功能

连接池加代理的实现举例

<code>
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
            response = HttpUtil.doGet("http://www.apache.org");
            logger.info("1:" + response);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        long begin = System.currentTimeMillis();
        for(int i = 0 ; i < 5; ++i){
            // System.err.println(i);
            try {
                String responseI = HttpUtil.doGet("http://www.apache.org");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        logger.info("du " + (end - begin));
    }
}
</code>

相关文件也可以再仓储中寻找
jar包：
<dependency>
  <groupId>com.github.squallopen</groupId>
  <artifactId>HttpUtil</artifactId>
  <version>0.1</version>
</dependency>

源码:
<dependency>
  <groupId>com.github.squallopen</groupId>
  <artifactId>HttpUtil</artifactId>
  <version>0.1</version>
  <classifier>sources</classifier>
</dependency>

javadoc:
<dependency>
  <groupId>com.github.squallopen</groupId>
  <artifactId>HttpUtil</artifactId>
  <version>0.1</version>
  <classifier>javadoc</classifier>
</dependency>


