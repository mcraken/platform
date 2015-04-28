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

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.HttpMethod;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.exception.RedirectException;
import org.cradle.platform.httpgateway.spi.OutputHttpHandler;
import org.cradle.platform.httpgateway.spi.ResponseObject;
import org.cradle.platform.httpgateway.spi.handler.AsyncIOtHttpHandler;
import org.cradle.platform.httpgateway.spi.handler.BasicHttpHandler;
import org.cradle.platform.httpgateway.spi.handler.MultipartIOHttpHandler;
import org.cradle.platform.spi.RegistrationAgent;
import org.cradle.platform.spi.RegistrationPrincipal;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 16, 2015
 */
public class HttpHandlerResgisterationPrinicipal extends RegistrationPrincipal<BasicHttpHandler, HttpMethod> {

	private String tempFolder;
	
	private HttpHandlerCreationStrategy outputHandlerCreator = new HttpHandlerCreationStrategy() {
		
		@Override
		public void isMethodValid(Method target, Annotation annotation) {
			
			checkMethodParamLength(target, 1, "One parameter is allowed for GET & DELETE methods");
			
			checkMethodParam(target, 1, HttpAdapter.class, "HttpAdapter or one of its subclasses is required as the first parameter");
			
		}
		
		@Override
		public BasicHttpHandler createHttpHandler(final Object handler, final Method target,
				final HttpMethod annotation) {
			
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
	};
	
	private HttpHandlerCreationStrategy ioHandlerCreator = new HttpHandlerCreationStrategy(){

		@Override
		public BasicHttpHandler createHttpHandler(final Object handler,
				final Method target, final HttpMethod annotation) {
			
			final Class<?> documentType = target.getParameterTypes()[1];
			
			return new AsyncIOtHttpHandler() {

				@Override
				public Class<?> getDocumentType() {
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

		@Override
		public void isMethodValid(Method target, Annotation annotation) {
			
			checkMethodParamLength(target, 2, "Exactly two parameter are required for POST & PUT methods");

			checkMethodParam(target, 1, HttpAdapter.class, "HttpAdapter or one of its subclasses is required as the first parameter");
			
		}
		
	};
	
	private HttpHandlerCreationStrategy multipartHandlerCreator = new HttpHandlerCreationStrategy(){

		@Override
		public BasicHttpHandler createHttpHandler(final Object handler,
				final Method target, final HttpMethod annotation) {
			
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
				protected ResponseObject execute(final HttpAdapter httpAdapter, Object form,
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

		@Override
		public void isMethodValid(Method target, Annotation annotation) {
			
			checkMethodParamLength(target, 3, "Exactly 3 parameters are required for multipart POST methods");
			
			checkMethodParam(target, 1, HttpAdapter.class, "HttpAdapter or one of its subclasses is required as the first parameter");

			ParameterizedType thirdParam = (ParameterizedType) target.getGenericParameterTypes()[2];

			String actualThirdParamType = thirdParam.getActualTypeArguments()[0].toString();

			if(!actualThirdParamType.equalsIgnoreCase("class java.io.File")){
				throw new RuntimeException("Third Parameter of multipart POST methods should be List of files");
			}
			
		}
		
	};
	
	private HashMap<HttpMethod.Method, HttpHandlerCreationStrategy> handlerCreatorsMap;
	
	/**
	 * @param next
	 * @param annotationClass
	 */
	public HttpHandlerResgisterationPrinicipal(String tempFolder, RegistrationAgent<BasicHttpHandler, HttpMethod> agent) {
		
		super(HttpMethod.class, agent);
		
		this.tempFolder = tempFolder;
		
		handlerCreatorsMap = new HashMap<HttpMethod.Method, HttpHandlerCreationStrategy>();
		
		handlerCreatorsMap.put(HttpMethod.Method.GET, outputHandlerCreator);
		
		handlerCreatorsMap.put(HttpMethod.Method.POST, ioHandlerCreator);
		
		handlerCreatorsMap.put(HttpMethod.Method.MULTIPART_POST, multipartHandlerCreator);
		
		handlerCreatorsMap.put(HttpMethod.Method.PUT, ioHandlerCreator);
		
		handlerCreatorsMap.put(HttpMethod.Method.DELETE, outputHandlerCreator);
		
	}

	/**
	 * @param target
	 */
	protected void checkHttpAdapterParam(Method target) {

		Class<?> paramType = target.getParameterTypes()[0];

		if(paramType != HttpAdapter.class){
			throw new RuntimeException("HttpAdapter or one of its subclasses is required as the first parameter");
		}
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationPrincipal#isMethodValid(java.lang.reflect.Method, java.lang.annotation.Annotation)
	 */
	@Override
	protected void isMethodValid(Method target, HttpMethod annotation) {
		
		handlerCreatorsMap.get(annotation.method()).isMethodValid(target, annotation);
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationPrincipal#executePrincipal(java.lang.Object, java.lang.reflect.Method, java.lang.annotation.Annotation)
	 */
	@Override
	protected BasicHttpHandler executePrincipal(final Object receiver, Method target,
			HttpMethod annotation) {
		
		return handlerCreatorsMap.get(annotation.method()).createHttpHandler(receiver, target, annotation);
	}

}