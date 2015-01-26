/**
 * 
 */
package org.mcplissken.gateway;

import org.mcplissken.gateway.restful.RESTfulRequest;
import org.mcplissken.gateway.restful.RESTfulResponse;
import org.mcplissken.gateway.restful.ResponseObject;
import org.mcplissken.gateway.restful.ServiceResponse;
import org.mcplissken.gateway.restful.exception.BadContentType;
import org.mcplissken.gateway.restful.exception.BadRequestException;
import org.mcplissken.gateway.restful.exception.UnauthorizedException;
import org.mcplissken.gateway.restful.filter.RESTfullServiceFilterConfig;
import org.mcplissken.localization.LocalizationException;
import org.mcplissken.localization.LocalizationService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 10, 2014
 */
public abstract class BasicHttpHandler {

	protected String method;
	protected String path;
	protected HttpGateway gateway;
	protected RESTfullServiceFilterConfig filterConfig;
	protected LocalizationService localizationService;

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @param gateway the gateway to set
	 */
	public void setGateway(HttpGateway gateway) {
		this.gateway = gateway;
	}

	/**
	 * @param filterConfig the filterConfig to set
	 */
	public void setFilterConfig(RESTfullServiceFilterConfig filterConfig) {
		this.filterConfig = filterConfig;
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

	public void destroy(){

	}

	protected RESTfullServiceFilterConfig getServiceConfig(){

		if(filterConfig == null)
			return new RESTfullServiceFilterConfig();

		return filterConfig;
	}

	public void unbindGateway(HttpGateway gateway){

		gateway.unregisterHttpHandler(method, path);
	}

	protected void writeServiceResponse(HttpAdapter httpAdapter,
			RESTfulResponse response, ResponseObject responseObject, boolean chuncked) {

		try {

			String contentType = determineContentType(httpAdapter,
					responseObject);

			String contentLanguage = checkHeader(httpAdapter.getContentLangauge());

			ServiceResponse serviceResponse = new ServiceResponse(contentLanguage, localizationService);

			serviceResponse.build(responseObject);

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

	public  void init(){

		gateway.registerHttpHandler(method, path, this, getServiceConfig());
	}

	public abstract void service(HttpAdapter httpAdapter, RESTfulRequest request, RESTfulResponse response)
			throws BadRequestException, UnauthorizedException;

}
