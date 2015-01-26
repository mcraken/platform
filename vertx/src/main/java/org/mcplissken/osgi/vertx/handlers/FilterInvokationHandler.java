/**
 * 
 */
package org.mcplissken.osgi.vertx.handlers;

import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.gateway.restful.exception.RESTfulException;
import org.mcplissken.gateway.restful.filter.RESTfulFilter;
import org.mcplissken.gateway.vertx.VertxHttpAdapter;
import org.mcplissken.reporting.SystemReportingService;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

/**
 * @author Sherief Shawky
 * @email mcrakens@gmail.com
 * @date Aug 25, 2014
 */
public class FilterInvokationHandler implements Handler<HttpServerRequest> {

	private RESTfulFilter filter;
	
	private SystemReportingService reportingService;

	public FilterInvokationHandler(RESTfulFilter startFilter, SystemReportingService reportingService) {

		filter = startFilter;
		
		this.reportingService = reportingService;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(HttpServerRequest request) {

		HttpAdapter httpAdapter = new VertxHttpAdapter(request, request.response());

		try {

			filter.filter(httpAdapter);

		} catch (RESTfulException e) {
			
			reportingService.exception(this.getClass().getSimpleName(), SystemReportingService.FILE, e);
			
			httpAdapter.exception(e);

		} catch (Exception e) {
			
			reportingService.exception(this.getClass().getSimpleName(), SystemReportingService.MAIL, e);
			
			httpAdapter.error(e);
		}
	}

}