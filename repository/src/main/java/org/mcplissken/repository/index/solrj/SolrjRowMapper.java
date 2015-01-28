/**
 * 
 */
package org.mcplissken.repository.index.solrj;

import org.apache.solr.common.SolrDocument;
import org.mcplissken.repository.BasicRowMapper;


/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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
