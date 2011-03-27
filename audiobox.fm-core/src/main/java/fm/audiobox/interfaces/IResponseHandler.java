package fm.audiobox.interfaces;

import org.apache.http.HttpResponse;

import fm.audiobox.core.exceptions.ServiceException;

public interface IResponseHandler {

  
  /**
   * Used to parse a XML response
   * 
   * @param response the {@link HttpResponse} received
   * @return the body of response
   */
  public String parseAsXml(HttpResponse response) throws ServiceException;
  
  /**
   * Used to parse a json response
   * 
   * @param response the {@link HttpResponse} received
   * @return the body of response
   */
  public String parseAsJson(HttpResponse response) throws ServiceException;
  
  /**
   * Used to parse a text response
   * 
   * @param response the {@link HttpResponse} received
   * @return the body of response
   */
  public String parseAsText(HttpResponse response) throws ServiceException;
  
  /**
   * Used to parse a binary response
   * 
   * @param response the {@link HttpResponse} received
   * @return the body of response
   */
  public String parseAsBinary(HttpResponse response) throws ServiceException;
  
  
  /**
   * Used to parse an Undefined response
   * 
   * @param response the {@link HttpResponse} received
   * @return the body of response
   */  
  public String parse(HttpResponse response) throws ServiceException;
  
  
}
