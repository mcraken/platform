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
package org.cradle.platform.eventbus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 16, 2015
 */
public abstract class JsonEventbusHandler<T> {

	protected EventbusService eventBusService;
	private Gson gson;
	private Class<T> messageType;
	/**
	 * 
	 */
	public JsonEventbusHandler(Class<T> messageType) {
		gson = new GsonBuilder().create();
		this.messageType = messageType;
	}
	
	/**
	 * @param eventBusService the eventBusService to set
	 */
	public void setEventBusService(EventbusService eventBusService) {
		this.eventBusService = eventBusService;
	}
	
	protected void subscribe(String address) {
		
		eventBusService.subscribe(address, this);
	}

	protected void unsubscribe(String address) {
		
		eventBusService.unsubscribe(address, this);
	}
	
	private T unmarshall(String message){
		
		return gson.fromJson(message, messageType);
	}
	
	public void recieve(String address, String message){
		recieve(address, unmarshall(message));
	}
	
	protected abstract void recieve(String address, T message);

}