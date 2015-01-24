/**
 * 
 */
package org.mcplissken.repository;

import org.mcplissken.repository.index.IndexDocumentObjectFactory;
import org.mcplissken.repository.index.IndexPorter;
import org.mcplissken.repository.index.IndexQueryAdapter;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 19, 2014
 */
public interface IndexRepository {
	
	public IndexPorter indexPorter(String coreName);
	
	public <T> IndexQueryAdapter<T> queryAdapter(String coreName, IndexDocumentObjectFactory<T> documentFactory, String[] fieldNames);
}
