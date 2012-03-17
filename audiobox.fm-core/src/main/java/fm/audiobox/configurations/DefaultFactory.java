package fm.audiobox.configurations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.core.models.ArtWork;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.User;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IFactory;

public class DefaultFactory implements IFactory {

  private static final Logger log = LoggerFactory.getLogger(DefaultFactory.class);
  
  private static final String[] EXCLUDED_EXTENDABLE_CLASSES = new String[]{ User.TAGNAME };
  
  /**
   * Default Entities map.
   * Used to store the default entity class
   */
  private static Map<String, Class<? extends IEntity>> gEntities;
  
  /**
   * Used to store the entity class associated with this {@link AudioBox} instance
   */
  private Map<String, Class<? extends IEntity>> entities;
  
  /**
   * {@link IConnector} associated with this {@link AudioBox} instance
   */
  private IConnector connector;
  
  static {
    gEntities = new HashMap<String, Class<? extends IEntity>>();
    gEntities.put( User.TAGNAME, User.class );
    
    gEntities.put( Playlists.TAGNAME, Playlists.class );
    gEntities.put( Playlist.TAGNAME, Playlist.class );
    gEntities.put( MediaFiles.TAGNAME, MediaFiles.class );
    
    
    gEntities.put( ArtWork.TAGNAME, ArtWork.class );
    
    gEntities.put( MediaFiles.TAGNAME, MediaFiles.class );
    gEntities.put( MediaFile.TAGNAME, MediaFile.class );
    
    gEntities.put( fm.audiobox.core.models.Error.TAGNAME, fm.audiobox.core.models.Error.class );
    
  }
  
  
  @SuppressWarnings("unchecked")
  public DefaultFactory(){
    entities = (Map<String, Class<? extends IEntity>>) 
          ((HashMap<String, Class<? extends IEntity>>) gEntities).clone();
  }
  
  
  @Override
  public boolean containsEntity(String tagName) {
    for (String k : this.entities.keySet()) {
      if (tagName.matches(k))
        return true;
    }
    return false;
  }
  
  public Class<? extends IEntity> getEntityByTag(String tagName) {
    for (String k : this.entities.keySet()) {
      if (tagName.matches(k))
        return this.entities.get(k);
    }
    return null;
  }
  
  
  @Override
  public IEntity getEntity(String tagName, IConfiguration configuration) {
    
    IEntity entity = null;
    
    if ( ! this.containsEntity(tagName) ) {
      log.warn("No IEntity found under key: " + tagName );
      return null;
    }
    
    Class<? extends IEntity> klass = getEntityByTag(tagName);
    
    try {
      Constructor<? extends IEntity> constructor = klass.getConstructor(IConnector.class, IConfiguration.class);
      entity = constructor.newInstance(this.connector, configuration);
    } catch (SecurityException e) {
      log.error("SecurityException while instantiating IEntity under key: " + tagName, e);
    } catch (NoSuchMethodException e) {
      log.error("NoSuchMethodException while instantiating IEntity under key: " + tagName, e);
    } catch (IllegalArgumentException e) {
      log.error("IllegalArgumentException while instantiating IEntity under key: " + tagName, e);
    } catch (InstantiationException e) {
      log.error("InstantiationException while instantiating IEntity under key: " + tagName, e);
    } catch (IllegalAccessException e) {
      log.error("IllegalAccessException while instantiating IEntity under key: " + tagName, e);
    } catch (InvocationTargetException e) {
      log.error("InvocationTargetException while instantiating IEntity under key: " + tagName, e);
    }
    
    return entity;
  }

  @Override
  public void setEntity(String tagName, Class<? extends IEntity> entity) {
    if ( Arrays.asList( EXCLUDED_EXTENDABLE_CLASSES ).contains( tagName ) ) {
      log.warn("Cannot use '" + tagName + "' as Entity. Use its default Entity instead");
      return;
    }
    entities.put( tagName, entity );
  }

  @Override
  public void setConnector(IConnector connector) {
    this.connector = connector;
  }

}
