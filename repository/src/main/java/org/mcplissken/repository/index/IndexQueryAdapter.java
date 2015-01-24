/**
 * 
 */
package org.mcplissken.repository.index;

import java.util.List;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 19, 2014
 */
public interface IndexQueryAdapter<T> {
	
	public IndexQueryAdapter<T> queryAll();
	
	public IndexQueryAdapter<T> query(String terms);
	
	public IndexQueryAdapter<T> filter(String fieldName);
	
	public IndexQueryAdapter<T> eq(String value);
	
	public IndexQueryAdapter<T> gt(String value);
	
	public IndexQueryAdapter<T> OR();
	
	public IndexQueryAdapter<T> and();
	
	public IndexQueryAdapter<T> start(Integer start);
	
	public IndexQueryAdapter<T> page(Integer count);
	
	public IndexQueryAdapter<T> sort(String fieldName, boolean asc);
	
	public List<T> result() throws QueryException;
}
