/**
 * 
 */
package org.mcplissken.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jul 6, 2014
 */
public class DisruptorEventFactory<T> implements EventFactory<DisruptorEvent<T>> {

	/* (non-Javadoc)
	 * @see com.lmax.disruptor.EventFactory#newInstance()
	 */
	public DisruptorEvent<T> newInstance() {
		return createInstance();
	}
	
	protected DisruptorEvent<T> createInstance(){
		return new DisruptorEvent<T>();
	}

}
