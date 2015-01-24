/**
 * 
 */
package org.mcplissken.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jul 6, 2014
 */
public abstract class DisruptorEventHandler<T> implements EventHandler<DisruptorEvent<T>> {

	/* (non-Javadoc)
	 * @see com.lmax.disruptor.EventHandler#onEvent(java.lang.Object, long, boolean)
	 */
	public void onEvent(DisruptorEvent<T> event, long sequence,
			boolean endOfBatch) throws Exception {
		
		T data = event.get();
		
		consume(data);
		
	}

	/**
	 * @param data
	 */
	protected abstract void consume(T data);

}
