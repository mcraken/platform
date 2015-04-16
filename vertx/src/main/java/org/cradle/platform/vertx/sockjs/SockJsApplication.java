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
package org.cradle.platform.vertx.sockjs;

import org.cradle.platform.gateway.restful.document.DocumentWriter;
import org.cradle.reporting.SystemReportingService;
import org.vertx.java.core.Handler;
import org.vertx.java.core.sockjs.SockJSSocket;
import org.vertx.java.core.streams.Pump;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 14, 2014
 */
public abstract class SockJsApplication implements Handler<SockJSSocket> {
	
	private SockJsGateway gateway;
	
	private DocumentWriter writer;
	
	private String contentType;
	
	private String path;
	
	private SystemReportingService reportingService;
	
	public void setReportingService(SystemReportingService reportingService) {
		this.reportingService = reportingService;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public void setGateway(SockJsGateway gateway) {
		this.gateway = gateway;
	}

	public void init() {
		gateway.registerAgentApplication(path, contentType, this);
	}
	
	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(SockJSSocket socket) {
		
		initConnection(socket);
		
		SockJsAgent agent = createAgent();
		
		agent.setWriter(writer);
		
		agent.setSocket(socket);
		
	}

	private void initConnection(SockJSSocket socket){

		Pump.createPump(socket, socket).start();

		socket.exceptionHandler(new Handler<Throwable>() {

			@Override
			public void handle(Throwable event) {
				
				reportingService.exception(this.getClass().getSimpleName(), SystemReportingService.FILE, event);
				
				event.printStackTrace();
			}
		});
	}
	
	public void setWriter(DocumentWriter writer) {
		this.writer = writer;
	}
	
	protected abstract SockJsAgent createAgent();
	
}
