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
package org.cradle.gateway.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Method;

import org.cradle.gateway.BasicHttpHandler;
import org.cradle.gateway.HttpGateway;
import org.cradle.gateway.restful.filter.RESTfullServiceFilterConfig;
/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 15, 2015
 */
public abstract class BasicHttpGateway implements HttpGateway {

	private RegistrationPrincipal principalChain;
	private RegistrationAgent registrationAgent;
	
	public BasicHttpGateway() {
		
		principalChain = new OutputHttpHandlerResgistrationPrincipal(
				new IOHttpHandlerRegistrationPrincipal(
						new MultipartHttpHandlerRegistrationPrincipal(null)
						)
				);
		
		registrationAgent = new RegistrationAgent() {
			
			@Override
			public void register(String method, String path,
					BasicHttpHandler httpHandler) {
				
				registerHttpHandler(method, path, httpHandler, new RESTfullServiceFilterConfig());
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpGateway#registerHandler(java.lang.Object)
	 */
	@Override
	public void registerHandler(Object handler) {

		checkNotNull(handler);
		
		for (Method method : handler.getClass().getMethods())
		{
			principalChain.execute(registrationAgent, handler, method);
		}
	}
	
	protected abstract void unregisterHttpHandler(String method, String path);
	
	protected abstract void registerHttpHandler(String method, String path, BasicHttpHandler httpHandler, RESTfullServiceFilterConfig serviceConfig);
	

}
