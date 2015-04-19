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
package org.cradle.platform.httpgateway.spi;

import java.lang.reflect.InvocationTargetException;

import org.cradle.platform.httpgateway.BasicHttpHandler;
import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.HttpMethod;
import org.cradle.platform.httpgateway.HttpMethod.Method;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.spi.RegistrationPrincipal;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 16, 2015
 */
public class IOHttpHandlerRegistrationPrincipal  extends HttpHandlerResgisterationPrinicipal{

	/**
	 * @param next
	 */
	public IOHttpHandlerRegistrationPrincipal(RegistrationPrincipal next) {
		super(next);
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#isMethodSupported(org.cradle.gateway.HttpMethod.Method)
	 */
	@Override
	protected boolean isMethodSupported(Method method) {

		return method.equals(HttpMethod.Method.POST) || method.equals(HttpMethod.Method.PUT);
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#createHttpHandler(java.lang.Object, java.lang.reflect.Method, org.cradle.gateway.HttpMethod)
	 */
	@Override
	protected BasicHttpHandler createHttpHandler(final Object handler,
			final java.lang.reflect.Method target, final HttpMethod annotation) {
		
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
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#isAnnotationValid(java.lang.reflect.Method, org.cradle.gateway.HttpMethod)
	 */
	@Override
	protected void isAnnotationValid(java.lang.reflect.Method target,
			HttpMethod annotation) {
		
		checkMethodParamLength(target, 2, "Exactly two parameter are required for POST & PUT methods");

		checkMethodParam(target, 1, HttpAdapter.class, "HttpAdapter or one of its subclasses is required as the first parameter");

	}

}