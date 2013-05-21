package fm.audiobox.core.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import fm.audiobox.core.models.AbstractCollectionEntity;
import fm.audiobox.core.models.AbstractEntity;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;


/**
 * This is an utility class used for parsing a JSON string.
 * <br />
 * It also populates the {@link IEntity} associated with this instance
 */
public class JParserStreaming {

  private static Logger log = LoggerFactory.getLogger(JParserStreaming.class);

  private IEntity entity;

  private IConfiguration config;

  private long startParse = 0;

  public JParserStreaming(IEntity entity){
    this.entity = entity;
    this.config = entity.getConfiguration();
  }

  
  /**
   * Parses given string and returns the associated entity
   * 
   * @param isr the InputStreamReader representing a JSON string
   * @return the {@link IEntity} associated with this instance
   */
  public IEntity parse ( InputStreamReader isr ){
    startParse = System.currentTimeMillis();
    
    try {
      JsonReader reader = new JsonReader( isr );
      reader.beginObject();
      String entityTag = reader.nextName();
      if ( entityTag.equals( this.entity.getTagName() ) ) {
        
        if ( this.entity instanceof AbstractCollectionEntity<?> ) {
          if ( reader.peek() == JsonToken.BEGIN_ARRAY ) {
            reader.beginArray();
            
            String subtagname = ((AbstractCollectionEntity<?>) this.entity).getSubTagName();
            while( reader.hasNext() ) {
              if ( ! setDynamicProperty(subtagname, this.entity, reader) ) {
                log.warn("Sub-entity has been skipped by the parser: " + subtagname);
                reader.skipValue();
              }
              
            }
            
          }
        } else if ( this.entity instanceof AbstractEntity ) {
          if ( reader.peek() == JsonToken.BEGIN_OBJECT ) {
            reader.beginObject();
            this.parse(this.entity, reader);
          }
        } else {
          log.error("No valid entity has been passed");
          reader.skipValue();
          reader.close();
          return null;
        }
        
        reader.close();
      }
    } catch (IOException e) {
      log.error("Error while parsing response", e);
    }

    if (log.isDebugEnabled()) {
      log.debug("Json parsed in " + (System.currentTimeMillis() - startParse) + "ms (" + this.entity.getNamespace() + ")");
    }
    return entity;
  }
  
  
  
  private IEntity parse(IEntity _entity, JsonReader reader) throws IOException {
    
    if ( reader.peek() == JsonToken.BEGIN_OBJECT ) {
      reader.beginObject();
    } else if ( reader.peek() == JsonToken.BEGIN_ARRAY ) {
      reader.beginArray();
    }
    
    while( reader.hasNext() ) {
      String prop = reader.nextName();
      try {
        if ( ! setDynamicProperty( prop, _entity, reader ) ){
          if ( reader.peek() != JsonToken.NULL ) {
            log.warn("Property has been skipped by the parser: " + prop);
          } else {
            log.debug("Property " + prop + " is null, so not set");
          }
          reader.skipValue();
        }
      } catch ( java.lang.IllegalStateException e ){
        log.error("Skipping value for " + _entity.getTagName() + "." + prop + " while parsing" );
        reader.skipValue();
      }
    }
      
    reader.endObject();
    return _entity;
  }
  
  
  @SuppressWarnings("deprecation")
  private boolean setDynamicProperty(String prop, IEntity dest, JsonReader reader) {
    JsonToken nextToken;
    Method method = null;
    try {
      nextToken = reader.peek();
      boolean isPrimitive = ( nextToken == JsonToken.BOOLEAN ) || ( nextToken == JsonToken.NUMBER ) || ( nextToken == JsonToken.STRING );
      
      try{
        method =  dest.getSetterMethod(prop);
      }catch (SecurityException e) {
        log.error("No accessible method found under key: " + prop, e);
        return false;
      }
      
      if ( method == null ) {
        log.warn("No setter method " + prop + " for entity: " + dest.getTagName() );
        return false;
      }
      
      
      if (method.getParameterTypes().length == 1) {
        Class<?> paramType = method.getParameterTypes()[0];
        
        if ( nextToken == JsonToken.NULL ) {
          method.invoke(dest, (Object) null);
          return false;
        }
        
        if ( isPrimitive ) {
          
          if ( paramType.equals(int.class) ) {
            try{
              method.invoke(dest, reader.nextInt() );
            }catch (NumberFormatException e) {
              method.invoke(dest, 0);
            }
            return true;
          } else if (paramType.equals(long.class) ) {
            try{
              method.invoke(dest, reader.nextLong() );
            }catch (NumberFormatException e) {
              method.invoke(dest, 0);
            }
            return true;
          } else if (paramType.equals(boolean.class) ) {
            method.invoke(dest, reader.nextBoolean() );
            return true;
            
          } else if (paramType.equals(String.class) ) {
            method.invoke(dest, reader.nextString() );
            return true;
          }
        
        } else {
          
          boolean isEntity = false;
          try {
            isEntity = paramType.asSubclass(IEntity.class) != null;
          } catch (ClassCastException e) {
            ; // silent fail
          }
          
          
          if ( isEntity && !isPrimitive ) {
            IEntity subentity = this.config.getFactory().getEntity(prop, this.config);
            if ( subentity != null ) {
              this.parse( subentity, reader);
            }
            
            method.invoke(dest, subentity);
            return true;
          }
          
        }
        
      }
    } catch (IOException e) {
      log.error("An error while reading from reader '" + method.getName() + "' for tag: " + prop, e);
    } catch (IllegalArgumentException e) {
      log.error("An error while invoking method '" + method.getName() + "' for tag: " + prop, e);
    } catch (IllegalAccessException e) {
      log.error("An error while invoking method '" + method.getName() + "' for tag: " + prop, e);
    } catch (InvocationTargetException e) {
      log.error("An error while invoking method '" + method.getName() + "' for tag: " + prop, e);
    }
    
    return false;
    
  }
  
}
