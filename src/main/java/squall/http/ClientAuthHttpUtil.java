package squall.http;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import squall.http.bean.ClientAuthCert;

public class ClientAuthHttpUtil {
	
	
	
	public ClientAuthHttpUtil(ClientAuthCert clientAuthCert) {
		this.clientAuthCert = clientAuthCert;
	}

	
	public String doGet(String url) throws URISyntaxException {
		return HttpUtil.doGet(url, this.clientAuthCert);
	}
	
	public String doGet(String url, List<NameValuePair> params) throws URISyntaxException{
		return HttpUtil.doGet(url, params, this.clientAuthCert);
	}
	
	public String doGet(String url, Map<String,String> params) throws URISyntaxException{
		return HttpUtil.doGet(url, params, this.clientAuthCert);
	}
	
	public  String doPost(String url, String json) throws URISyntaxException{
		return HttpUtil.doPost(url, json, this.clientAuthCert);
	}
	
	public String doPost(String url, String json, Map<String,String> headers) throws URISyntaxException{
		return HttpUtil.doPost(url, json, headers, this.clientAuthCert);
	}
	
	public String doPost(String url, List<NameValuePair> params) throws UnsupportedEncodingException, URISyntaxException {
		return HttpUtil.doPost(url, params, this.clientAuthCert);
	}
	
	private ClientAuthCert clientAuthCert;

}
