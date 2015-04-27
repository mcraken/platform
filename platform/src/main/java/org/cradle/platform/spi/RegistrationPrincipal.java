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
public abstract class RegistrationPrincipal<T, A extends Annotation> {

	private Class<A> annotationClass;
	private RegistrationAgent<T, A> registrationAgent;
	/**
	 * @param next
	 * @param annotationClass
	 */
	public RegistrationPrincipal(Class<A> annotationClass, RegistrationAgent<T, A> registrationAgent) {

		this.annotationClass = annotationClass;
		
		this.registrationAgent = registrationAgent;
	}

	public void execute(Object receiver, Method target){

		A annotation = target.getAnnotation(annotationClass);

		if(annotation != null ){

			isMethodValid(target, annotation);

			T handler = executePrincipal(receiver, target, annotation);
			
			registrationAgent.register(annotation, handler);
		}

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

	protected abstract void isMethodValid(Method target, A annotation);

	protected abstract T executePrincipal(Object receiver, Method target, A annotation);
}
