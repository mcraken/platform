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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.cradle.gateway.BasicHttpHandler;
import org.cradle.gateway.HttpAdapter;
import org.cradle.gateway.HttpMethod;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 16, 2015
 */
public abstract class HttpHandlerResgisterationPrinicipal extends
		RegistrationPrincipal {

	/**
	 * @param next
	 */
	public HttpHandlerResgisterationPrinicipal(RegistrationPrincipal next) {
		super(next);
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.ResgistrationPrincipal#executePrincipal(org.cradle.gateway.spi.RegistrationAgent, java.lang.Object, java.lang.reflect.Method)
	 */
	@Override
	protected void executePrincipal(RegistrationAgent agent, final Object handler,
			final Method target) {

		HttpMethod annotation = target.getAnnotation(HttpMethod.class);

		if(annotation != null ){

			HttpMethod.Method method = annotation.method();

			if(isMethodSupported(method)){

				isAnnotationValid(target, annotation);

				agent.register(annotation.method().getValue(), annotation.path(), createHttpHandler(handler, target, annotation));
			}

		}

	}

	/**
	 * @param target
	 */
	protected void checkHttpAdapterParam(Method target) {
	
		Parameter httpAdapterParam = target.getParameters()[0];
	
		Class<?> paramType = httpAdapterParam.getType();
	
		if(paramType != HttpAdapter.class){
			throw new RuntimeException("HttpAdapter or one of its subclasses is required as the first parameter");
		}
	}
	
	protected abstract boolean isMethodSupported(HttpMethod.Method method);
	
	protected abstract void isAnnotationValid(Method target, HttpMethod annotation);
	
	protected abstract BasicHttpHandler createHttpHandler(final Object handler,
			final Method target, final HttpMethod annotation);

}