/**
 * 
 */
package org.cradle.repository.cassandra.mappers;

import org.cradle.repository.BasicRowMapper;
import org.cradle.repository.RowAdapter;
import org.cradle.repository.models.account.Account;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public class AccountRowMapper extends BasicRowMapper<Account> {

	/* (non-Javadoc)
	 * @see org.cradle.repository.BasicRowMapper#map(org.cradle.repository.RowAdapter)
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
