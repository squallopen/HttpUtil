import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;



public class SimpleUpload {

	public static void main(String[] args) throws FileNotFoundException, IOException, DecoderException {
		String url = args[0]; //URL
		String fileName = args[1]; //文件路径
		String fileMD5B64 = Base64.encodeBase64String((Hex.decodeHex(DigestUtils.md5Hex(new FileInputStream(fileName)))));
    	HttpPut httpPut = new HttpPut(url);
    	FileEntity fileEntity = new FileEntity(new File(fileName));
    	String mimeType = ContentType.get(fileEntity).getMimeType();
    	httpPut.setEntity(fileEntity);
    	httpPut.addHeader("Content-MD5", fileMD5B64);
    	httpPut.addHeader("Content-Type", mimeType);
        try(CloseableHttpClient client= HttpClients.createDefault();
            CloseableHttpResponse reponse =client.execute(httpPut)){
        	System.out.println(reponse.getStatusLine());
        	System.out.println(reponse.getAllHeaders());
        }
	}

}
