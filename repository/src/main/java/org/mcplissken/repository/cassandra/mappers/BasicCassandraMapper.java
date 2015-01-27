/**
 * 
 */
package org.mcplissken.repository.cassandra.mappers;

import org.springframework.cassandra.core.RowMapper;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 26, 2015
 */
public class BasicCassandraMapper<T> implements RowMapper<T> {

	/* (non-Javadoc)
	 * @see org.springframework.cassandra.core.RowMapper#mapRow(com.datastax.driver.core.Row, int)
	 */
	@Override
	public T mapRow(Row row, int rowNum) throws DriverException {
		// TODO Auto-generated method stub
		return null;
	}

}
