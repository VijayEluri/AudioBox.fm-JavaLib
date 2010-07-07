
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

import fm.audiobox.core.api.ModelItem;


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
 * @version 0.0.1
 */
public class Genre extends ModelItem {
	
    /** The XML tag name for the Genre element */
	public static final String TAG_NAME = "genre";
	
	/**
	 * <p>Constructor for Genre.</p>
	 */
	protected Genre() {
		this.pEndPoint = Genres.END_POINT;
	}
	
}