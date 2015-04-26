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

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.HttpMethod;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.spi.RegistrationPrincipal;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 16, 2015
 */
public class MultipartHttpHandlerRegistrationPrincipal extends HttpHandlerResgisterationPrinicipal{

	private String tempFolder;
	
	/**
	 * @param next
	 * @param tempFolder
	 */
	public MultipartHttpHandlerRegistrationPrincipal(
			RegistrationPrincipal next, String tempFolder) {
		
		super(next);
		
		this.tempFolder = tempFolder;
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#isMethodSupported(org.cradle.gateway.HttpMethod.Method)
	 */
	@Override
	protected boolean isAnnotationSupported(java.lang.reflect.Method target, Annotation annotation) {
		
		HttpMethod.Method targetMethod = ((HttpMethod) annotation).method();
		
		return targetMethod.equals(HttpMethod.Method.MULTIPART_POST);
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#isAnnotationValid(java.lang.reflect.Method, org.cradle.gateway.HttpMethod)
	 */
	@Override
	protected void isMethodValid(java.lang.reflect.Method target,
			Annotation annotation) {
		
		checkMethodParamLength(target, 3, "Exactly 3 parameters are required for multipart POST methods");
		
		checkMethodParam(target, 1, HttpAdapter.class, "HttpAdapter or one of its subclasses is required as the first parameter");

		ParameterizedType thirdParam = (ParameterizedType) target.getGenericParameterTypes()[2];

		String actualThirdParamType = thirdParam.getActualTypeArguments()[0].toString();

		if(!actualThirdParamType.equalsIgnoreCase("class java.io.File")){
			throw new RuntimeException("Third Parameter of multipart POST methods should be List of files");
		}

	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#createHttpHandler(java.lang.Object, java.lang.reflect.Method, org.cradle.gateway.HttpMethod)
	 */
	@Override
	protected BasicHttpHandler createHttpHandler(final Object handler,
			final java.lang.reflect.Method target, final HttpMethod annotation) {

		final Class<?> formType = target.getParameterTypes()[1];

		return new MultipartIOHttpHandler(tempFolder) {

			@Override
			public Object createFormInstance() {
				try {
					return formType.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException("Error while creating form instance");
				}
			}

			@Override
			protected ResponseObject execute(HttpAdapter httpAdapter, Object form,
					List<File> uploads) throws HttpException {
				try{
					
					return new ResponseObject(target.invoke(handler, httpAdapter, form, uploads), annotation.contentType());
					
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

}
