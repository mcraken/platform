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
package org.cradle.platform.httpgateway.restful;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cradle.localization.LocalizationException;
import org.cradle.localization.LocalizationService;
import org.cradle.localization.Localized;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 20, 2015
 */
public class ServiceResponse{

	private static final String SUCCESS_STATUS = "success";
	private static final String FAILURE_STATUS = "failure";

	private String status;

	private List<Localized> messages;

	private Map<String, String> errors;

	private Object response;

	public ServiceResponse() {

	}

	public String getStatus() {
		return status;
	}

	public Object getResponse() {
		return response;
	}

	/**
	 * @return the errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}	
	
	/**
	 * @return the messages
	 */
	public List<Localized> getMessages() {
		return messages;
	}

	public void build(
			ResponseObject responseObject, 
			String language, 
			LocalizationService localizationService
			) throws LocalizationException{

		if(responseObject.getErrorsIds() == null){

			status = SUCCESS_STATUS;

			response = responseObject.getResponse();

		} else {

			status = FAILURE_STATUS;
			
			if(localizationService != null){
				
				localizeErrors(responseObject, language, localizationService);
			}
		}

		if(responseObject.getMessagesIds() != null && localizationService != null)
			messages = localizationService.localize(language, responseObject.getMessagesIds());
	}

	/**
	 * @param responseObject
	 * @param language
	 * @param localizationService
	 * @throws LocalizationException
	 */
	private void localizeErrors(ResponseObject responseObject,
			String language, LocalizationService localizationService)
			throws LocalizationException {
		
		List<Localized> targetErrors = localizationService.localize(language, responseObject.getErrorsIds());

		errors = new HashMap<String, String>();
		
		for(Localized localized : targetErrors){
			errors.put(localized.getId(), localized.getDesc());
		}
	}

}