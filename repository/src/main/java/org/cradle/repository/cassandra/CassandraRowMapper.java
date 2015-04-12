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
package org.cradle.repository.cassandra;

import org.cradle.repository.BasicRowMapper;
import org.springframework.cassandra.core.RowMapper;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 26, 2015
 */
public class CassandraRowMapper<T> implements RowMapper<T>{

	private BasicRowMapper<T> rowMapper;
	
	public CassandraRowMapper(BasicRowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}

	/* (non-Javadoc)
	 * @see org.springframework.cassandra.core.RowMapper#mapRow(com.datastax.driver.core.Row, int)
	 */
	@Override
	public T mapRow(Row row, int rowNum) throws DriverException {
		
		return rowMapper.map(new CassandraRowAdapter(row));
	}

}
