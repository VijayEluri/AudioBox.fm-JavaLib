package fm.audiobox.configurations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.AudioBoxException;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;

/**
 * This class is an abstract representation of a response content
 */
public class Response implements Serializable {

  private static Logger log = LoggerFactory.getLogger(Response.class);
  private static final long serialVersionUID = 1L;

  /**
   * The default byte array length used while parsing a stream
   */
  public static final int CHUNK = 1024 * 256;
  
  /**
   * Indentifies the type of the response
   */
  private ContentFormat format;
  
  /**
   * Indentifies the status code of the response
   */
  private int status;
  
  /**
   * Contains the {@link InputStream} which read data from
   */
  private InputStream stream;
  
  /**
   * Contains the {@link InputStream} represented as String
   */
  private String body;
  
  /**
   * Contains the {@link AudioBoxException} that is thrown.
   */
  private AudioBoxException exception;
  
  public Response(ContentFormat format, int status, InputStream stream){
    super();
    this.format = format;
    this.status = status;
    this.stream = stream;
  }
  
  public Response(ContentFormat format, int status, String body){
    this(format, status, new ByteArrayInputStream( body == null ? ("no response body").getBytes() : body.getBytes() ) );
  }

  
  /**
   * Returns the {@link ContentFormat} associated with this {@link Response}
   * @return the {@code ContentFormat}
   */
  public ContentFormat getFormat() {
    return format;
  }

  /**
   * Returns the {@code status} code associated with this {@link Response}
   * @return the status code
   */
  public int getStatus() {
    return status;
  }
  
  /**
   * Sets the response status
   */
  public void setStatus(int st) {
    this.status = st;
  }
  
  
  /**
   * Tests the response status code and returns {@code true} if it is 
   * 2xx and 3xx 
   * 
   * @return boolean
   */
  public boolean isOK() {
    return this.getStatus() >= HttpStatus.SC_OK && this.getStatus() < HttpStatus.SC_BAD_REQUEST;
  }
  
  /**
   * Returns the {@link InputStream} containing the body of the response 
   * @return the {@code InputStream} containing the body of the response
   */
  public InputStream getStream(){
    try {
      this.stream.reset();
    } catch (IOException e) {
    }
    return this.stream;
  }
  
  /**
   * Sets the exception that is thrown
   * @param exception the {@link AudioBoxException} that is thrown
   */
  public void setException(AudioBoxException exception){
    this.exception = exception;
  }
  
  /**
   * Returns the exception that is thrown
   * @return the {@link AudioBoxException exception} that is thrown
   */
  public AudioBoxException getException(){
    return this.exception;
  }
  
  
  /**
   * Returns the {@link InputStream} as string
   * <p>
   *  This method parses the InputStream associated with this Response once only
   * </p>
   * 
   * @return the {@code InputStream} as string
   */
  public String getBody(){
    
    if ( this.body != null ){
      return this.body;
    }
    try {
      this.body = streamToString( this.stream );
      return this.body;
    } catch (IOException e) {
      log.error("An error occurred while parsing the stream", e);
    }
    return null;
  }
  
  
  /**
   * Serializes the Response into an {@link OutputStream}
   * 
   * @param outStream the {@link OutputStream} which write objects to
   * @throws IOException the OutputStream default exception
   */
  public final void serialize(OutputStream outStream) throws IOException {
    ObjectOutputStream out = new ObjectOutputStream(outStream);
    out.writeInt( this.format.ordinal() );
    out.writeInt( this.status );
    out.writeObject( this.getBody() );
    out.close();
  }
  
  
  /**
   * Instantiates a new {@link Response} by reading a given {@link InputStream}
   * @param inStream the {@link InputStream} which read objects from
   * @return a new {@link Response}
   * @throws IOException the InputStream default exception
   */
  public static Response deserialize(InputStream inStream) throws IOException {
    ObjectInputStream in = new ObjectInputStream( inStream );
    ContentFormat format = ContentFormat.values()[ in.readInt() ];
    int status = in.readInt();
    InputStream stream = null;
    try {
      stream = new ByteArrayInputStream( ((String)in.readObject()).getBytes() );
    } catch (ClassNotFoundException e) {
      log.error("An error occurred while deserializing Response", e);
      return null;
    }
    in.close();
    
    return new Response( format, status, stream );
    
  }
  
  /**
   * Returns the String reading a given {@link InputStream}
   * <p>
   *  This method invokes the {@link #streamToString(InputStream, int)} passing default {@link #CHUNK}
   * </p>
   * 
   * @param stream the {@link InputStream} where read data from
   * @return the String representing the parsed {@code InputStream}
   * 
   * @throws IOException the InputStream default exception
   */
  public static String streamToString(InputStream stream) throws IOException{
    return streamToString(stream, CHUNK);
  }
  
  /**
   * Returns the String reading a given {@link InputStream}
   * 
   * @param stream the {@link InputStream} where read data from
   * @param chunk the {@code byte array} used as chunk
   * @return the String representing the parsed {@code InputStream}
   * @throws IOException the InputStream default exception
   */
  public static String streamToString(InputStream stream, int chunk) throws IOException{
    byte[] bytes = new byte[ chunk ];
    int read = -1;
    StringBuffer sb = new StringBuffer();
    while(  (read = stream.read(bytes) ) != -1 ){
      sb.append( new String(bytes,0,read) );
    }
    return sb.toString();
  }
  
}
