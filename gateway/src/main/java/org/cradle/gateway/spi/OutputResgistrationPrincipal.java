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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.cradle.gateway.HttpAdapter;
import org.cradle.gateway.OutputHttpHandler;
import org.cradle.gateway.method.GET;
import org.cradle.gateway.restful.ResponseObject;
import org.cradle.gateway.restful.exception.RESTfulException;
import org.cradle.gateway.restful.exception.RedirectException;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 15, 2015
 */
public class OutputResgistrationPrincipal extends RegistrationPrincipal {

	/**
	 * @param next
	 */
	public OutputResgistrationPrincipal(RegistrationPrincipal next) {
		super(next);
	}


	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.ResgistrationPrincipal#executePrincipal(org.cradle.gateway.spi.RegistrationAgent, java.lang.Object, java.lang.reflect.Method)
	 */
	@Override
	protected void executePrincipal(RegistrationAgent agent, final Object handler,
			final Method target) {

		final GET getAnnotation = target.getAnnotation(GET.class);

		if(getAnnotation != null){
			
			if(target.getParameterCount() != 1){
				throw new RuntimeException("One parameter is allowed for GET & DELETE methods");
			}

			Parameter httpAdapterParam = target.getParameters()[0];

			Class<?> paramType = httpAdapterParam.getType();

			if(paramType != HttpAdapter.class){
				throw new RuntimeException("HttpAdapter or one of its subclasses is allowed as a parameter");
			}

			registerHandler(agent, handler, target, getAnnotation);

		}

	}


	/**
	 * @param agent
	 * @param handler
	 * @param target
	 * @param getAnnotation
	 */
	private void registerHandler(RegistrationAgent agent, final Object handler,
			final Method target, final GET getAnnotation) {

		agent.register("GET", getAnnotation.path(), new OutputHttpHandler() {

			@Override
			protected ResponseObject execute(HttpAdapter httpAdapter)
					throws RedirectException, RESTfulException {

				try {

					return new ResponseObject(target.invoke(handler, httpAdapter), getAnnotation.contentType());

				} catch (IllegalAccessException | IllegalArgumentException e) {

					throw new RuntimeException(e);

				} catch (InvocationTargetException e) {

					Throwable targetException  = e.getTargetException();

					if(targetException instanceof RedirectException)
						throw (RedirectException) targetException;

					if(targetException instanceof RESTfulException)
						throw (RESTfulException) targetException;

					throw new RuntimeException(e);
				}

			}
		});
	}

}
