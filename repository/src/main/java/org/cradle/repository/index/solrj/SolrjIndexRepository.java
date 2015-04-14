/**
 * 
 */
package org.cradle.repository.index.solrj;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.cradle.repository.BasicRowMapper;
import org.cradle.repository.IndexRepository;
import org.cradle.repository.index.Core;
import org.cradle.repository.index.CoreAnnotationIsNotPresent;
import org.cradle.repository.index.IndexException;
import org.cradle.repository.index.IndexPorter;
import org.cradle.repository.index.IndexQueryAdapter;
import org.cradle.repository.key.RestSearchKey;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public class SolrjIndexRepository implements IndexRepository{

	private String solrUrl;

	private Map<String, SolrjRowMapper<?>> rowMappers;

	private Map<String, HttpSolrServer> queryCores;

	/**
	 * @param solrUrl the solrUrl to set
	 */
	public void setSolrUrl(String solrUrl) {

		this.solrUrl = solrUrl;
	}

	public void init(){

		rowMappers = new HashMap<>();

		queryCores = new HashMap<>();

	}

	public <T> IndexPorter<T> indexPorter(Class<T> type) throws CoreAnnotationIsNotPresent {

		String coreName = getTargetCore(type);

		return new SolrjIndexPorter<T>(createServer(coreName));
	}

	@Override
	public <T> IndexPorter<T> indexPorter(Class<T> type, CoreLanguage language)
			throws CoreAnnotationIsNotPresent {

		String coreName = getTargetCore(type) + "_" + language.value;

		return new SolrjIndexPorter<T>(createServer(coreName));
	}

	@Override
	public <T> void registerIndexMapper(String modelName,
			BasicRowMapper<T> mapper) {

		rowMappers.put(modelName, new SolrjRowMapper<>(mapper));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> IndexQueryAdapter<T> queryAdapter(String coreName) {

		HttpSolrServer server = selectQueryCore(coreName);

		return new SolrjQueryAdapter<T>(server, (SolrjRowMapper<T>) rowMappers.get(coreName));
	}

	private HttpSolrServer createServer(String coreName) {

		String url = solrUrl + coreName;

		return new HttpSolrServer(url);

	}

	private String getTargetCore(Class<?> targetClass)
			throws CoreAnnotationIsNotPresent {

		if(targetClass.isAnnotationPresent(Core.class)){

			Annotation annotation = targetClass.getAnnotation(Core.class);

			Core core = (Core) annotation;

			return core.value();

		} else {

			throw new CoreAnnotationIsNotPresent();
		}
	}

	private HttpSolrServer selectQueryCore(String coreName){

		HttpSolrServer server = queryCores.get(coreName);

		if(server == null){

			server = createServer(coreName);

			queryCores.put(coreName, server);

		}

		return server;

	}

	@Override
	public <T> List<T> readIndex(RestSearchKey key) throws IndexException {
		return null;
	}
	
	@Override
	public <T> void writeIndex(final T model) throws IndexException {
		
		SinglePorter(model, new PortingCommand<T>() {

			@Override
			public void execute(T model, IndexPorter<T> porter) throws IndexException {
				
				porter.port(model);
			}
		});
	}
	
	@Override
	public <T> void writeIndex(List<T> models) throws IndexException {
		
		multiporter(models, new PortingCommand<T>() {

			@Override
			public void execute(T model, IndexPorter<T> porter)
					throws IndexException {
				
				porter.port(model);
			}
		});
	}
	
	@Override
	public <T> void deleteIndex(T model) throws IndexException {
		
		SinglePorter(model, new PortingCommand<T>() {

			@Override
			public void execute(T model, IndexPorter<T> porter)
					throws IndexException {
				
				porter.delete(model);
			}
			
		});
		
	}
	

	@Override
	public <T> void deleteIndex(List<T> models) throws IndexException {
		
		multiporter(models, new PortingCommand<T>() {

			@Override
			public void execute(T model, IndexPorter<T> porter)
					throws IndexException {
				
				porter.delete(model);
				
			}
		});
		
	}
	
	@SuppressWarnings("unchecked")
	private <T> void SinglePorter(T model, PortingCommand<T> portingCommand) throws IndexException{

		try {
			
			Class<T> type = (Class<T>) model.getClass();

			IndexPorter<T> porter;
			
			porter = indexPorter(type);
			
			portingCommand.execute(model, porter);
			
			porter.commit();

			porter.shutdown();
			
		} catch (CoreAnnotationIsNotPresent e) {
			
			throw new IndexException(e);
		}

	}
	
	@SuppressWarnings("unchecked")
	private <T> void multiporter(List<T> models, PortingCommand<T> portingCommand) throws IndexException {
		
		try {

			Class<T> type = (Class<T>) models.get(0).getClass();

			IndexPorter<T> porter = indexPorter(type);
			
			for(T model : models){
				
				portingCommand.execute(model, porter);
			}
			
			porter.commit();
			
			porter.shutdown();
			
		} catch (CoreAnnotationIsNotPresent e) {
			
			throw new IndexException(e);
		}
	}

}
