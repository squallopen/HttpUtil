http请求的封装类，目前使用的HttpClient的实现，支持对不同的访问目标网站进行 连接池 代理 超时参数等个性化配置

支持http/https/https双向认证的实现，所有的配置请查看 .properties文件，也可以自己实现相关接口即可。 

参考代码请查看 src/test下的代码。

如果使用过程中有疑问或者发现bug，可以加[QQ群](<a target="_blank" href="https://qm.qq.com/cgi-bin/qm/qr?k=zv5wK5NqfxljEfVAAu9h3Cq_kRgveQeA&jump_from=webapi"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="HTTP客户端编程交流" title="HTTP客户端编程交流"></a>)

目前版本 0.2.3 release，后续会继续实现其他功能

连接池加代理的实现举例

```java
public class SimpleProxyTest {
    private static Logger logger = LoggerFactory.getLogger(SimpleProxyTest.class);

    public static void main(String args[]) throws URISyntaxException {
        HttpProxySelector httpProxySelector = new HttpProxySelectorByProperties();
        HttpClientUtil.initProxy(httpProxySelector);
        long begin = System.currentTimeMillis();
        for(int i = 0; i < 20 ; ++i) {
            String response = HttpUtil.doGet("http://www.apache.org");
        }
        long end = System.currentTimeMillis();
        logger.info("du " + (end - begin));
        //System.out.println(response);
    }
}

```

