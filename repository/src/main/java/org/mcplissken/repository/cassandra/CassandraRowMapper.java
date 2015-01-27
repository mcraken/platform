/**
 * 
 */
package org.mcplissken.repository.cassandra;

import org.mcplissken.repository.BasicRowMapper;
import org.springframework.cassandra.core.RowMapper;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 26, 2015
 */
public class CassandraRowMapper<T> implements RowMapper<T>{

	private BasicRowMapper<T> rowMapper;
	
	public CassandraRowMapper(BasicRowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}

	/* (non-Javadoc)
	 * @see org.springframework.cassandra.core.RowMapper#mapRow(com.datastax.driver.core.Row, int)
	 */
	@Override
	public T mapRow(Row row, int rowNum) throws DriverException {
		
		return rowMapper.map(new CassandraRowAdapter(row));
	}

}
