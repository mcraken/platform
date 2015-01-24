/**
 * 
 */
package org.mcplissken.gateway.restful;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mcplissken.localization.LocalizationException;
import org.mcplissken.localization.LocalizationService;
import org.mcplissken.localization.Localized;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 20, 2015
 */
public class ServiceResponse{

	private static final String SUCCESS_STATUS = "success";
	private static final String FAILURE_STATUS = "failure";

	private String status;

	private List<Localized> messages;

	private Map<String, String> errors;

	private Object response;

	private transient String language;

	private transient LocalizationService localizationRepository;

	public ServiceResponse(String language, LocalizationService localizationRepository) {

		this.localizationRepository = localizationRepository;

		this.language = language;
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

	/**
	 * @param localizationRepository the localizationRepository to set
	 */
	public void setLocalizationRepository(LocalizationService localizationRepository) {

		this.localizationRepository = localizationRepository;
	}

	public void build(ResponseObject responseObject) throws LocalizationException{

		if(responseObject.getErrorsIds() == null){

			status = SUCCESS_STATUS;

			response = responseObject.getResponse();

		} else {

			status = FAILURE_STATUS;

			List<Localized> targetErrors = localizationRepository.localize(language, responseObject.getErrorsIds());

			errors = new HashMap<String, String>();
			
			for(Localized localized : targetErrors){
				errors.put(localized.getId(), localized.getDesc());
			}
		}

		if(responseObject.getMessagesIds() != null)
			messages = localizationRepository.localize(language, responseObject.getMessagesIds());
	}

}