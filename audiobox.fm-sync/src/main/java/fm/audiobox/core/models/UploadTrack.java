package fm.audiobox.core.models;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.content.FileBody;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient.AudioBoxConnector;

public class UploadTrack extends Track {

	HttpRequestBase method;
	
    public UploadTrack() {
        super();
    }

    public void setFileBody( FileBody fb ){
        this.fileBody = fb;
    }


    public void upload() throws IOException, ServiceException, LoginException {
    	method = this.pConnector.createConnectionMethod(Tracks.END_POINT , null, null, this, HttpPost.METHOD_NAME);
    	String result[] = this.pConnector.request(method, this, false);
    	this.setToken( result[ AudioBoxConnector.RESPONSE_BODY ]  );
    }

	@Override
	public String parseBinaryResponse(HttpResponse response) throws IOException {
		if ( this.fileOutputStream == null )
			return super.parsePlainResponse( response.getEntity().getContent() );
		return super.parseBinaryResponse(response);
	}
    
    public HttpRequestBase getConnectionMethod(){
    	return this.method;
    }

}
