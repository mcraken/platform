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
package org.cradle.platform.httpgateway.restful.filter.tracking;

import java.util.List;

import org.cradle.disruptor.workerpool.WorkEvent;
import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.repository.ModelRepository;
import org.cradle.repository.models.RestModel;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 11, 2015
 */
public class TrackingWorkEvent implements WorkEvent {

	private ModelRepository modelRepository;
	private HttpAdapter httpAdapter;
	private RESTfulTrackingFilterFactory factory;
	
	public TrackingWorkEvent(
			ModelRepository modelRepository,
			HttpAdapter httpAdapter,
			RESTfulTrackingFilterFactory factory
			) {
		
		this.modelRepository = modelRepository;
		
		this.httpAdapter = httpAdapter;
		
		this.factory = factory;
	}

	/* (non-Javadoc)
	 * @see org.cradle.disruptor.workerpool.WorkEvent#execute()
	 */
	@Override
	public void execute() {
		
		TrackingLogCreator logCreator = factory.getLogCreator(httpAdapter.method());
		
		List<RestModel> trackingLogs =  logCreator.create(httpAdapter);
		
		if(trackingLogs != null && trackingLogs.size() > 0){
			
			modelRepository.write(trackingLogs);
			
		}
	}

}