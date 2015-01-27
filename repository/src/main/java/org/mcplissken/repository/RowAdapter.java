/**
 * 
 */
package org.mcplissken.repository;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 26, 2015
 */
public interface RowAdapter {
	
	public String getString(String name);
	
	public Integer getInt(String name);
	
	public Long getLong(String name);
	
	public Double getDouble(String name);
	
	public  <T> List<T> getList(String name, Class<T> type);
	
	public <K, V> Map<K, V> getMap(String name, Class<K> keyType, Class<V> valueType);
	
	public ByteBuffer getBytes(String name);
	
	public UUID getUUID(String name);
}
