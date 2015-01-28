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
package org.mcplissken.repository;

import java.util.List;

import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;
import org.mcplissken.repository.models.RestModel;
import org.mcplissken.repository.query.SimpleSelectionAdapter;
/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 20, 2014
 */

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 20, 2014
 */
public interface StructuredRepository {
	
	public <T> List<T> read(RestSearchKey key) throws InvalidCriteriaException;

	public void write(RestModel model);
	
	public void write(List<RestModel> entities);
	
	public void update(RestModel model);
	
	public void delete(RestModel model);

	public <T> SimpleSelectionAdapter<T> createSimpleSelectionAdapter(String modelName);
	
	public <T> void registerStructuredMapper(String modelName, BasicRowMapper<T> mapper);

}

