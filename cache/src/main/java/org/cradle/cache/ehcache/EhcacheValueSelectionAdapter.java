/**
 * 
 */
package org.cradle.cache.ehcache;

import org.cradle.cache.ValueSelectionAdapter;

import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Attribute;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 3, 2014
 */
public class EhcacheValueSelectionAdapter extends EhcacheSelectionAdapter implements ValueSelectionAdapter{
	
	/**
	 * @param cache
	 */
	public EhcacheValueSelectionAdapter(Cache cache) {
		super(cache);
	}



	/* (non-Javadoc)
	 * @see org.cradle.cache.ValueSelectionAdapter#eq(java.lang.String, java.lang.Object)
	 */
	@Override
	public ValueSelectionAdapter eq(String name, Object value) {
		
		Attribute<Object> attr = getAttribute(name);
		
		doEq(value, attr);
		
		return this;
	}


	/* (non-Javadoc)
	 * @see org.cradle.cache.ValueSelectionAdapter#in(java.lang.String, java.lang.Object[])
	 */
	@Override
	public ValueSelectionAdapter in(String name, Object[] values) {
		
		Attribute<Object> attr = getAttribute(name);
		
		doIn(values, attr);
		
		return this;
	}

	
	/* (non-Javadoc)
	 * @see org.cradle.cache.ValueSelectionAdapter#orderBy(boolean)
	 */
	@Override
	public ValueSelectionAdapter orderBy(boolean asc, String name) {
		
		Attribute<Object> attr = getAttribute(name);
		
		doOrderBy(asc, attr);
		
		return this;
	}
	

}
