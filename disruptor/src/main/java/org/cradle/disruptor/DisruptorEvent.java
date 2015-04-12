/**
 * 
 */
package org.cradle.disruptor;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
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
