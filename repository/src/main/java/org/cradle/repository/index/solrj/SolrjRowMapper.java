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
package org.cradle.repository.index.solrj;

import org.apache.solr.common.SolrDocument;
import org.cradle.repository.BasicRowMapper;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 28, 2015
 */
public class SolrjRowMapper<T>  {
	
	private BasicRowMapper<T> rowMapper;

	public SolrjRowMapper(BasicRowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}
	
	public T mapDocument(SolrDocument document){
		
		return rowMapper.map(new SolrjRowAdapter(document));
	}
	
}
