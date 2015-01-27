/**
 * 
 */
package org.mcplissken.repository.cassandra;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mcplissken.repository.RowAdapter;

import com.datastax.driver.core.Row;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 26, 2015
 */
public class CassandraRowAdapter implements RowAdapter {

	private Row row;
	
	public CassandraRowAdapter(Row row) {
		this.row = row;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.RowAdapter#getString(java.lang.String)
	 */
	@Override
	public String getString(String name) {
		
		return row.getString(name);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.RowAdapter#getInteger(java.lang.String)
	 */
	@Override
	public Integer getInt(String name) {
		
		return row.getInt(name);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.RowAdapter#getLong(java.lang.String)
	 */
	@Override
	public Long getLong(String name) {
		
		return row.getLong(name);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.RowAdapter#getList(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> List<T> getList(String name, Class<T> type) {
		
		return row.getList(name, type);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.RowAdapter#getMap(java.lang.String, java.lang.Class, java.lang.Class)
	 */
	@Override
	public <K, V> Map<K, V> getMap(String name, Class<K> keyType,
			Class<V> valueType) {
		
		return row.getMap(name, keyType, valueType);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.RowAdapter#getByte(java.lang.String)
	 */
	@Override
	public ByteBuffer getBytes(String name) {
		
		return row.getBytes(name);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.RowAdapter#getDouble(java.lang.String)
	 */
	@Override
	public Double getDouble(String name) {
		
		return row.getDouble(name);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.RowAdapter#getUUID(java.lang.String)
	 */
	@Override
	public UUID getUUID(String name) {
		
		return row.getUUID(name);
	}

}
