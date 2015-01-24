/**
 * 
 */
package org.mcplissken.gateway.restful.filter.tracking;

import java.util.Map;

import org.mcplissken.disruptor.workerpool.SimpleWorkEventHandler;
import org.mcplissken.disruptor.workerpool.WorkEvent;
import org.mcplissken.disruptor.workerpool.WorkerPoolOperation;
import org.mcplissken.gateway.restful.filter.RESTfulFilter;
import org.mcplissken.gateway.restful.filter.RESTfulFilterFactory;
import org.mcplissken.repository.ModelRepository;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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
	 * @see com.mubasher.gateway.restful.filter.RESTfulFilterFactory#createFilter()
	 */
	@Override
	public RESTfulFilter createFilter() {
		
		return new RESTfulTrackingFilter(modelRepository, workerPool,  this);
	}

}
