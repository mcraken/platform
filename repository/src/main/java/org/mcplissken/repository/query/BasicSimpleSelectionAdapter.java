/**
 * 
 */
package org.mcplissken.repository.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 31, 2014
 */
public abstract class BasicSimpleSelectionAdapter<T, O> implements SimpleSelectionAdapter {

	private ArrayList<T> criteria;

	/**
	 * 
	 */
	public BasicSimpleSelectionAdapter() {

		criteria = new ArrayList<>();
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.query.SimpleSelectionAdapter#eq(java.lang.String, java.lang.Object)
	 */
	@Override
	public SimpleSelectionAdapter eq(String name, Object value) {

		criteria.add(doEq(name, value));

		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.query.SimpleSelectionAdapter#gt(java.lang.String, java.lang.Object)
	 */
	@Override
	public SimpleSelectionAdapter gt(String name, Object value) {

		criteria.add(doGt(name, value));

		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.query.SimpleSelectionAdapter#gte(java.lang.String, java.lang.Object)
	 */
	@Override
	public SimpleSelectionAdapter gte(String name, Object value) {
		
		criteria.add(doGte(name, value));

		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.query.SimpleSelectionAdapter#and()
	 */
	@Override
	public SimpleSelectionAdapter and() {

		doAnd(criteria.iterator());

		criteria.clear();

		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.query.SimpleSelectionAdapter#or()
	 */
	@Override
	public SimpleSelectionAdapter or() {

		doOr(criteria.iterator());

		criteria.clear();

		return this;

	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.query.SimpleSelectionAdapter#page(int)
	 */
	@Override
	public SimpleSelectionAdapter page(int size) {

		if(criteria.size() > 0){
			doLimit(criteria.get(0), size);
		} else {
			doLimit(size);
		}

		return this;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.query.SimpleSelectionAdapter#orderBy(boolean, java.lang.String[])
	 */
	@Override
	public SimpleSelectionAdapter orderBy(boolean asc, String... columns) {

		ArrayList<O> orderings = new ArrayList<O>(columns.length);

		for(int i = 0; i < columns.length; i++){
			orderings.add(order(asc, columns[i])); 
		}
		
		orderBy(orderings);
		
		return this;
	}

	@Override
	public SimpleSelectionAdapter lt(String name, Object value){
		
		criteria.add(doLt(name, value));
		
		return this;
	}
	
	protected Iterator<T> criteria(){
		
		return criteria.iterator();
	}
	
	protected abstract T doEq(String name, Object value);

	protected abstract T doGt(String name, Object value);

	protected abstract T doGte(String name, Object value);
	
	protected abstract O order(boolean asc, String column);
	
	protected abstract void orderBy(List<O> orderings);
	
	protected abstract void doAnd(Iterator<T> clauses);

	protected abstract void doOr(Iterator<T> clauses);

	protected abstract void doLimit(T clause, int size);

	protected abstract void doLimit(int size);
	
	protected abstract T doLt(String name, Object value);
}
