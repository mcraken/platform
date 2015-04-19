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
package org.cradle.platform.httpgateway.filter.tracking;

import org.cradle.disruptor.workerpool.WorkEvent;
import org.cradle.disruptor.workerpool.WorkerPoolOperation;
import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.exception.UnknownResourceException;
import org.cradle.platform.httpgateway.filter.BasicFilter;
import org.cradle.repository.ModelRepository;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 11, 2015
 */
public class TrackingFilter extends BasicFilter {

	private ModelRepository modelRepository;
	private WorkerPoolOperation<WorkEvent> workerPool;
	private TrackingFilterFactory factory;
	
	public TrackingFilter(
			ModelRepository modelRepository,
			WorkerPoolOperation<WorkEvent> workerPool, 
			TrackingFilterFactory factory) {
		
		this.modelRepository = modelRepository;
		
		this.workerPool = workerPool;
		
		this.factory = factory;
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.BasicRESTfulFilter#doFilter(org.cradle.gateway.HttpAdapter)
	 */
	@Override
	protected void doFilter(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException,
			UnknownResourceException {
		
		workerPool.onData(new TrackingWorkEvent(modelRepository, httpAdapter, factory));
	}

}
