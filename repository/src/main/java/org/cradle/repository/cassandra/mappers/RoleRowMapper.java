/**
 * 
 */
package org.cradle.repository.cassandra.mappers;

import org.cradle.repository.BasicRowMapper;
import org.cradle.repository.RowAdapter;
import org.cradle.repository.models.account.Role;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public class RoleRowMapper extends BasicRowMapper<Role>{

	/* (non-Javadoc)
	 * @see org.cradle.repository.BasicRowMapper#map(org.cradle.repository.RowAdapter)
	 */
	@Override
	public Role map(RowAdapter row) {
		return new Role(
				row.getString("name"),
				row.getList("permissions", String.class)
				);
	}

}
