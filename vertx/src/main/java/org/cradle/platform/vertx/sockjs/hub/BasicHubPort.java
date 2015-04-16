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


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 23, 2014
 */
public abstract class BasicHubPort  {

	protected HubSockJsAgent agent;
	
	protected String portName;

	public BasicHubPort(HubSockJsAgent agent, String portName) {
		
		this.agent = agent;
		
		this.portName = portName;
	}

	protected void portResponse(Object document){
		
		agent.documentPortResponse(portName, document);
	}
	
	protected void portResponse(byte[] buff) {
		
		agent.rawPortResponse(buff);
	}
}