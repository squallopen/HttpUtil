package squall.http.bean;

import java.util.Arrays;

public class ClientAuthCert {
	

	public ClientAuthCert(byte[] keyStoreByteArr, char[] passArr, String certType) {
		this.keyStoreByteArr = keyStoreByteArr;
		this.passArr = passArr;
		this.certType = certType;
	}

	

	public byte[] getKeyStoreByteArr() {
		return keyStoreByteArr;
	}



	public void setKeyStoreByteArr(byte[] keyStoreByteArr) {
		this.keyStoreByteArr = keyStoreByteArr;
	}



	public char[] getPassArr() {
		return passArr;
	}



	public void setPassArr(char[] passArr) {
		this.passArr = passArr;
	}



	public String getCertType() {
		return certType;
	}



	public void setCertType(String certType) {
		this.certType = certType;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((certType == null) ? 0 : certType.hashCode());
		result = prime * result + Arrays.hashCode(keyStoreByteArr);
		result = prime * result + Arrays.hashCode(passArr);
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
		ClientAuthCert other = (ClientAuthCert) obj;
		if (certType == null) {
			if (other.certType != null)
				return false;
		} else if (!certType.equals(other.certType))
			return false;
		if (!Arrays.equals(keyStoreByteArr, other.keyStoreByteArr))
			return false;
		if (!Arrays.equals(passArr, other.passArr))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientAuthCert [keyStoreByteArr=" + Arrays.toString(keyStoreByteArr) + ", passArr="
				+ Arrays.toString(passArr) + ", certType=" + certType + "]";
	}
	
	/**
	 * 证书加载到byte[]的结果
	 */
	private byte[] keyStoreByteArr = null;
	
	/**
	 * 证书的秘码
	 */
	private char[] passArr = null;
	
	/**
	 * 证书的类型 JKS或者PKCS12
	 */
	private String certType = null;

	
}
