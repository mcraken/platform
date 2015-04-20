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
package org.cradle.platform.sockjsgateway.spi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.spi.AsyncIOtHttpHandler;
import org.cradle.platform.httpgateway.spi.ResponseObject;
import org.cradle.platform.sockjsgateway.SockJS;
import org.cradle.platform.spi.RegistrationAgent;
import org.cradle.platform.spi.RegistrationPrincipal;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 19, 2015
 */
public class SockJsHandlerRegistrationPrinicipal extends RegistrationPrincipal{

	/**
	 * @param next
	 */
	public SockJsHandlerRegistrationPrinicipal(RegistrationPrincipal next) {
		super(next);
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationPrincipal#executePrincipal(org.cradle.platform.spi.RegistrationAgent, java.lang.Object, java.lang.reflect.Method)
	 */
	@Override
	protected void executePrincipal(RegistrationAgent agent, final Object handler,
			final Method target) {
		
		SockJS webSocket = target.getAnnotation(SockJS.class);
		
		if(webSocket != null ){
			
			checkMethodParamLength(target, 2, "Websocket handler require exactly two parameters");
			
			checkMethodParam(target, 1, HttpAdapter.class, "First parameter must be of type HttpAdapter");
			
			final Class<?> documentType = target.getParameterTypes()[1];
			
			agent.register("", webSocket.path(), new AsyncIOtHttpHandler() {
				
				@Override
				protected Class<?> getDocumentType() {
					return documentType;
				}
				
				@Override
				protected ResponseObject execute(HttpAdapter adapter, Object document)
						throws HttpException {
					try{
						
						return new ResponseObject(target.invoke(handler, adapter, document));

					} catch (IllegalAccessException | IllegalArgumentException e) {

						throw new RuntimeException(e);

					} catch (InvocationTargetException e) {

						Throwable targetException  = e.getTargetException();
					
						if(targetException instanceof HttpException)
							throw (HttpException) targetException;

						throw new RuntimeException(e);
					}
				}
			});
		}
		
	}

}
