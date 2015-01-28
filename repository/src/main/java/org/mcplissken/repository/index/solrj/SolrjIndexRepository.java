/**
 * 
 */
package org.mcplissken.repository.index.solrj;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.mcplissken.repository.BasicRowMapper;
import org.mcplissken.repository.IndexRepository;
import org.mcplissken.repository.index.Core;
import org.mcplissken.repository.index.CoreAnnotationIsNotPresent;
import org.mcplissken.repository.index.IndexPorter;
import org.mcplissken.repository.index.IndexQueryAdapter;

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
	
	/* (non-Javadoc)
	 * @see com.mubasher.solrj.SolrjService#indexPorter()
	 */
	public <T> IndexPorter<T> indexPorter(Class<T> type) throws CoreAnnotationIsNotPresent {
		
		String coreName = getTargetCore(type);
		
		return new SolrjIndexPorter<T>(createServer(coreName));
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.IndexRepository#indexPorter(java.lang.Class, com.mubasher.market.repository.IndexRepository.CoreLanguage)
	 */
	@Override
	public <T> IndexPorter<T> indexPorter(Class<T> type, CoreLanguage language)
			throws CoreAnnotationIsNotPresent {
		
		String coreName = getTargetCore(type) + "_" + language.value;
		
		return new SolrjIndexPorter<T>(createServer(coreName));
	}
	
	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.IndexRepository#registerIndexMapper(java.lang.String, com.mubasher.market.repository.BasicRowMapper)
	 */
	@Override
	public <T> void registerIndexMapper(String modelName,
			BasicRowMapper<T> mapper) {
		
		rowMappers.put(modelName, new SolrjRowMapper<>(mapper));
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.IndexRepository#queryAdapter(java.lang.String)
	 */
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

}
