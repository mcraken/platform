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
package org.mcplissken.cache.ehcache;

import org.mcplissken.cache.KeySelectionAdapter;
import org.mcplissken.cache.SelectionAdapter;

import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Attribute;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 4, 2014
 */
public class EhcacheKeySelectionAdapter extends EhcacheSelectionAdapter implements KeySelectionAdapter {
	
	private Attribute<Object> key;
	
	/**
	 * @param cache
	 */
	public EhcacheKeySelectionAdapter(Cache cache) {
		super(cache);
		
		key = new Attribute<Object>("key");
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.cache.SelectionAdapter#page(int)
	 */
	@Override
	public SelectionAdapter page(int size) {
		
		doMaxResults(size);
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.cache.KeySelectionAdapter#eq(java.lang.Object)
	 */
	@Override
	public SelectionAdapter eq(Object value) {
		
		doEq(value, key);
		
		return this;
	}
	
	@Override
	public SelectionAdapter like(Object value) {
		
		doLike(value, key);
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.cache.KeySelectionAdapter#in(java.lang.Object[])
	 */
	@Override
	public SelectionAdapter in(Object[] values) {
		
		doIn(values, key);
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.cache.KeySelectionAdapter#orderBy(boolean)
	 */
	@Override
	public SelectionAdapter orderBy(boolean asc) {
		
		doOrderBy(asc, key);
		
		return this;
	}

}
