/**
 * 
 */
package org.mcplissken.reporting;

import org.mcplissken.disruptor.workerpool.WorkEventHandler;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 3, 2014
 */
public class LoggingWorkHandler extends WorkEventHandler<LoggingEvent>  {

	/* (non-Javadoc)
	 * @see com.mubasher.disruptor.workerpool.WorkEventHandler#consume(java.lang.Object)
	 */
	@Override
	protected void consume(LoggingEvent data) throws Exception {
		data.execute();
	}


}
