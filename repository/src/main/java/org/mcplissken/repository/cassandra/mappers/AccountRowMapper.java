/**
 * 
 */
package org.mcplissken.repository.cassandra.mappers;

import org.mcplissken.repository.BasicRowMapper;
import org.mcplissken.repository.RowAdapter;
import org.mcplissken.repository.models.account.Account;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 9, 2014
 */
public class AccountRowMapper extends BasicRowMapper<Account> {

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.BasicRowMapper#map(org.mcplissken.repository.RowAdapter)
	 */
	@Override
	public Account map(RowAdapter row) {
		
		return new Account(
				row.getString("email"), 
				row.getString("password"), 
				row.getList("roles", String.class)
				);
	}

}
