/**
 * 
 */
package org.mcplissken.osgi.vertx;

import org.mcplissken.reporting.SystemReportingService;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 25, 2014
 */
public class DeploymentReporter implements Handler<AsyncResult<String>>{

	private String moduleName;
	private SystemReportingService reportingService;
	
	public DeploymentReporter(String moduleName, SystemReportingService reportingService) {
		
		this.moduleName = moduleName;
		
		this.reportingService = reportingService;
	}

	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(AsyncResult<String> event) {
		
		reportingService.info(this.getClass().getSimpleName(), SystemReportingService.FILE, "Deployed "+ moduleName +"? " + event.succeeded());
		
		if(event.failed()){
			
			reportingService.exception(this.getClass().getSimpleName(), SystemReportingService.FILE, event.cause());
		}
	}

}
