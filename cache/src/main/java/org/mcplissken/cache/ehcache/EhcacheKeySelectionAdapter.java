/**
 * 
 */
package org.mcplissken.cache.ehcache;

import org.mcplissken.cache.KeySelectionAdapter;
import org.mcplissken.cache.SelectionAdapter;

import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Attribute;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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
	 * @see com.mubasher.market.cache.SelectionAdapter#page(int)
	 */
	@Override
	public SelectionAdapter page(int size) {
		
		doMaxResults(size);
		
		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.cache.KeySelectionAdapter#eq(java.lang.Object)
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
	 * @see com.mubasher.market.cache.KeySelectionAdapter#in(java.lang.Object[])
	 */
	@Override
	public SelectionAdapter in(Object[] values) {
		
		doIn(values, key);
		
		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.cache.KeySelectionAdapter#orderBy(boolean)
	 */
	@Override
	public SelectionAdapter orderBy(boolean asc) {
		
		doOrderBy(asc, key);
		
		return this;
	}

}
