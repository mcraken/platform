/**
 * 
 */
package org.mcplissken.repository.cassandra.mappers;

import org.mcplissken.repository.BasicRowMapper;
import org.mcplissken.repository.RowAdapter;
import org.mcplissken.repository.models.tracking.TrackingLog;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 12, 2015
 */
public class TrackingLogRowMapper extends BasicRowMapper<TrackingLog> {

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.BasicRowMapper#map(org.mcplissken.repository.RowAdapter)
	 */
	@Override
	public TrackingLog map(RowAdapter row) {
	
		return new TrackingLog(
				row.getString("method"), 
				row.getString("resource_name"), 
				row.getList("resource_id", String.class), 
				row.getString("user_id"), 
				row.getLong("timing")
				);
	}

}
