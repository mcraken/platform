/**
 *  Copyright mcplissken.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cradle.localization.impl;

import org.cradle.localization.Localized;
import org.cradle.repository.BasicRowMapper;
import org.cradle.repository.RowAdapter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
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
