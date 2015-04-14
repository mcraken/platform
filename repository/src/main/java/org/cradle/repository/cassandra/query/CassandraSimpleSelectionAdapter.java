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
package org.cradle.repository.cassandra.query;

import java.util.Iterator;
import java.util.List;

import org.cradle.repository.cassandra.CassandraRowMapper;
import org.cradle.repository.exception.NoResultException;
import org.cradle.repository.query.BasicSimpleSelectionAdapter;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Ordering;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 31, 2014
 */
public class CassandraSimpleSelectionAdapter<T> extends
		BasicSimpleSelectionAdapter<T, Clause, Ordering> {
	
	private Select select;
	private Where where;
	
	private CassandraTemplate cassandraTemplate;
	private CassandraRowMapper<T> rowMapper;
	/**
	 * 
	 */
	public CassandraSimpleSelectionAdapter(
			String modelName, 
			CassandraTemplate cassandraTemplate,
			CassandraRowMapper<T> rowMapper
			) {
		
		this.cassandraTemplate = cassandraTemplate;
		
		this.rowMapper = rowMapper;
		
		select = QueryBuilder.select().from(modelName);
		
		where = select.where();
		
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.repository.query.BasicSimpleSelectionAdapter#doEq(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Clause doEq(String name, Object value) {
		return QueryBuilder.eq(name, value);
	}

	/* (non-Javadoc)
	 * @see org.cradle.repository.query.BasicSimpleSelectionAdapter#doGt(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Clause doGt(String name, Object value) {
		return QueryBuilder.gt(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.repository.query.BasicSimpleSelectionAdapter#doGte(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Clause doGte(String name, Object value) {
		return QueryBuilder.gte(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.repository.query.BasicSimpleSelectionAdapter#doAnd(java.util.Iterator)
	 */
	@Override
	protected void doAnd(Iterator<Clause> clauses) {
		
		while(clauses.hasNext()){
			where = where.and(clauses.next());
		}
		
	}

	/* (non-Javadoc)
	 * @see org.cradle.repository.query.BasicSimpleSelectionAdapter#doOr(java.util.Iterator)
	 */
	@Override
	protected void doOr(Iterator<Clause> clauses) {
		
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.repository.query.SimpleSelectionAdapter#result()
	 */
	@Override
	public List<T> result() throws NoResultException {
		
		doAnd(criteria());
		
		List<T> result = cassandraTemplate.query(select, rowMapper);
		
		if(result.size() == 0)
			throw new NoResultException();
		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cradle.repository.query.BasicSimpleSelectionAdapter#doLimit(java.lang.Object)
	 */
	@Override
	protected void doLimit(Clause clause, int size) {
		select = select.where(clause).limit(size);
	}

	/* (non-Javadoc)
	 * @see org.cradle.repository.query.BasicSimpleSelectionAdapter#doLimit(int)
	 */
	@Override
	protected void doLimit(int size) {
		
		select = where.limit(size);
	}

	/* (non-Javadoc)
	 * @see org.cradle.repository.query.BasicSimpleSelectionAdapter#order(boolean)
	 */
	@Override
	protected Ordering order(boolean asc, String column) {
		
		return asc == true ? QueryBuilder.asc(column) : QueryBuilder.desc(column);
	}

	/* (non-Javadoc)
	 * @see org.cradle.repository.query.BasicSimpleSelectionAdapter#orderBy(java.util.List)
	 */
	@Override
	protected void orderBy(List<Ordering> orderings) {
		
		select = where.orderBy(orderings.toArray(new Ordering[]{}));
	}

	@Override
	protected Clause doLt(String name, Object value) {
		
		return QueryBuilder.lt(name, value);
	}

}
