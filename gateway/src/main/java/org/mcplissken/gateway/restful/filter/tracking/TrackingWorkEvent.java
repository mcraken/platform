/**
 * 
 */
package org.mcplissken.gateway.restful.filter.tracking;

import java.util.List;

import org.mcplissken.disruptor.workerpool.WorkEvent;
import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.repository.ModelRepository;
import org.mcplissken.repository.models.RestModel;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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
	 * @see com.mubasher.disruptor.workerpool.WorkEvent#execute()
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