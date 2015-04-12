/**
 * 
 */
package org.cradle.disruptor.workerpool;

import org.cradle.disruptor.DisruptorEvent;

import com.lmax.disruptor.WorkHandler;

/**
 * @author 	Sherief Shawky
 * @param <T>
 * @email 	mcrakens@gmail.com
 * @date 	Oct 1, 2014
 */
public abstract class WorkEventHandler<T> implements WorkHandler<DisruptorEvent<T>> {

	/* (non-Javadoc)
	 * @see com.lmax.disruptor.WorkHandler#onEvent(java.lang.Object)
	 */
	@Override
	public void onEvent(DisruptorEvent<T> event) throws Exception {

		T data = event.get();

		consume(data);
	}

	protected abstract void consume(T data) throws Exception;
}
