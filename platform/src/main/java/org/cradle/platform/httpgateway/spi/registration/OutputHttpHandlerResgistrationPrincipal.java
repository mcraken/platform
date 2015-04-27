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
package org.cradle.platform.httpgateway.spi.registration;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.HttpMethod;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.exception.RedirectException;
import org.cradle.platform.httpgateway.spi.OutputHttpHandler;
import org.cradle.platform.httpgateway.spi.ResponseObject;
import org.cradle.platform.httpgateway.spi.handler.BasicHttpHandler;
import org.cradle.platform.spi.RegistrationPrincipal;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 15, 2015
 */
public class OutputHttpHandlerResgistrationPrincipal extends HttpHandlerResgisterationPrinicipal {

	/**
	 * @param next
	 */
	public OutputHttpHandlerResgistrationPrincipal(RegistrationPrincipal next) {
		super(next);
	}

	/**
	 * @param target
	 * @return
	 */
	protected boolean isAnnotationSupported(Method target, Annotation annotation) {
		
		HttpMethod.Method targetMethod = ((HttpMethod) annotation).method();
		
		return targetMethod.equals(HttpMethod.Method.GET) || target.equals(HttpMethod.Method.DELETE);
	}

	/**
	 * @param handler
	 * @param target
	 * @param annotation
	 * @return
	 */
	protected BasicHttpHandler createHttpHandler(final Object handler,
			final Method target, final HttpMethod annotation) {

		return new OutputHttpHandler() {

			@Override
			protected ResponseObject execute(HttpAdapter httpAdapter)
					throws RedirectException, HttpException {

				try {

					return new ResponseObject(target.invoke(handler, httpAdapter), annotation.contentType());

				} catch (IllegalAccessException | IllegalArgumentException e) {

					throw new RuntimeException(e);

				} catch (InvocationTargetException e) {

					Throwable targetException  = e.getTargetException();
					
					if(targetException instanceof RedirectException)
						throw (RedirectException) targetException;
				
					if(targetException instanceof HttpException)
						throw (HttpException) targetException;

					throw new RuntimeException(e);
				}

			}

		};
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#isAnnotationValid(java.lang.reflect.Method, org.cradle.gateway.HttpMethod)
	 */
	@Override
	protected void isMethodValid(Method target, Annotation annotation) {
		
		checkMethodParamLength(target, 1, "One parameter is allowed for GET & DELETE methods");
		
		checkMethodParam(target, 1, HttpAdapter.class, "HttpAdapter or one of its subclasses is required as the first parameter");
		
	}
}
