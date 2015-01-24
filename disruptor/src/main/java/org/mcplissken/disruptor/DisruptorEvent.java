/**
 * 
 */
package org.mcplissken.disruptor;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jul 6, 2014
 */
public class DisruptorEvent<T> {
	
	private T data;
	
	public T get(){
		return data;
	}
	
	public void set(T data){
		this.data = data;
	}
}
