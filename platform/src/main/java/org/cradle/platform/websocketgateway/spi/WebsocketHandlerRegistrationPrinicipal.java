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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.spi.ResponseObject;
import org.cradle.platform.httpgateway.spi.handler.AsyncIOtHttpHandler;
import org.cradle.platform.httpgateway.spi.handler.AsyncInputHttpHandler;
import org.cradle.platform.spi.RegistrationAgent;
import org.cradle.platform.spi.RegistrationPrincipal;
import org.cradle.platform.websocketgateway.WebSocket;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 19, 2015
 */
public class WebsocketHandlerRegistrationPrinicipal extends RegistrationPrincipal{

	/**
	 * @param next
	 */
	public WebsocketHandlerRegistrationPrinicipal(RegistrationPrincipal next) {
		super(next, WebSocket.class);
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationPrincipal#executePrincipal(org.cradle.platform.spi.RegistrationAgent, java.lang.Object, java.lang.reflect.Method)
	 */
	protected <T> void executePrincipal(RegistrationAgent agent, T handler,
			Method target) {

		WebSocket webSocket = target.getAnnotation(WebSocket.class);

		switch(webSocket.type()){
			case RECEIVER:
				registerInputHttpHandler(agent, handler, target, webSocket);
				break;
			case SYNCHRONOUS:
				registerInputOutputHandler(agent, handler, target, webSocket);
				break;
		};

	}

	/**
	 * @param agent
	 * @param handler
	 * @param target
	 * @param webSocket
	 */
	private void registerInputHttpHandler(RegistrationAgent agent,
			final Object handler, final Method target, WebSocket webSocket) {


		final Class<?> documentType = target.getParameterTypes()[1];

		agent.register(webSocket, new AsyncInputHttpHandler() {

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
		});
	}

	/**
	 * @param agent
	 * @param handler
	 * @param target
	 * @param webSocket
	 */
	private void registerInputOutputHandler(RegistrationAgent agent,
			final Object handler, final Method target, WebSocket webSocket) {

		final Class<?> documentType = target.getParameterTypes()[1];

		agent.register(webSocket, new AsyncIOtHttpHandler() {

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

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationPrincipal#isAnnotationSupported(java.lang.reflect.Method, java.lang.annotation.Annotation)
	 */
	@Override
	protected boolean isAnnotationSupported(Method target, Annotation annotation) {

		return true;
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationPrincipal#isMethodValid(java.lang.reflect.Method, java.lang.annotation.Annotation)
	 */
	@Override
	protected void isMethodValid(Method target, Annotation annotation) {

		checkMethodParamLength(target, 2, "IO Websocket handler require exactly two parameters");

		checkMethodParam(target, 1, HttpAdapter.class, "First parameter must be of type HttpAdapter");

		WebSocket webSocket = (WebSocket) annotation;

		if(webSocket.type() == WebSocket.Type.RECEIVER)
			checkForVoidReturnType(target, "Receiver weboscket methods should return nothing");

	}


}
