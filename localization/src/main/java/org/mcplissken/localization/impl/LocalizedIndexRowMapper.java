/**
 * 
 */
package org.mcplissken.localization.impl;

import org.mcplissken.localization.Localized;
import org.mcplissken.repository.BasicRowMapper;
import org.mcplissken.repository.RowAdapter;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 28, 2015
 */
public class LocalizedIndexRowMapper extends BasicRowMapper<Localized> {

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.BasicRowMapper#map(com.mubasher.market.repository.RowAdapter)
	 */
	@Override
	public Localized map(RowAdapter row) {
		
		return new Localized(
				row.getString("id"), 
				row.getString("language"), 
				row.getString("language")
				);
	}

}
