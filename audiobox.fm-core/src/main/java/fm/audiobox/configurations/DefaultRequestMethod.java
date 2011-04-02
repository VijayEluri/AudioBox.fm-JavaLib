package fm.audiobox.configurations;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

public class DefaultRequestMethod implements IConnectionMethod {

  private static final Logger log = Logger.getLogger(DefaultRequestMethod.class);
  
  private volatile transient HttpClient connector;
  private volatile transient HttpRequestBase method;
  private volatile transient IEntity destEntity;
  private volatile transient IConfiguration configuration;
  
  
  public DefaultRequestMethod(){
    super();
  }
  
  
  
  @Override
  public void init(IEntity destEntity, HttpRequestBase method, HttpClient connector, IConfiguration config) {
    this.connector = connector;
    this.method = method;
    this.destEntity = destEntity;
    this.configuration = config;
  }
  
  
  @Override
  public String[] send(boolean async) throws ServiceException, LoginException {
    return send(async, null,null);
  }

  @Override
  public String[] send(boolean async, List<NameValuePair> params) throws ServiceException, LoginException {
    HttpEntity entity = null;
    if (  (! isGET() && ! isDELETE() )  && params != null ){
      try {
        entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      } catch (UnsupportedEncodingException e) {
        log.error("An error occurred while instanciating UrlEncodedFormEntity", e);
      }
    }
    return send(async, entity);
  }

  @Override
  public String[] send(boolean async, HttpEntity params) throws ServiceException, LoginException {
    return send(async, params, null);
  }
  
  
  
  
  @Override
  public String[] send(boolean async, HttpEntity params, final IResponseHandler responseHandler) throws ServiceException, LoginException {
    if (   ( ! isGET() && ! isDELETE() )  && params != null ){
      ((HttpEntityEnclosingRequestBase) getHttpMethod() ).setEntity( params );
    }
    
    
    try {
      
      log.info("METHOD: " + getHttpMethod().getRequestLine().getUri() );
      
      return this.connector.execute( getHttpMethod(), new DefaultResponseHandler( this.configuration, this.destEntity, responseHandler), new BasicHttpContext() );
      
    } catch (ClientProtocolException e) {
      
      log.error("ClientProtocolException thrown while executing request method", e);
      throw new ServiceException(e);
      
    } catch (IOException e) {
      
      log.error("IOException thrown while executing request method", e);
      if ( e instanceof ServiceException) {
        
        ServiceException se = (ServiceException)e;
        if ( configuration.getDefaultServiceExceptionHandler() != null ){
          configuration.getDefaultServiceExceptionHandler().handle( se );
        }
        
        throw se;
      } else if ( e instanceof LoginException ) {
        
        LoginException le = (LoginException)e;
        if ( configuration.getDefaultLoginExceptionHandler() != null ){
          configuration.getDefaultLoginExceptionHandler().handle( le );
        }
        throw le;
        
      } else {
        ServiceException se = new ServiceException(e);
        
        if ( configuration.getDefaultServiceExceptionHandler() != null ){
          configuration.getDefaultServiceExceptionHandler().handle( se );
        }
        
        throw se;
      }
    }
  }

  @Override
  public void abort() {
    getHttpMethod().abort();
  }


  @Override
  public HttpRequestBase getHttpMethod() {
    return this.method;
  }
  
  @Override
  public IEntity getDestinationEntity() {
    return this.destEntity;
  }


  @Override
  public boolean isGET() {
    return getHttpMethod().getMethod().equals( HttpGet.METHOD_NAME );
  }

  @Override
  public boolean isPOST() {
    return getHttpMethod().getMethod().equals( HttpPost.METHOD_NAME );
  }

  @Override
  public boolean isPUT() {
    return getHttpMethod().getMethod().equals( HttpPut.METHOD_NAME );
  }

  @Override
  public boolean isDELETE() {
    return getHttpMethod().getMethod().equals( HttpDelete.METHOD_NAME );
  }

  @Override
  public void setFollowRedirect(boolean followRedirect) {
    getHttpMethod().getParams().setBooleanParameter( ClientPNames.HANDLE_REDIRECTS, true );
  }

}
