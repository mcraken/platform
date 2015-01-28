/**
 * 
 */
package org.mcplissken.repository.index.solrj;


import java.lang.reflect.Field;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.mcplissken.repository.index.Index;
import org.mcplissken.repository.index.IndexException;
import org.mcplissken.repository.index.IndexPorter;
import org.mcplissken.repository.index.TargetHasNoIndexableFileds;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public class SolrjIndexPorter<T> implements IndexPorter<T> {

	private HttpSolrServer server;

	public SolrjIndexPorter(HttpSolrServer server) {
		this.server = server;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.solrj.IndexPorter#port(java.lang.Object, java.lang.String[])
	 */
	public void port(Object target) throws IndexException {

		try {
			
			SolrInputDocument doc = new SolrInputDocument();
			
			Class<?> targetClass = target.getClass();
			
			indexFields(target, doc, targetClass);
			
			if(!doc.isEmpty()){
				
				server.add(doc);
				
			} else {
				
				throw new TargetHasNoIndexableFileds();
			}

		} catch (Exception e) {

			throw new IndexException(e);
		}

	}

	private void indexFields(Object target, SolrInputDocument doc,
			Class<?> targetClass) throws IllegalAccessException {
		
		Field[] fields = targetClass.getDeclaredFields();
		
		for(Field field : fields){
			
			if(field.isAnnotationPresent(Index.class)){
				
				doc.addField(field.getName(), field.get(target));
			}
			
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
