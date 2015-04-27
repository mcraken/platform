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
package org.cradle.platform.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 15, 2015
 */
public abstract class RegistrationPrincipal {
	
	private RegistrationPrincipal next;
	private Class<? extends Annotation> annotationClass;
	
	/**
	 * @param next
	 * @param annotationClass
	 */
	public RegistrationPrincipal(RegistrationPrincipal next,
			Class<? extends Annotation> annotationClass) {
		
		this.next = next;
		this.annotationClass = annotationClass;
	}

	public <T>void execute(RegistrationAgent agent, T handler, Method target){
		
		Annotation annotation = target.getAnnotation(annotationClass);

		if(annotation != null ){

			if(isAnnotationSupported(target, annotation)){

				isMethodValid(target, annotation);

				executePrincipal(agent, handler, target);
			}

		}
		
		
		if(next != null)
			next.execute(agent, handler, target);
	}
	
	protected Class<?> getParamType(Method target, int number){
		
		return target.getParameterTypes()[number - 1];
	}
	
	/**
	 * @param target
	 */
	protected void checkMethodParam(Method target, int number, Class<?> targetClass, String errorMessage) {
	
		Class<?> paramType = getParamType(target, number);
	
		if(paramType != targetClass){
			throw new RuntimeException(errorMessage);
		}
	}
	
	protected boolean checkMethodParam(Method target, int number, Class<?> targetClass) {
		
		Class<?> paramType = getParamType(target, number);
	
		if(paramType != targetClass){
			return false;
		}
		
		return true;
	}
	
	protected void checkMethodParamLength(Method target, int length, String errorMessage){
		
		if(target.getParameterTypes().length != length){

			throw new RuntimeException(errorMessage);
		}
	}
	
	/**
	 * @param target
	 */
	protected void checkForVoidReturnType(final Method target, String errorMessage) {
		
		Class<?> returnType = target.getReturnType();
		
		if(!returnType.getName().equalsIgnoreCase("void"))
			throw new RuntimeException(errorMessage);
	}
	
	protected abstract boolean isAnnotationSupported(Method target, Annotation annotation);
	
	protected abstract void isMethodValid(Method target, Annotation annotation);
	
	protected abstract <T>void executePrincipal(RegistrationAgent agent, T handler, Method target);
}
