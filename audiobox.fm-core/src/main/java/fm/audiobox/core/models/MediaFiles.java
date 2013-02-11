/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Lucio Regina 							                           *
 *                                                                         *
 *   This program is free software: you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation, either version 3 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program. If not, see http://www.gnu.org/licenses/     *
 *                                                                         *
 ***************************************************************************/
package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;

/**
 * <p>MediaFiles is a {@link AbstractCollectionEntity<MediaFile>} specialization for {@link MediaFile} collections.</p>
 */
public class MediaFiles extends AbstractCollectionEntity<MediaFile> implements Serializable {

  private static final long serialVersionUID = 1L;
  private static Logger log = LoggerFactory.getLogger(MediaFiles.class);

  /** The XML tag name for the MediaFiles element */
  public static final String TAGNAME = "media_files";
  public static final String NAMESPACE = MediaFiles.TAGNAME;
  
  protected static final String TOKENS_PARAMETER =        "tokens[]";


  
  /**
   *  MediaFiles are grouped by types that are:
   * <ul>
   *   <li>{@link MediaFilesTypes#AudioFile AudioFile}</li>
   *   <li>{@link MediaFilesTypes#VideoFile VideoFile}</li>
   * </ul>
   */
  public enum Type {
    AudioFile,
    VideoFile
  }

  public enum Actions {
    hashes,
    multidestroy,
    remove
  }
  
  

  public MediaFiles(IConfiguration config) {
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

  @Override
  public Method getSetterMethod(String tagName) {

    if ( tagName.equals( MediaFile.TAGNAME ) ) {
      try {
        return this.getClass().getMethod("add", MediaFile.class);
      } catch (SecurityException e) {
        log.error("Security error", e);
      } catch (NoSuchMethodException e) {
        log.error("No method found", e);
      }
    }

    return null;
  }

  @Override
  public boolean add(MediaFile entity) {
    return super.addEntity(entity);
  }

  
  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    this.clear();
    IConnectionMethod request = getConnector(IConfiguration.Connectors.RAILS).get(this, this.getParent().getApiPath(), NAMESPACE, null);
    request.send(async, null, responseHandler);
    return request;
  }
  

  // DELETE /api/v1/playlists/:playlist_id/media_files/remove
  public boolean removeFromPlaylist(List<MediaFile> mediaFiles) throws ServiceException, LoginException {

    if ( mediaFiles == null || mediaFiles.size() == 0 )
      return false;

    String action = IConnector.URI_SEPARATOR.concat( Actions.remove.toString() );

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    for ( MediaFile m : mediaFiles ){
      params.add(  new BasicNameValuePair(TOKENS_PARAMETER, m.getToken() ) );
    }

    Response response = this.getConnector(IConfiguration.Connectors.RAILS).delete(this, action, params).send(false);

    return response.isOK();

  }
  
  
  
  public boolean destroy(List<MediaFile> mediaFiles) throws ServiceException, LoginException {

    if ( mediaFiles == null || mediaFiles.size() == 0 )
      return false;

    String path = IConnector.URI_SEPARATOR.concat( NAMESPACE );

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    for ( MediaFile m : mediaFiles ){
      params.add(  new BasicNameValuePair(TOKENS_PARAMETER, m.getToken() ) );
    }

    Response response = this.getConnector(IConfiguration.Connectors.RAILS).delete(this, path, Actions.multidestroy.toString(), params).send(false);

    boolean result = response.isOK();

    if ( result ) {
      for ( MediaFile m : mediaFiles ){
        this.remove( m.getToken() );
      }
    }

    return result;
  }

  public boolean destroyAll() throws ServiceException, LoginException {
    return this.destroy( this.subList(0, this.size() ) );
  }



  /**
   * Returns a list of {@link MediaFile} that matches the given {@link MediaFilesTypes}
   *
   * @param type the {@link MediaFilesTypes}
   * @return a list of {@link MediaFile} that matches with the given {@link MediaFilesTypes}
   */
  public List<MediaFile> getMediaFilesByType( Type type ){
    List<MediaFile> pls = new ArrayList<MediaFile>();
    for ( Iterator<MediaFile> it = this.iterator(); it.hasNext();  ){
      MediaFile mdf = it.next();
      if (  mdf.getType() == type  ) {
        pls.add(mdf);
      }
    }
    return pls;
  }


  @Override
  public String getSubTagName() {
    return MediaFile.TAGNAME;
  }


  @Override
  protected void copy(IEntity entity) {
  }

  @Override
  public String getApiPath() {
    return IConnector.URI_SEPARATOR + NAMESPACE;
  }
  
  protected List<NameValuePair> toQueryParameters(boolean all) {
    return new ArrayList<NameValuePair>();
  }
  
}
