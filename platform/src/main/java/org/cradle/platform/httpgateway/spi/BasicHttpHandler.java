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

import org.cradle.localization.LocalizationException;
import org.cradle.localization.LocalizationService;
import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadContentType;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 10, 2014
 */
public abstract class BasicHttpHandler {

	protected String method;
	protected String path;
	protected LocalizationService localizationService;

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}


	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @param localizationRepository the localizationRepository to set
	 */
	public void setLocalizationService(
			LocalizationService localizationService) {

		this.localizationService = localizationService;
	}

	protected void writeServiceResponse(HttpAdapter httpAdapter,
			GatewayResponse response, ResponseObject responseObject, boolean chuncked) {

		try {

			String contentType = determineContentType(httpAdapter,
					responseObject);

			String contentLanguage = httpAdapter.getContentLangauge();
			
			if(contentLanguage == null || contentLanguage.equals(""))
				contentLanguage = "en";

			ServiceResponse serviceResponse = new ServiceResponse();

			serviceResponse.build(responseObject, contentLanguage, localizationService);

			response.writeResponseDocument(serviceResponse, contentType, chuncked);

		} catch (BadRequestException e) {

			httpAdapter.exception(e);

		} catch (LocalizationException e) {

			httpAdapter.error(e);
		}
	}

	private String determineContentType(HttpAdapter httpAdapter,
			ResponseObject responseObject) throws BadRequestException {
		
		String contentType = responseObject.getContentType();

		if(contentType == null){

			contentType = checkHeader(httpAdapter.getContentType());
		}
		
		return contentType;
	}

	private String checkHeader(String value) throws BadRequestException {

		if(value == null || value.equals("")){

			throw new BadRequestException(new BadContentType());
		}

		return value;
	}

	public abstract void service(HttpAdapter httpAdapter, GatewayRequest request, GatewayResponse response)
			throws BadRequestException, UnauthorizedException;

}
