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

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.solr.common.SolrDocument;
import org.mcplissken.repository.RowAdapter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 28, 2015
 */
public class SolrjRowAdapter implements RowAdapter {

	private SolrDocument document;
	
	public SolrjRowAdapter(SolrDocument document) {
		this.document = document;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.RowAdapter#getString(java.lang.String)
	 */
	@Override
	public String getString(String name) {
		
		return (String) document.getFieldValue(name);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.RowAdapter#getInt(java.lang.String)
	 */
	@Override
	public Integer getInt(String name) {
		return (Integer) document.getFieldValue(name);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.RowAdapter#getLong(java.lang.String)
	 */
	@Override
	public Long getLong(String name) {
		return (Long) document.getFieldValue(name);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.RowAdapter#getDouble(java.lang.String)
	 */
	@Override
	public Double getDouble(String name) {
		return (Double) document.getFieldValue(name);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.RowAdapter#getList(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> List<T> getList(String name, Class<T> type) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.RowAdapter#getMap(java.lang.String, java.lang.Class, java.lang.Class)
	 */
	@Override
	public <K, V> Map<K, V> getMap(String name, Class<K> keyType,
			Class<V> valueType) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.RowAdapter#getBytes(java.lang.String)
	 */
	@Override
	public ByteBuffer getBytes(String name) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.RowAdapter#getUUID(java.lang.String)
	 */
	@Override
	public UUID getUUID(String name) {
		throw new UnsupportedOperationException();
	}

}
