package squall.http.config;


import java.util.Map;

/**
 * 描述pool参数的接口，为了和spring的读取value解耦不引入spring的依赖
 * @author squall
 * @version 0.1.0
 * @since 0.1.0
 **/
public interface PoolConfig {
    /**
     * 获取连接池的总的最大连接数s
     *
     * @return 连接池的总的最大连接数s
     */
    int getMaxTotal();

    /**
     * 获取默认每个Route的最大连接数，注这里的Route指的为IP或域名+端口
     *
     * @return 单个Route的最大连接数
     */
    int getDefaultMaxPerRoute();

    /**
     * 获取需要特定声明的target和其最大连接数
     *
     * @return 特定的路由机器最大连接数 返回的String为符合URI的格式如 http://www.apache.org https://www.baidu.com:1443
     */
    Map<String, Integer> getSpecHostsMax();
    
    /*
     * 是否复用连接，如果复用连接的话，会尽量复用连接，但是可能会存在大量连接
     * 如果只是访问特定的几个网站建议打开，如果使用程序会访问大量的网站建议关闭
     * @return true-保持连接复用 false-每次使用完后关闭连接
     */
	/* boolean isShareConnection(); */


}
