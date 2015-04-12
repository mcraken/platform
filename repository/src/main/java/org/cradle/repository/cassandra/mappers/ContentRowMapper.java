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
package org.cradle.repository.cassandra.mappers;

import org.cradle.repository.BasicRowMapper;
import org.cradle.repository.RowAdapter;
import org.cradle.repository.models.content.Content;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 30, 2014
 */
public class ContentRowMapper extends BasicRowMapper<Content>{

	/* (non-Javadoc)
	 * @see org.cradle.repository.BasicRowMapper#map(org.cradle.repository.RowAdapter)
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
