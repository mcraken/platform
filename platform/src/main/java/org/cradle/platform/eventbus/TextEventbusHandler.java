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



/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 16, 2015
 */
public abstract class TextEventbusHandler {

	protected EventbusService eventBusService;
	
	/**
	 * @param eventBusService the eventBusService to set
	 */
	public void setEventBusService(EventbusService eventBusService) {
		this.eventBusService = eventBusService;
	}
	
	protected void publish(String address, String message) {
	
		eventBusService.publish(address, message);
	}

	public void subscribe(String address) {
		
		eventBusService.subscribe(address, this);
	}

	public void unsubscribe(String address) {
		
		eventBusService.unsubscribe(address, this);
	}

	public abstract void recieve(String message);

}