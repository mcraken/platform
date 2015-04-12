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
package org.cradle.gateway.restful.filter.tracking;

import java.util.Map;

import org.cradle.disruptor.workerpool.SimpleWorkEventHandler;
import org.cradle.disruptor.workerpool.WorkEvent;
import org.cradle.disruptor.workerpool.WorkerPoolOperation;
import org.cradle.gateway.restful.filter.RESTfulFilter;
import org.cradle.gateway.restful.filter.RESTfulFilterFactory;
import org.cradle.repository.ModelRepository;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 11, 2015
 */
public class RESTfulTrackingFilterFactory implements RESTfulFilterFactory{

	private int concurrency;
	private WorkerPoolOperation<WorkEvent> workerPool;
	private ModelRepository modelRepository;
	private Map<String, TrackingLogCreator> logCreators;
	
	/**
	 * @param logCreators the logCreators to set
	 */
	public void setLogCreators(Map<String, TrackingLogCreator> logCreators) {
		this.logCreators = logCreators;
	}
	
	/**
	 * @param concurrency the concurrency to set
	 */
	public void setConcurrency(int concurrency) {
		
		this.concurrency = concurrency;
	}
	
	/**
	 * @param modelRepository the modelRepository to set
	 */
	public void setModelRepository(ModelRepository modelRepository) {
		
		this.modelRepository = modelRepository;
	}
	
	public void init(){
		
		SimpleWorkEventHandler[] eventHandlers = new SimpleWorkEventHandler[concurrency];
		
		for(int i = 0; i < concurrency; i++){
			
			eventHandlers[i] = new SimpleWorkEventHandler();
		}
		
		workerPool = new WorkerPoolOperation<>(eventHandlers);
		
		workerPool.start();
		
	}
	
	public void destroy(){
		
		workerPool.stop();
	}
	
	public TrackingLogCreator getLogCreator(String method){
		
		return logCreators.get(method);
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.RESTfulFilterFactory#createFilter()
	 */
	@Override
	public RESTfulFilter createFilter() {
		
		return new RESTfulTrackingFilter(modelRepository, workerPool,  this);
	}

}
