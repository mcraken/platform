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
package org.mcplissken.gateway.restful.filter.tracking;

import org.mcplissken.disruptor.workerpool.WorkEvent;
import org.mcplissken.disruptor.workerpool.WorkerPoolOperation;
import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.gateway.restful.exception.BadRequestException;
import org.mcplissken.gateway.restful.exception.UnauthorizedException;
import org.mcplissken.gateway.restful.exception.UnknownResourceException;
import org.mcplissken.gateway.restful.filter.BasicRESTfulFilter;
import org.mcplissken.repository.ModelRepository;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 11, 2015
 */
public class RESTfulTrackingFilter extends BasicRESTfulFilter {

	private ModelRepository modelRepository;
	private WorkerPoolOperation<WorkEvent> workerPool;
	private RESTfulTrackingFilterFactory factory;
	
	public RESTfulTrackingFilter(
			ModelRepository modelRepository,
			WorkerPoolOperation<WorkEvent> workerPool, 
			RESTfulTrackingFilterFactory factory) {
		
		this.modelRepository = modelRepository;
		
		this.workerPool = workerPool;
		
		this.factory = factory;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.restful.filter.BasicRESTfulFilter#doFilter(org.mcplissken.gateway.HttpAdapter)
	 */
	@Override
	protected void doFilter(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException,
			UnknownResourceException {
		
		workerPool.onData(new TrackingWorkEvent(modelRepository, httpAdapter, factory));
	}

}
