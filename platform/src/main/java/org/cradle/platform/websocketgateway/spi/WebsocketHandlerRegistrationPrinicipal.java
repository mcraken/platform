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
package org.cradle.platform.websocketgateway.spi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.spi.ResponseObject;
import org.cradle.platform.httpgateway.spi.handler.AsyncIOtHttpHandler;
import org.cradle.platform.httpgateway.spi.handler.AsyncInputHttpHandler;
import org.cradle.platform.httpgateway.spi.handler.BasicHttpHandler;
import org.cradle.platform.spi.RegistrationAgent;
import org.cradle.platform.spi.RegistrationPrincipal;
import org.cradle.platform.websocketgateway.WebSocket;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 19, 2015
 */
public class WebsocketHandlerRegistrationPrinicipal extends RegistrationPrincipal<BasicHttpHandler, WebSocket>{

	/**
	 * @param next
	 */
	public WebsocketHandlerRegistrationPrinicipal(RegistrationAgent<BasicHttpHandler, WebSocket> agent) {
		super(WebSocket.class, agent);
	}

	/**
	 * @param agent
	 * @param handler
	 * @param target
	 * @param webSocket
	 */
	private BasicHttpHandler registerInputHttpHandler(final Object handler, final Method target, WebSocket webSocket) {


		final Class<?> documentType = target.getParameterTypes()[1];

		return new AsyncInputHttpHandler() {

			@Override
			protected Class<?> getDocumentType() {
				return documentType;
			}

			@Override
			protected void execute(HttpAdapter httpAdapter, Object document)
					throws HttpException {
				try{

					target.invoke(handler, httpAdapter, document);

				} catch (IllegalAccessException | IllegalArgumentException e) {

					throw new RuntimeException(e);

				} catch (InvocationTargetException e) {

					Throwable targetException  = e.getTargetException();

					if(targetException instanceof HttpException)
						throw (HttpException) targetException;

					throw new RuntimeException(e);
				}
			}
		};
	}

	/**
	 * @param agent
	 * @param handler
	 * @param target
	 * @param webSocket
	 */
	private BasicHttpHandler registerInputOutputHandler(final Object handler, final Method target, WebSocket webSocket) {

		final Class<?> documentType = target.getParameterTypes()[1];

		 return new AsyncIOtHttpHandler() {

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
		};
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationPrincipal#isMethodValid(java.lang.reflect.Method, java.lang.annotation.Annotation)
	 */
	@Override
	protected void isMethodValid(Method target, WebSocket annotation) {
		
		checkMethodParamLength(target, 2, "IO Websocket handler require exactly two parameters");

		checkMethodParam(target, 1, HttpAdapter.class, "First parameter must be of type HttpAdapter");

		WebSocket webSocket = (WebSocket) annotation;

		if(webSocket.type() == WebSocket.Type.RECEIVER)
			checkForVoidReturnType(target, "Receiver weboscket methods should return nothing");
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationPrincipal#executePrincipal(java.lang.Object, java.lang.reflect.Method, java.lang.annotation.Annotation)
	 */
	@Override
	protected BasicHttpHandler executePrincipal(Object receiver, Method target,
			WebSocket annotation) {
		
		switch(annotation.type()){
		
			case RECEIVER:
				return registerInputHttpHandler(receiver, target, annotation);
				
			default:
				return registerInputOutputHandler(receiver, target, annotation);
		}
	}


}
