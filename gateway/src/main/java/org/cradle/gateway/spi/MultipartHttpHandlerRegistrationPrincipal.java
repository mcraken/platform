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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.cradle.gateway.BasicHttpHandler;
import org.cradle.gateway.HttpAdapter;
import org.cradle.gateway.HttpMethod;
import org.cradle.gateway.HttpMethod.Method;
import org.cradle.gateway.restful.ResponseObject;
import org.cradle.gateway.restful.exception.RESTfulException;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 16, 2015
 */
public class MultipartHttpHandlerRegistrationPrincipal extends HttpHandlerResgisterationPrinicipal{

	/**
	 * @param next
	 */
	public MultipartHttpHandlerRegistrationPrincipal(RegistrationPrincipal next) {
		super(next);
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#isMethodSupported(org.cradle.gateway.HttpMethod.Method)
	 */
	@Override
	protected boolean isMethodSupported(Method method) {
		return method.equals(HttpMethod.Method.MULTIPART_POST);
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#isAnnotationValid(java.lang.reflect.Method, org.cradle.gateway.HttpMethod)
	 */
	@Override
	protected void isAnnotationValid(java.lang.reflect.Method target,
			HttpMethod annotation) {

		if(target.getParameterCount() != 3){

			throw new RuntimeException("Exactly 3 parameters are required for multipart POST methods");
		}

		checkHttpAdapterParam(target);

		ParameterizedType thirdParam = (ParameterizedType) target.getParameters()[2].getParameterizedType();

		String actualThirdParamType = thirdParam.getActualTypeArguments()[0].getTypeName();

		if(!actualThirdParamType.equalsIgnoreCase("java.io.File")){
			throw new RuntimeException("Third Parameter of multipart POST methods should be List of files");
		}

	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#createHttpHandler(java.lang.Object, java.lang.reflect.Method, org.cradle.gateway.HttpMethod)
	 */
	@Override
	protected BasicHttpHandler createHttpHandler(final Object handler,
			final java.lang.reflect.Method target, final HttpMethod annotation) {

		final Class<?> formType = target.getParameters()[1].getType();

		return new MultipartIOHttpHandler() {

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
					List<File> uploads) throws RESTfulException {
				try{
					
					return new ResponseObject(target.invoke(handler, httpAdapter, form, uploads), annotation.contentType());
					
				} catch (IllegalAccessException | IllegalArgumentException e) {

					throw new RuntimeException(e);

				} catch (InvocationTargetException e) {

					Throwable targetException  = e.getTargetException();

					if(targetException instanceof RESTfulException)
						throw (RESTfulException) targetException;

					throw new RuntimeException(e);
				}
			}
		};
	}

}
