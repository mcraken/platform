/**
 * 
 */
package org.mcplissken.repository.cassandra.mappers;

import org.mcplissken.repository.BasicRowMapper;
import org.mcplissken.repository.RowAdapter;
import org.mcplissken.repository.models.account.Oauth;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 19, 2015
 */
public class OauthRowMapper extends BasicRowMapper<Oauth> {

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.BasicRowMapper#map(org.mcplissken.repository.RowAdapter)
	 */
	@Override
	public Oauth map(RowAdapter row) {
		
		return new Oauth(
				row.getString("email"),
				row.getString("oauth_provider"), 
				row.getString("oauth_token"), 
				row.getString("secret"), 
				row.getString("raw_response"),
				row.getLong("last_access")
				);
	}

}
