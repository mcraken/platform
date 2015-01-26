/**
 * 
 */
package org.mcplissken.repository.index.solrj;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.mcplissken.repository.IndexRepository;
import org.mcplissken.repository.index.IndexDocumentObjectFactory;
import org.mcplissken.repository.index.IndexPorter;
import org.mcplissken.repository.index.IndexQueryAdapter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public class SolrjIndexRepository implements IndexRepository{

	private String solrUrl;
	private Map<String, String> cores;
	private Map<String, IndexPorter> corePorters;
	
	/**
	 * @param corePorters the corePorters to set
	 */
	public void setCores(Map<String, String> cores) {
		this.cores = cores;
	}
	
	/**
	 * @param solrUrl the solrUrl to set
	 */
	public void setSolrUrl(String solrUrl) {
		
		this.solrUrl = solrUrl;
	}
	
	public void init(){
		
		String[] coreNames = cores.keySet().toArray(new String[]{});
		
		String[] fields = null;
		
		corePorters = new HashMap<String, IndexPorter>();
		
		for(String coreName : coreNames){
			
			fields = cores.get(coreName).split(",");
			
			corePorters.put(coreName, new SolrjIndexPorter(createServer(coreName), fields));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjService#indexPorter()
	 */
	public IndexPorter indexPorter(String coreName) {
		
		return corePorters.get(coreName);
	}

	private HttpSolrServer createServer(String coreName) {
		
		String url = solrUrl + coreName;
		
		HttpSolrServer server = new HttpSolrServer(url);
		
		return server;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjService#queryAdapter(java.lang.String, org.mcplissken.solrj.SolrDocumentObjectFactory, java.lang.String[])
	 */
	@Override
	public <T> IndexQueryAdapter<T> queryAdapter(String coreName,
			IndexDocumentObjectFactory<T> documentFactory, String[] fieldNames) {
		
		HttpSolrServer server = createServer(coreName);
		
		return new SolrjQueryAdapter<T>(server, fieldNames, documentFactory);
	}

}
