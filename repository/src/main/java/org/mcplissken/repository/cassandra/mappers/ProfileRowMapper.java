/**
 * 
 */
package org.mcplissken.repository.cassandra.mappers;

import org.mcplissken.repository.BasicRowMapper;
import org.mcplissken.repository.RowAdapter;
import org.mcplissken.repository.models.account.Profile;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 15, 2015
 */
public class ProfileRowMapper extends BasicRowMapper<Profile> {

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.BasicRowMapper#map(org.mcplissken.repository.RowAdapter)
	 */
	@Override
	public Profile map(RowAdapter row) {
		
		return new Profile(
				row.getString("email"), 
				row.getString("first_name"), 
				row.getString("last_name")
				);
	}

}
