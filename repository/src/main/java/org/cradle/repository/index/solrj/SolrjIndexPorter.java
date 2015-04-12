/**
 * 
 */
package org.cradle.repository.index.solrj;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.cradle.repository.index.Index;
import org.cradle.repository.index.IndexException;
import org.cradle.repository.index.IndexId;
import org.cradle.repository.index.IndexPorter;
import org.cradle.repository.index.TargetHasNoIndexableFileds;

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
			Class<?> targetClass) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Field[] fields = targetClass.getDeclaredFields();

		String fieldName = null; 
		
		for(Field field : fields){

			if(field.isAnnotationPresent(Index.class)){
				
				fieldName = field.getName();
				
				doc.addField(fieldName, BeanUtils.getProperty(target, fieldName));
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

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.index.IndexPorter#delete(java.lang.Object)
	 */
	@Override
	public void delete(T model) throws IndexException {
		
		try {
			
			Class<?> targetClass = model.getClass();

			Field[] fields = targetClass.getDeclaredFields();
			
			for(Field field : fields){

				if(field.isAnnotationPresent(IndexId.class)){
					
					server.deleteById((String) BeanUtils.getProperty(model, field.getName()));
				}

			}
			
		} catch (IllegalArgumentException | IllegalAccessException
				| SolrServerException | IOException | InvocationTargetException | NoSuchMethodException e) {
			
			throw new IndexException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.index.IndexPorter#deleteById(java.lang.String)
	 */
	@Override
	public void deleteById(String id) throws IndexException {
		
		try {
			server.deleteById(id);
		} catch (SolrServerException | IOException e) {
			
			throw new IndexException(e);
		}
	}

}

