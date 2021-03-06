package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

public class Args extends AbstractEntity implements Serializable{

  private static final long serialVersionUID = 1L;

  private static Logger log = LoggerFactory.getLogger(Args.class);

  public static final String TAGNAME = "args";
  public static final String NAMESPACE = Args.TAGNAME;


  public static final String FILENAME = "filename";
  public static final String RANGEMIN = "rangeMin";
  public static final String RANGEMAX = "rangeMax";
  public static final String ETAG = "etag";
  public static final String SERVER_IP = "server_ip";
  public static final String SERVER_PORT = "server_port";
  public static final String REMOTE_PATH = "remote_path";
  public static final String MESSAGE = "message";
  public static final String CRITICAL = "critical";
  public static final String DESTINATION_HOST = "destination_hash";

  private String filename;
  private String etag;
  private long rangeMin;
  private long rangeMax;
  private String serverIp;
  private String serverPort;
  private String remotePath;
  private String destinationHash;
  
  private String message;
  private boolean critical;



  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  private static Map<String, Method> getterMethods = null;
  
  static {
    try {
      setterMethods.put( FILENAME, Args.class.getMethod("setFileName", String.class) );
      setterMethods.put( RANGEMIN, Args.class.getMethod("setRangeMin", long.class) );
      setterMethods.put( RANGEMAX, Args.class.getMethod("setRangeMax", long.class) );
      setterMethods.put( ETAG, Args.class.getMethod("setEtag", String.class) );
      setterMethods.put( SERVER_IP, Args.class.getMethod("setServerIp", String.class) );
      setterMethods.put( SERVER_PORT, Args.class.getMethod("setServerPort", String.class) );
      setterMethods.put( REMOTE_PATH, Args.class.getMethod("setRemotePath", String.class) );
      setterMethods.put( MESSAGE, Args.class.getMethod("setMessage", String.class) );
      setterMethods.put( CRITICAL, Args.class.getMethod("setCritical", boolean.class) );
      setterMethods.put( DESTINATION_HOST, Args.class.getMethod("setDestinationHash", String.class) );
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }


  public Args(IConfiguration config) {
    super(config);
  }

  @Override
  public String getNamespace() {
    return NAMESPACE;
  }

  @Override
  public String getTagName() {
    return TAGNAME;
  }


  public String getFileName() {
    return filename;
  }

  public void setFileName(String filename) {
    this.filename = filename;
  }

  public String getServerIp() {
    return this.serverIp;
  }

  public void setServerIp(String ip) {
    this.serverIp = ip;
  }

  public String getServerPort() {
    return this.serverPort;
  }

  public void setServerPort(String port) {
    this.serverPort = port;
  }

  public long getRangeMin() {
    return rangeMin;
  }

  public void setRangeMin(long rangeMin) {
    this.rangeMin = rangeMin;
  }

  public long getRangeMax() {
    return rangeMax;
  }

  public void setRangeMax(long rangeMax) {
    this.rangeMax = rangeMax;
  }

  
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getRemotePath() {
    return remotePath;
  }

  public void setRemotePath(String remotePath) {
    this.remotePath = remotePath;
  }
  
  
  public boolean isCritical() {
    return critical;
  }

  public void setCritical(boolean critical) {
    this.critical = critical;
  }
  
  
  
  public String getDestinationHash() {
    return destinationHash;
  }

  public void setDestinationHash(String destinationHash) {
    this.destinationHash = destinationHash;
  }

  public Method getSetterMethod(String tagName) {
    if ( setterMethods.containsKey( tagName) ) {
      return setterMethods.get( tagName );
    }
    return null;
  }
  
  public Map<String, Method> getGetterMethods() {
    if ( getterMethods == null ) {
      getterMethods = new HashMap<String, Method>();
      try {  
        getterMethods.put( FILENAME, Args.class.getMethod("getFileName") );
        getterMethods.put( RANGEMIN, Args.class.getMethod("getRangeMin") );
        getterMethods.put( RANGEMAX, Args.class.getMethod("getRangeMax") );
        getterMethods.put( ETAG, Args.class.getMethod("getEtag") );
        getterMethods.put( SERVER_IP, Args.class.getMethod("getServerIp") );
        getterMethods.put( SERVER_PORT, Args.class.getMethod("getServerPort") );
        getterMethods.put( REMOTE_PATH, Args.class.getMethod("getRemotePath") );
        getterMethods.put( MESSAGE, Args.class.getMethod("getMessage") );
        getterMethods.put( CRITICAL, Args.class.getMethod("getCritical") );
        getterMethods.put( DESTINATION_HOST, Args.class.getMethod("getDestinationHash") );
      } catch (SecurityException e) {
        log.error("Security error", e);
      } catch (NoSuchMethodException e) {
        log.error("No method found", e);
      }
    }
    
    return getterMethods;
  }
  
  

  @Override
  public String getApiPath() {
    return null;
  }

  @Override
  public void setParent(IEntity parent) {
  }

  @Override
  protected void copy(IEntity entity) {
  }


  public String getEtag() {
    return etag;
  }

  public void setEtag(String etag) {
    this.etag = etag;
  }
  
  @Override
  public IConnectionMethod load(boolean sync) throws ServiceException, LoginException {
    return this.load(false, null);
  }

  @Override
  public IConnectionMethod load(boolean sync, IResponseHandler responseHandler) throws ServiceException, LoginException {
    throw new ServiceException("method not supported");
  }
  
  protected List<NameValuePair> toQueryParameters(boolean all) {
    String prefix = TAGNAME + "[";
    String suffix = "]";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    
    params.add(new BasicNameValuePair(prefix + FILENAME + suffix, this.filename )  );
    params.add(new BasicNameValuePair(prefix + RANGEMIN + suffix, String.valueOf( this.rangeMin ) )  );
    params.add(new BasicNameValuePair(prefix + RANGEMAX + suffix, String.valueOf( this.rangeMax ) )  );
    params.add(new BasicNameValuePair(prefix + ETAG + suffix, this.etag )  );
    params.add(new BasicNameValuePair(prefix + SERVER_IP + suffix, this.serverIp )  );
    params.add(new BasicNameValuePair(prefix + SERVER_PORT + suffix, this.serverPort )  );
    
    return params;
  }
        

}
