/**
 * 
 */
package org.mcplissken.disruptor.workerpool;

import java.util.Random;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 1, 2014
 */
public class WorkerpoolTest {

	public static class DemoEvent{
		int id = new Random().nextInt(); 
	}
	
	public static class DemoWorkEventHandler extends WorkEventHandler<DemoEvent>{
		private int id = new Random().nextInt();
		
		@Override
		protected void consume(DemoEvent data) throws InterruptedException {
			Thread.sleep(1000);
			System.out.println(id + " is consuming " + data.id);
			
		}
		
	}
	
	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		
		WorkerPoolOperation<DemoEvent> workerPoolOperation = 
				new WorkerPoolOperation<DemoEvent>(
						new DemoWorkEventHandler(),
						new DemoWorkEventHandler(),
						new DemoWorkEventHandler(),
						new DemoWorkEventHandler(),
						new DemoWorkEventHandler()
						);
		
		workerPoolOperation.start();
		
		for(int i=0; i < 5; i++)
			workerPoolOperation.onData(new DemoEvent());
		
		workerPoolOperation.stop();
		
		System.out.println("Work done!");
		
		System.out.println("+++++++++++++++++++++++++++++++++++");
		
		workerPoolOperation.start();
		
		for(int i=0; i < 5; i++)
			workerPoolOperation.onData(new DemoEvent());
		
		workerPoolOperation.stop();
		
		System.out.println("Work done!");
		
	}

}
