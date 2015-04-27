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
package org.cradle.platform.eventbus.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cradle.platform.eventbus.EventbusListener;
import org.cradle.platform.spi.RegistrationAgent;
import org.cradle.platform.spi.RegistrationPrincipal;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 27, 2015
 */
public class EventbusListenerRegistrationPrincipal extends RegistrationPrincipal{

	/**
	 * @param next
	 * @param annotationClass
	 */
	public EventbusListenerRegistrationPrincipal(RegistrationPrincipal next) {
		
		super(next, EventbusListener.class);
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
		
		checkForVoidReturnType(target, "Eventbus listener method should return nothing.");
		
		checkMethodParamLength(target, 1, "Eventbus listener method should accept only one parameter.");
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationPrincipal#executePrincipal(org.cradle.platform.spi.RegistrationAgent, java.lang.Object, java.lang.reflect.Method)
	 */
	@Override
	protected <T> void executePrincipal(RegistrationAgent agent, final T handler,
			final Method target) {
		
		EventbusListener eventbusListener = target.getAnnotation(EventbusListener.class);
		
		if(checkMethodParam(target, 1, String.class)){
			
			agent.register(eventbusListener, createTextMessageHandler(handler, target));
			
		} else {
			
			agent.register(eventbusListener, new TypeEventbusHandler(getParamType(target, 1)) {
				
				@Override
				protected void recieve(Object message) {
					
						try {
							
							target.invoke(handler, message);
							
						} catch (IllegalAccessException | IllegalArgumentException e) {
							
							throw new RuntimeException(e);
							
						} catch (InvocationTargetException e) {
							
							e.printStackTrace();
						}
				}
			});
			
		}
		
	}

	/**
	 * @param handler
	 * @param target
	 * @return
	 */
	private <T> EventbusHandler createTextMessageHandler(final T handler,
			final Method target) {
		return new EventbusHandler() {
			
			@Override
			public void recieve(String message) {
				
				try {
					
					target.invoke(handler, message);
					
				} catch (IllegalAccessException | IllegalArgumentException e) {
					
					throw new RuntimeException(e);
					
				} catch (InvocationTargetException e) {
					
					e.printStackTrace();
				}
			}
		};
	}

}
