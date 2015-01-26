/**
 * 
 */
package org.mcplissken.repository.index.solrj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.mcplissken.repository.exception.NoResultException;
import org.mcplissken.repository.index.IndexDocumentObjectFactory;
import org.mcplissken.repository.index.IndexQueryAdapter;
import org.mcplissken.repository.index.QueryException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public class SolrjQueryAdapter<T> implements IndexQueryAdapter<T> {

	private String[] fieldNames;
	private HttpSolrServer server; 
	private SolrQuery solrQuery;
	private ArrayList<String> filterOprands;
	private String currentFilterFiled;
	private IndexDocumentObjectFactory<T> documentFactory;

	public SolrjQueryAdapter(HttpSolrServer server, String[] fieldNames, IndexDocumentObjectFactory<T> documentFactory) {

		this.fieldNames = fieldNames;
		this.server = server;
		this.documentFactory = documentFactory;

		solrQuery = new SolrQuery();

		filterOprands = new ArrayList<>();
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#queryAll()
	 */
	@Override
	public IndexQueryAdapter<T> queryAll() {

		solrQuery.setQuery("*:*");

		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#query(java.lang.String)
	 */
	@Override
	public IndexQueryAdapter<T> query(String terms) {

		solrQuery.setQuery(terms);

		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#filter(java.lang.String[])
	 */
	@Override
	public IndexQueryAdapter<T> filter(String filedName) {

		applyDanglingFilters();

		currentFilterFiled = filedName;

		return this;
	}

	private void applyDanglingFilters(){

		if(filterOprands.size() > 0){

			solrQuery.addFilterQuery(applyFilter("AND"));
		}
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#start(int)
	 */
	@Override
	public IndexQueryAdapter<T> start(Integer start) {

		solrQuery.setStart(start);

		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#page(int)
	 */
	@Override
	public IndexQueryAdapter<T> page(Integer count) {

		solrQuery.setRows(count);

		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#result()
	 */
	@Override
	public List<T> result() throws QueryException{

		try {

			applyDanglingFilters();

			QueryResponse response = server.query(solrQuery);

			SolrDocumentList docs = response.getResults();

			Iterator<SolrDocument> documentIterator = docs.iterator();

			SolrDocument resultDoc;
			
			T documentObj;
			
			ArrayList<T> result = new ArrayList<>();
			
			while(documentIterator.hasNext()){

				resultDoc = documentIterator.next();

				documentObj = documentFactory.createDocument();
				
				for(String field : fieldNames){
					
					BeanUtils.setProperty(documentObj, field, resultDoc.getFieldValue(field));
				}
				
				result.add(documentObj);
			}

			if(result.size() == 0){
				
				throw new NoResultException();
			}
			
			return result;

		} catch (Exception e) {

			throw new QueryException(e);
		}

	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#sort(java.lang.String, boolean)
	 */
	@Override
	public IndexQueryAdapter<T> sort(String fieldName, boolean asc) {

		solrQuery.addSort(fieldName, asc ? ORDER.asc : ORDER.desc);

		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#eq(java.lang.String)
	 */
	@Override
	public IndexQueryAdapter<T> eq(String value) {

		filterOprands.add(currentFilterFiled + ":" + value);

		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#gt(java.lang.String)
	 */
	@Override
	public IndexQueryAdapter<T> gt(String value) {

		filterOprands.add(currentFilterFiled + ":" + "{" + value + " TO *]");

		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#OR()
	 */
	@Override
	public IndexQueryAdapter<T> OR() {

		String filter = applyFilter("OR");

		solrQuery.addFilterQuery(filter);

		return this;
	}

	private String applyFilter(String operator) {

		String filter;

		if(filterOprands.size() > 1){

			filter = "(" + filterOprands.get(0);

			for(int i = 1; i < filterOprands.size(); i++){
				filter += " " + operator + " " + filterOprands.get(i);
			}

		} else {

			filter = filterOprands.get(0);
		}

		filterOprands.clear();

		return filter;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.solrj.SolrjQueryAdapter#AND()
	 */
	@Override
	public IndexQueryAdapter<T> and() {

		String filter = applyFilter("AND");

		solrQuery.addFilterQuery(filter);

		return this;
	}

}
