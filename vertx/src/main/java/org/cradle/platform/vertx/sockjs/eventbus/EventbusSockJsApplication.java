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
package org.cradle.platform.vertx.sockjs.eventbus;

import org.cradle.platform.eventbus.EventBusService;
import org.cradle.platform.eventbus.spi.TextEventbusHandler;
import org.cradle.platform.vertx.sockjs.SockJsAgent;
import org.cradle.platform.vertx.sockjs.SockJsApplication;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 7, 2014
 */
public abstract class EventbusSockJsApplication extends SockJsApplication{

	private EventBusService eventBusService;
	private String address;

	
	public void setEventBusService(EventBusService eventBusService) {
		this.eventBusService = eventBusService;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.sockjs.BasicSockJsApplication#createAgent(org.vertx.java.core.sockjs.SockJSSocket)
	 */
	@Override
	protected SockJsAgent createAgent(){

		final EventbusSockJsAgent agent = createEventbusAgent();

		eventBusService.subscribe(address, new TextEventbusHandler() {
			
			@Override
			public void recieve(String message) {
				agent.eventBusMessageReceived(message);
			}
		});
		

		return agent;
	}

	/**
	 * @return
	 */
	protected abstract EventbusSockJsAgent createEventbusAgent();

}
