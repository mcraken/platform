/**
 * 
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
 * @email 	sherif.shawki@mubasher.info
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
	 * @see com.mubasher.gateway.restful.filter.BasicRESTfulFilter#doFilter(com.mubasher.gateway.HttpAdapter)
	 */
	@Override
	protected void doFilter(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException,
			UnknownResourceException {
		
		workerPool.onData(new TrackingWorkEvent(modelRepository, httpAdapter, factory));
	}

}
