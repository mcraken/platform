/**
 * 
 */
package org.mcplissken.repository.index.solrj;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.mcplissken.repository.index.IndexException;
import org.mcplissken.repository.index.IndexPorter;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 19, 2014
 */
public class SolrjIndexPorter implements IndexPorter {

	private String[] fieldNames;
	private HttpSolrServer server;

	public SolrjIndexPorter(HttpSolrServer server, String[] fieldNames) {

		this.fieldNames = fieldNames;

		this.server = server; 

	}

	/* (non-Javadoc)
	 * @see com.mubasher.solrj.IndexPorter#port(java.lang.Object, java.lang.String[])
	 */
	public void port(Object target) throws IndexException {

		try {

			SolrInputDocument doc = new SolrInputDocument();

			for(String fieldName : fieldNames){

				doc.addField(fieldName, BeanUtils.getProperty(target, fieldName));
			}

			server.add(doc);

		} catch (Exception e) {

			throw new IndexException(e);
		}

	}

	/* (non-Javadoc)
	 * @see com.mubasher.solrj.IndexPorter#commit()
	 */
	public void commit() throws IndexException {
		
		try {
			
			server.commit();
			
		} catch (Exception e) {
			
			throw new IndexException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.mubasher.solrj.IndexPorter#shutdown()
	 */
	public void shutdown() {
		
		server.shutdown();
	}

	@Override
	public void delete(String id) throws IndexException{
			
		try {
			
			server.deleteById(id);
			
		} catch (Exception e) {
			
			throw new IndexException(e);
		}
		
		

	}

}
