/**
 * 
 */
package org.mcplissken.disruptor.workerpool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.mcplissken.disruptor.DisruptorEvent;
import org.mcplissken.disruptor.DisruptorEventFactory;
import org.mcplissken.disruptor.RingBufferOperation;

import com.lmax.disruptor.WorkerPool;


/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 1, 2014
 */
public class WorkerPoolOperation<T> extends RingBufferOperation<T>  {

	private WorkerPool<DisruptorEvent<T>> workerPool;
	private DisruptorEventFactory<T> factory;
	private WorkerPoolExceptionHandler exceptionHandler;

	@SuppressWarnings("unchecked")
	public WorkerPoolOperation(WorkEventHandler<T>... workHandlers){

		factory = new DisruptorEventFactory<>();

		exceptionHandler = new WorkerPoolExceptionHandler();

		workerPool = new WorkerPool<DisruptorEvent<T>>(
				factory, 
				exceptionHandler, 
				workHandlers);

	}

	public void start(){

		Executor executor = Executors.newCachedThreadPool();

		ringBuffer = workerPool.start(executor);
	}

	public void stop(){

		workerPool.drainAndHalt();
	}

}
