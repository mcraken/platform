/**
 * 
 */
package org.mcplissken.disruptor.workerpool;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 11, 2015
 */
public class SimpleWorkEventHandler extends WorkEventHandler<WorkEvent>{

	/* (non-Javadoc)
	 * @see com.mubasher.disruptor.workerpool.WorkEventHandler#consume(java.lang.Object)
	 */
	@Override
	protected void consume(WorkEvent event) throws Exception {
		
		event.execute();
	}

}
