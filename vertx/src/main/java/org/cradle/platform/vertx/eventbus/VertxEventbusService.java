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
package org.cradle.platform.vertx.eventbus;

import java.util.Map;

import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.eventbus.CradleEventbus;
import org.cradle.platform.eventbus.TypeEventbusHandler;
import org.cradle.platform.eventbus.TextEventbusHandler;
import org.cradle.reporting.SystemReportingService;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 19, 2015
 */
public class VertxEventbusService implements CradleEventbus {

	private EventBus eventBus;
	private Map<String, DocumentWriter> documentWriters;
	private SystemReportingService reportingService;
	

	/**
	 * @param eventBus
	 * @param documentWriters
	 * @param reportingService
	 */
	public VertxEventbusService(EventBus eventBus,
			Map<String, DocumentWriter> documentWriters,
			SystemReportingService reportingService) {
		this.eventBus = eventBus;
		this.documentWriters = documentWriters;
		this.reportingService = reportingService;
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.VertxEventBusService#subscribe(java.lang.String, org.cradle.osgi.vertx.EventBusHandler)
	 */
	@Override
	public void subscribe(String address, TextEventbusHandler handler) {

		eventBus.registerHandler(address, new VertxTextEventbusHandler(handler));
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.VertxEventBusService#unsubscribe(java.lang.String, org.cradle.osgi.vertx.EventBusHandler)
	 */
	@Override
	public void unsubscribe(String address, TextEventbusHandler handler) {

		eventBus.unregisterHandler(address, new VertxTextEventbusHandler(handler));
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.eventbus.VertxEventBusService#subscribe(java.lang.String, org.cradle.osgi.vertx.eventbus.JsonEventbusHandler)
	 */
	@Override
	public <T>void subscribe(String address, TypeEventbusHandler<T> handler) {
		
		eventBus.registerHandler(address, new VertxJsonEventbusHandler<T>(handler));
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.eventbus.VertxEventBusService#unsubscribe(java.lang.String, org.cradle.osgi.vertx.eventbus.JsonEventbusHandler)
	 */
	@Override
	public <T>void unsubscribe(String address, TypeEventbusHandler<T> handler) {
		
		eventBus.unregisterHandler(address, new VertxJsonEventbusHandler<T>(handler));

	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.eventbus.VertxEventBusService#publish(java.lang.String, org.vertx.java.core.json.JsonObject)
	 */
	@Override
	public void publish(String address, String message) {
		eventBus.publish(address, message);
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.eventbus.VertxEventBusService#publish(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public <T>void publish(String address, T message, String contentType) {

		DocumentWriter documentWriter = documentWriters.get(contentType);

		StringBuffer output = new StringBuffer();

		documentWriter.write(message, output);

		eventBus.publish(address, output.toString());
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.eventbus.EventbusService#shutdown()
	 */
	@Override
	public void shutdown() {
		
		eventBus.close(new Handler<AsyncResult<Void>>() {

			@Override
			public void handle(AsyncResult<Void> event) {
				
				if(reportingService != null){
					reportingService.info(this.getClass().getSimpleName(), SystemReportingService.CONSOLE, "Event bus closed");
				}
			}
		});
		
	}

}
