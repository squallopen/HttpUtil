package squall.http.utils;

import org.apache.http.client.methods.HttpRequestBase;

import squall.http.config.RequestConfigDelegater;

public class RequestConfigUtil {
	
	public static RequestConfigUtil setRequestConfigForRequest(HttpRequestBase request, String targetHostStr) {
		if(requestConfigDelegater == null)
			return null;
		request.setConfig(requestConfigDelegater.getRequestConfig(targetHostStr));
		
		return null;
	}
	
	public static void setRequestConfigDelegater(RequestConfigDelegater requestConfigDelegater) {
		RequestConfigUtil.requestConfigDelegater = requestConfigDelegater;
	}
	
	private static RequestConfigDelegater requestConfigDelegater= null;

}
