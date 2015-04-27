/**
 *  Copyright mcplissken.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cradle.platform.eventbus.spi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 16, 2015
 */
public abstract class TypeEventbusHandler implements EventbusHandler{

	private Gson gson;
	private Class<?> messageType;
	/**
	 * 
	 */
	public TypeEventbusHandler(Class<?> messageType) {
		
		gson = new GsonBuilder().create();
		
		this.messageType = messageType;
	}
	
	private Object unmarshall(String message){
		
		return gson.fromJson(message, messageType);
	}
	
	@Override
	public void recieve(String message){
		
		recieve(unmarshall(message));
	}
	
	protected abstract <T>void recieve(T message);

}