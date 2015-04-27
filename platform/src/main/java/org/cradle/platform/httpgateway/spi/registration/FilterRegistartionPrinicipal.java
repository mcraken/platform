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
import org.cradle.platform.httpgateway.HttpFilter;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.filter.PrecedenceFilter;
import org.cradle.platform.spi.RegistrationAgent;
import org.cradle.platform.spi.RegistrationPrincipal;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 26, 2015
 */
public class FilterRegistartionPrinicipal extends RegistrationPrincipal {

	/**
	 * @param next
	 */
	public FilterRegistartionPrinicipal(RegistrationPrincipal next) {
		super(next, HttpFilter.class);
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationPrincipal#executePrincipal(org.cradle.platform.spi.RegistrationAgent, java.lang.Object, java.lang.reflect.Method)
	 */
	@Override
	protected <T> void executePrincipal(RegistrationAgent agent, final T handler,
			final Method target) {
		
		HttpFilter filterAnnotation = target.getAnnotation(HttpFilter.class);
		
		agent.register(filterAnnotation, new PrecedenceFilter(filterAnnotation.precedence()){

			@Override
			public void filter(HttpAdapter httpAdapter) throws HttpException {
				
				try {
					
					target.invoke(handler, httpAdapter);
					
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
		
		checkMethodParamLength(target, 1, "One parameter is allowed for filter methods");
		
		checkMethodParam(target, 1, HttpAdapter.class, "HttpAdapter or one of its subclasses is required as the first parameter");
	}

}
