/**
 * 
 */
package org.mcplissken.repository.cassandra.mappers;

import org.mcplissken.repository.models.content.Content;
import org.springframework.cassandra.core.RowMapper;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 30, 2014
 */
public class ContentRowMapper implements RowMapper<Content>{

	/* (non-Javadoc)
	 * @see org.springframework.cassandra.core.RowMapper#mapRow(com.datastax.driver.core.Row, int)
	 */
	@Override
	public Content mapRow(Row row, int count) throws DriverException {
		
		return new Content(
					row.getString("id"),
					row.getString("name"),
					row.getString("type"),
					row.getMap("metainfo", String.class, String.class),
					row.getBytes("data")
				);
	}

}
