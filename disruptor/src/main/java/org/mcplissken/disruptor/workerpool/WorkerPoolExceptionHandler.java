/**
 * 
 */
package org.mcplissken.disruptor.workerpool;

import com.lmax.disruptor.ExceptionHandler;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 1, 2014
 */
public class WorkerPoolExceptionHandler implements ExceptionHandler {

	/* (non-Javadoc)
	 * @see com.lmax.disruptor.ExceptionHandler#handleEventException(java.lang.Throwable, long, java.lang.Object)
	 */
	@Override
	public void handleEventException(Throwable ex, long sequence, Object event) {
		
		printException("Worker pool exception occured at sequence " + sequence, ex);
		
	}

	/* (non-Javadoc)
	 * @see com.lmax.disruptor.ExceptionHandler#handleOnStartException(java.lang.Throwable)
	 */
	@Override
	public void handleOnStartException(Throwable ex) {
		printException("Worker pool start failure", ex);
	}

	/* (non-Javadoc)
	 * @see com.lmax.disruptor.ExceptionHandler#handleOnShutdownException(java.lang.Throwable)
	 */
	@Override
	public void handleOnShutdownException(Throwable ex) {
		printException("Worker pool shutdown failure", ex);
	}
	
	private void printException(String message, Throwable e){
		
		System.out.println(message);
		
		e.printStackTrace();
		
	}

}
