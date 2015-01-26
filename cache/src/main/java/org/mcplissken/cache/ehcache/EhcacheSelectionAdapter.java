/**
 * 
 */
package org.mcplissken.cache.ehcache;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.mcplissken.cache.SelectionAdapter;

import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 4, 2014
 */
public class EhcacheSelectionAdapter implements SelectionAdapter{
	
	private Cache cache;
	private Query query;
	
	/**
	 * 
	 */
	public EhcacheSelectionAdapter(Cache cache)
	{
		this.cache = cache;
		
		query = cache.createQuery();
		
		query.includeKeys();
	}
	
	
	protected void doLike(Object value, Attribute<Object> attr) {

		query = query.addCriteria(attr.ilike((String) value));
	}

	protected void doEq(Object value, Attribute<Object> attr) {
		
		query = query.addCriteria(attr.eq(value));
	}
	
	protected void doIn(Object[] values, Attribute<Object> attr) {
		
		query = query.addCriteria(attr.in(Arrays.asList(values)));
	}
	
	protected void doMaxResults(int size) {
		
		query = query.maxResults(size);
	}
	
	protected void doOrderBy(boolean asc, Attribute<?> attr) {
		
		query = asc == true ? query.addOrderBy(attr, Direction.ASCENDING) : query.addOrderBy(attr, Direction.DESCENDING);
	}
	
	protected Map<Object, Object> map(List<Result> results) {
		
		Hashtable<Object, Object> all = new Hashtable<Object, Object>(results.size());

		Object key;

		for(Result result : results){

			key = result.getKey();

			all.put(key, cache.get(key).getObjectValue());
		}

		return all;
	}
	
	protected Attribute<Object> getAttribute(String name) {
		
		Attribute<Object> attr = cache.getSearchAttribute(name);
		
		return attr;
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
	 * @see org.mcplissken.cache.SelectionAdapter#result()
	 */
	@Override
	public Map<Object, Object> result() {
		
		return map(query.execute().all());
	}

}
