/**
 * 
 */
package org.cradle.reporting;

import org.cradle.disruptor.workerpool.WorkEventHandler;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 3, 2014
 */
public class LoggingWorkHandler extends WorkEventHandler<LoggingEvent>  {

	/* (non-Javadoc)
	 * @see org.cradle.disruptor.workerpool.WorkEventHandler#consume(java.lang.Object)
	 */
	@Override
	protected void consume(LoggingEvent data) throws Exception {
		data.execute();
	}


}
