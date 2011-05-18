
/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Valerio Chiodino - keytwo at keytwo dot net                         *
 *   - Fabio Tunno      - fat at fatshotty dot net                         *
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

import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;


/**
 * Genre class is one of the lighter models and offers defaults informations only.
 * 
 * <p>
 * 
 * Genre XML looks like this: 
 * 
 * <pre>
 * {@code
 * <genre>
 *   <name>Genre name</name>
 *   <token>sjKzpds9Tk</token>
 * </genre>
 * }
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public class Genre extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The XML tag name for the Genre element */
  public static final String NAMESPACE = Genres.TAGNAME;
  public static final String TAGNAME = "genre";

  private String name;
  private Tracks tracks;


  /**
   * <p>Constructor for Genre.</p>
   */
  public Genre(IConnector connector, IConfiguration config){
    super(connector, config);
  }

  
  @Override
  public String getTagName(){
    return TAGNAME;
  }


  @Override
  public String getNamespace() {
    return NAMESPACE;
  }

  /**
   * Sets the genre name. Used by parser
   * 
   * @param name the genre name
   */
  @Deprecated
  public void setName(String name){
    this.name = name;
  }

  /**
   * Returns the genre name
   * 
   * @return the genre name
   */
  public String getName() {
    return this.name;
  }
  
  
  
  /**
   * Returns a {@link Tracks} instance ready to be populated through {@link Tracks#load()} method
   * 
   * @return a {@link Tracks} instance
   */
  public Tracks getTracks(){
    if ( this.tracks == null ){
      this.tracks = (Tracks) getConfiguration().getFactory().getEntity( Tracks.TAGNAME , getConfiguration() );
      this.tracks.setParent( this );
    }
    return this.tracks;
  }
  


  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {

    if ( tagName.equals("token")  || tagName.equals("tk") ) {
      return this.getClass().getMethod("setToken", String.class);

    } else if ( tagName.equals("name") || tagName.equals("n") ){
      return this.getClass().getMethod("setName", String.class);

    }

    return null;
  }


  @Override
  protected void copy(IEntity entity) {
    
    Genre genre = (Genre) entity;
    
    this.name = genre.getName();
    
    this.setChanged();
    Event event = new Event( this, Event.States.ENTITY_REFRESHED );
    this.notifyObservers(event);
    
  }




}
