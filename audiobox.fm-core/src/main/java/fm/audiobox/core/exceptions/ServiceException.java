
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

package fm.audiobox.core.exceptions;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;



/**
 * ServiceException is thrown whenever a connection exception occurs while
 * performing requests to AudioBox.fm services.
 *
 * <p>
 *
 * Exception is specified through the error codes.
 *
 * @author Valerio Chiodino
 * @version 0.0.1
 */
public class ServiceException extends AudioBoxException {

    /** Socket exception error code */
    public static final int SOCKET_ERROR = 1;

    /** Client exception error code */
    public static final int CLIENT_ERROR = 2;

    /** Timeout exception error code */
    public static final int TIMEOUT_ERROR = 3;
    
    private static final long serialVersionUID = 1L;

    
    public ServiceException() {
      super();
    }

    public ServiceException(String message, Throwable cause) {
      super(message, cause);
    }
    
    public ServiceException(int errorCode, String message){
      super(message);
      this.errorCode = errorCode;
    }
    

    public ServiceException(String message) {
      super(message);
    }

    public ServiceException(Throwable cause) {
      super(cause);
      
      if ( cause instanceof ClientProtocolException ){
        this.errorCode = CLIENT_ERROR;
      } else if ( cause instanceof IOException ){
        this.errorCode = SOCKET_ERROR;
      }
      
    }
    
}
