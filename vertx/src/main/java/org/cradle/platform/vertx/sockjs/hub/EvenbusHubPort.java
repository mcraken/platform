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
package org.cradle.platform.vertx.sockjs.hub;

import java.util.HashSet;
import java.util.Set;

import org.cradle.platform.vertx.eventbus.TextEventBusHandler;
import org.cradle.platform.vertx.eventbus.VertxEventBusService;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 22, 2014
 */
public class EvenbusHubPort extends BasicHubPort implements HubPort {
	
	private Set<String> subscriptions;
	private TextEventBusHandler eventBusHandler;
	
	public EvenbusHubPort(HubSockJsAgent agent, String portName,
			VertxEventBusService eventBusService) {
		
		super(agent, portName);
		
		subscriptions = new HashSet<String>();
		
		eventBusHandler = new TextEventBusHandler() {
			
			@Override
			public void recieve(String message) {
				portResponse(message.getBytes());
			}
		};
		
		eventBusHandler.setEventBusService(eventBusService);
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.sockjs.hub.HubPort#portRequest(org.vertx.java.core.json.JsonObject)
	 */
	@Override
	public void portRequest(String message) {
		
		JsonObject jsonObject = new JsonObject(message);
		
		if (jsonObject.containsField("subscribe")) {
			addSubscriptions(jsonObject);
		}

		if (jsonObject.containsField("unsubscribe")) {
			removeSubscriptions(jsonObject);
		}
	}
	
	private void addSubscriptions(JsonObject jsonObject){
		
		JsonArray jsonArray = jsonObject.getArray("subscribe");
		
		String subscriptionKey;
		
		for(int i=0; i<jsonArray.size() ; i++){
			
			subscriptionKey = jsonArray.get(i).toString();
			
			if(subscriptions.add(subscriptionKey))
				eventBusHandler.subscribe(subscriptionKey);
		}
	}
	
	private void removeSubscriptions(JsonObject jsonObject){
		
		JsonArray jsonArray = jsonObject.getArray("unsubscribe");
		
		String subscriptionKey;
		
		for(int i=0; i<jsonArray.size() ; i++){
			
			subscriptionKey = jsonArray.get(i).toString();
			
			if(subscriptions.remove(subscriptionKey))
				eventBusHandler.unsubscribe(subscriptionKey);
		}
	}
}
