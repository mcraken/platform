/**
 * 
 */
package org.mcplissken.disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jul 6, 2014
 */
public class DisruptorOperation<T> extends RingBufferOperation<T> {

	public static final String BLOCKING = "blocking";
	public static final String SLEEPING = "sleeping";
	public static final String YIELD = "yield";
	public static final String BUSY_SPIN = "busySpin";

	protected Disruptor<DisruptorEvent<T>> disruptor;
	protected DisruptorEventHandler<T> handlers[];

	// Specify the size of the ring buffer, must be power of 2.
	protected int bufferSize;
	protected WaitStrategy waitStrategy;

	@SuppressWarnings("unchecked")
	public DisruptorOperation(int bufferSize,
			String waitStrategyName,
			DisruptorEventHandler<T>... handlers) {

		this.handlers = handlers;
		this.bufferSize = bufferSize;

		initWaitStrategy(waitStrategyName);
	}

	/**
	 * @param waitStrategy2
	 */
	private void initWaitStrategy(String waitStrategyName) {

		switch(waitStrategyName){

		case BLOCKING :
			waitStrategy = new BlockingWaitStrategy();
			break;	

		case SLEEPING :
			waitStrategy = new SleepingWaitStrategy();
			break;	

		case YIELD :
			waitStrategy = new YieldingWaitStrategy();
			break;	

		case BUSY_SPIN :
			waitStrategy = new BusySpinWaitStrategy();
			break;	
		}
	}

	public void start(){

		Executor executor = Executors.newCachedThreadPool();

		DisruptorEventFactory<T> factory = new DisruptorEventFactory<T>();

		// Construct the Disruptor
		disruptor = 
				new Disruptor<DisruptorEvent<T>>(
						factory, 
						bufferSize,
						executor,
						ProducerType.SINGLE,
						new SleepingWaitStrategy()
						);
		
		disruptor.handleEventsWith(handlers);

		ringBuffer = disruptor.getRingBuffer();

		// Start the Disruptor, starts all threads running
		disruptor.start();
	}

	public void stop(){

		disruptor.shutdown();
	}

}
