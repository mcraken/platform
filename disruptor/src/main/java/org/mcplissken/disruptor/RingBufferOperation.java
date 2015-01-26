/**
 * 
 */
package org.mcplissken.disruptor;

import com.lmax.disruptor.RingBuffer;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 1, 2014
 * @param <T>
 */
public class RingBufferOperation<T> implements OnDataCallBack<T>{

	protected RingBuffer<DisruptorEvent<T>> ringBuffer;

	/**
	 * 
	 */
	public RingBufferOperation() {
		
	}

	@Override
	public void onData(T data) {
		
		long sequence = 0;
	
		try {
	
			sequence = ringBuffer.next();  // Grab the next sequence
	
			// Get the entry in the Disruptor
			// for the sequence
			DisruptorEvent<T> event = ringBuffer.get(sequence);
			
			event.set(data);  // Fill with data
	
		}finally{
	
			ringBuffer.publish(sequence);
		}
	}

}