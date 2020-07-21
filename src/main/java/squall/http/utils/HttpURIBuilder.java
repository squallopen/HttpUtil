package squall.http.utils;

import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author squall
 * @version 0.1.0
 * @since 0.1.0
 **/
public class HttpURIBuilder extends URIBuilder {

    public HttpURIBuilder() {
    }

    public HttpURIBuilder(String string) throws URISyntaxException {
        super(string);
    }

    public HttpURIBuilder(URI uri) {
        super(uri);
    }

    public HttpURIBuilder(String string, Charset charset) throws URISyntaxException {
        super(string, charset);
    }

    public HttpURIBuilder(URI uri, Charset charset) {
        super(uri, charset);
    }

    @Override
    public int getPort() {
        int port = super.getPort();
        if( port == -1){
            Integer intVal = SCHEME_DEFAULT_PORT.get(this.getScheme());
            if(intVal != null)
            {
                port = intVal.intValue();
            }
        }
        return port;
    }

    public HttpHost getHttpHost(){
        if(this.httpHost == null) {
            this.httpHost = new HttpHost(getHost(), getPort(), getScheme());
        }
        return httpHost;
    }


    private static ConcurrentMap<String , Integer> SCHEME_DEFAULT_PORT = new ConcurrentHashMap<String , Integer>(){ {
        put("http",80);
        put("https", 443);
    }};

    private HttpHost httpHost = null;

    //private HttpRoute HttpRoute = null;
}
