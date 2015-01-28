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
package org.mcplissken.repository.index.solrj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.mcplissken.repository.exception.NoResultException;
import org.mcplissken.repository.index.IndexQueryAdapter;
import org.mcplissken.repository.index.QueryException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public class SolrjQueryAdapter<T> implements IndexQueryAdapter<T> {

	private HttpSolrServer server; 
	private SolrQuery solrQuery;
	private ArrayList<String> filterOprands;
	private String currentFilterFiled;
	private SolrjRowMapper<T> rowMapper;

	public SolrjQueryAdapter(HttpSolrServer server, SolrjRowMapper<T> rowAdapter) {

		this.server = server;

		this.rowMapper = rowAdapter;
		
		solrQuery = new SolrQuery();

		filterOprands = new ArrayList<>();
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#queryAll()
	 */
	@Override
	public IndexQueryAdapter<T> queryAll() {

		solrQuery.setQuery("*:*");

		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#query(java.lang.String)
	 */
	@Override
	public IndexQueryAdapter<T> query(String terms) {

		solrQuery.setQuery(terms);

		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#filter(java.lang.String[])
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
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#start(int)
	 */
	@Override
	public IndexQueryAdapter<T> start(Integer start) {

		solrQuery.setStart(start);

		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#page(int)
	 */
	@Override
	public IndexQueryAdapter<T> page(Integer count) {

		solrQuery.setRows(count);

		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#result()
	 */
	@Override
	public List<T> result() throws QueryException{

		try {

			applyDanglingFilters();

			QueryResponse response = server.query(solrQuery);

			SolrDocumentList docs = response.getResults();

			Iterator<SolrDocument> documentIterator = docs.iterator();

			SolrDocument resultDoc;
			
			ArrayList<T> result = new ArrayList<>();
			
			while(documentIterator.hasNext()){

				resultDoc = documentIterator.next();

				result.add(rowMapper.mapDocument(resultDoc));
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
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#sort(java.lang.String, boolean)
	 */
	@Override
	public IndexQueryAdapter<T> sort(String fieldName, boolean asc) {

		solrQuery.addSort(fieldName, asc ? ORDER.asc : ORDER.desc);

		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#eq(java.lang.String)
	 */
	@Override
	public IndexQueryAdapter<T> eq(String value) {

		filterOprands.add(currentFilterFiled + ":" + value);

		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#gt(java.lang.String)
	 */
	@Override
	public IndexQueryAdapter<T> gt(String value) {

		filterOprands.add(currentFilterFiled + ":" + "{" + value + " TO *]");

		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#OR()
	 */
	@Override
	public IndexQueryAdapter<T> or() {

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
	 * @see com.mubasher.market.solrj.SolrjQueryAdapter#AND()
	 */
	@Override
	public IndexQueryAdapter<T> and() {

		String filter = applyFilter("AND");

		solrQuery.addFilterQuery(filter);

		return this;
	}

}
