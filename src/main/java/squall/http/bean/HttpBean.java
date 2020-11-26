package squall.http.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class HttpBean implements Serializable {

	private static final long serialVersionUID = 2696622975793568929L;
	
	
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public String getJsonBody() {
		return jsonBody;
	}

	public void setJsonBody(String jsonBody) {
		this.jsonBody = jsonBody;
	}

	public Map<String, List<String>> getParameterBody() {
		return parameterBody;
	}

	public void setParameterBody(Map<String, List<String>> parameterBody) {
		this.parameterBody = parameterBody;
	}



	private Map<String,List<String>> headers;
	
	private String jsonBody;
	
	private Map<String, List<String>> parameterBody;


	@Override
	public String toString() {
		return "HttpBean [headers=" + headers + ", jsonBody=" + jsonBody + ", parameterBody=" + parameterBody + "]";
	}



	
	

}
