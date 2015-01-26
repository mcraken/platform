/**
 * 
 */
package org.mcplissken.disruptor.workerpool;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 11, 2015
 */
public class SimpleWorkEventHandler extends WorkEventHandler<WorkEvent>{

	/* (non-Javadoc)
	 * @see org.mcplissken.disruptor.workerpool.WorkEventHandler#consume(java.lang.Object)
	 */
	@Override
	protected void consume(WorkEvent event) throws Exception {
		
		event.execute();
	}

}
