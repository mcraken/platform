/**
 * 
 */
package org.mcplissken.repository.cassandra.mappers;

import org.mcplissken.repository.BasicRowMapper;
import org.mcplissken.repository.RowAdapter;
import org.mcplissken.repository.models.content.Content;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 30, 2014
 */
public class ContentRowMapper extends BasicRowMapper<Content>{

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.BasicRowMapper#map(org.mcplissken.repository.RowAdapter)
	 */
	@Override
	public Content map(RowAdapter row) {
		
		return new Content(
				row.getString("id"),
				row.getString("name"),
				row.getString("type"),
				row.getMap("metainfo", String.class, String.class),
				row.getBytes("data")
			);
	}



}
