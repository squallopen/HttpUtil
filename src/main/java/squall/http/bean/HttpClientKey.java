package squall.http.bean;


import org.apache.http.HttpHost;

public class HttpClientKey {
	
	
	public HttpClientKey(HttpHost target, ClientAuthCert clientAuthCert) {
		this.target = target;
		this.clientAuthCert = clientAuthCert;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientAuthCert == null) ? 0 : clientAuthCert.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpClientKey other = (HttpClientKey) obj;
		if (clientAuthCert == null) {
			if (other.clientAuthCert != null)
				return false;
		} else if (!clientAuthCert.equals(other.clientAuthCert))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	
	



	@Override
	public String toString() {
		return "HttpClientKey [target=" + target + ", clientAuthCert=" + clientAuthCert + "]";
	}





	private HttpHost target;
	
	private ClientAuthCert clientAuthCert;
	

}
