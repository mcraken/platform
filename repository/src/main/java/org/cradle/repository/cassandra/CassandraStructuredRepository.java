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
package org.cradle.repository.cassandra;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cradle.repository.BasicRowMapper;
import org.cradle.repository.StructuredRepository;
import org.cradle.repository.cassandra.criteriahandlers.CriteriaHandler;
import org.cradle.repository.cassandra.mappers.AccountRowMapper;
import org.cradle.repository.cassandra.mappers.ContentRowMapper;
import org.cradle.repository.cassandra.mappers.OauthRowMapper;
import org.cradle.repository.cassandra.mappers.ProfileRowMapper;
import org.cradle.repository.cassandra.mappers.RoleRowMapper;
import org.cradle.repository.cassandra.mappers.TrackingLogRowMapper;
import org.cradle.repository.cassandra.query.CassandraSimpleSelectionAdapter;
import org.cradle.repository.key.RestCriteria;
import org.cradle.repository.key.RestSearchKey;
import org.cradle.repository.key.exception.CriteriaNotFoundException;
import org.cradle.repository.key.exception.InvalidCriteriaException;
import org.cradle.repository.key.exception.ServerFunctionException;
import org.cradle.repository.key.functions.ServerFunctionHandler;
import org.cradle.repository.query.SimpleSelectionAdapter;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Selection;
import com.datastax.driver.core.querybuilder.Select.Where;
 
/**
 * @author Sherief Shawky
 * @email mcrakens@gmail.com
 * @date Jul 22, 2014
 */
public class CassandraStructuredRepository implements StructuredRepository {

	private WriteOptions writeOptions;
	private CassandraTemplate cassandraTemplate;

	private CassandraContext cassandraContext;

	private Map<String, CassandraRowMapper<?>> rowMappers;
	private Map<String, CriteriaHandler> criteriaHandlers;
	private Map<String, ServerFunctionHandler> serverFunctionHandlers;
	
	/**
	 * @param criteriaHandlers the criteriaHandlers to set
	 */
	public void setCriteriaHandlers(
			Map<String, CriteriaHandler> criteriaHandlers) {

		this.criteriaHandlers = criteriaHandlers;
	}

	/**
	 * @param cassandraContext
	 *            the cassandraContext to set
	 */
	public void setCassandraContext(CassandraContext cassandraContext) {
		this.cassandraContext = cassandraContext;
	}
	/**
	 * @param serverFunctionHandlers the serverFunctionHandlers to set
	 */
	public void setServerFunctionHandlers(
			Map<String, ServerFunctionHandler> serverFunctionHandlers) {
		this.serverFunctionHandlers = serverFunctionHandlers;
	}
	
	public void init() {

		cassandraTemplate = cassandraContext.getCassandraTemplate();

		writeOptions = cassandraContext.getWriteOptions();

		rowMappers = new HashMap<String, CassandraRowMapper<?>>();
		
		registerDefaultRowMappers();

	}

	@Override
	public <T> void registerStructuredMapper(String modelName, BasicRowMapper<T> mapper){
		
		rowMappers.put(modelName, new CassandraRowMapper<T>(mapper));
		
	}
	
	private void registerDefaultRowMappers() {
		
		registerStructuredMapper("account", new AccountRowMapper());
		
		registerStructuredMapper("role", new RoleRowMapper());
		
		registerStructuredMapper("content", new ContentRowMapper());
		
		registerStructuredMapper("trackinglog", new TrackingLogRowMapper());

		registerStructuredMapper("profile", new ProfileRowMapper());
		
		registerStructuredMapper("oauth", new OauthRowMapper());
		
	}

	@Override
	public <T> void write(T model) {
		
		cassandraTemplate.insert(model, writeOptions);
	}

	
	@Override
	public <T> void write(List<T> models) {
		
		cassandraTemplate.insertAsynchronously(models, writeOptions);
	}

	@Override
	public <T> void update(T model) {
		
		cassandraTemplate.update(model, writeOptions);
	}

	@Override
	public <T> void delete(T model) {
		
		cassandraTemplate.delete(model);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> SimpleSelectionAdapter<T> createSimpleSelectionAdapter(String modelName) {

		return new CassandraSimpleSelectionAdapter<T>(
				modelName, 
				cassandraTemplate,
				 (CassandraRowMapper<T>) rowMappers.get(modelName)
				);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> read(RestSearchKey key) throws InvalidCriteriaException {

		String resourceName = key.getResourceName();

		Select select = createSelection(key, resourceName);
		
		Where where = select.where();

		Map<String, Clause> crtsMap = buildCriteriaMap(where, key);

		Clause[] criterions = crtsMap.values().toArray(new Clause[]{});

		CassandraRowMapper<T> rowMapper = (CassandraRowMapper<T>) rowMappers.get(resourceName);

		return executeSearchQuery(key, select, where, criterions, rowMapper);
	}

	private Select createSelection(RestSearchKey key, String resourceName) {
		
		Selection selection = QueryBuilder.select();
		
		if(key.isSelectionRequired()){
			
			executeColumnSelection(key, selection);
		}
		
		Select select = selection.from(resourceName);
		
		return select;
	}

	private void executeColumnSelection(RestSearchKey key, Selection selection) {
		
		String[] columns = key.parseColumns();
		
		for(String column : columns)
			selection.column(column);
	}

	/**
	 * @param key
	 * @param where
	 * @param criterions
	 * @param rowMapper 
	 * @return
	 */
	private <T> List<T> executeSearchQuery(RestSearchKey key, Select select, Where where,
			Clause[] criterions, RowMapper<T> rowMapper) {

		for(Clause crt : criterions){
			where = where.and(crt);
		}

		int count = key.getCount();

		if(count != 0)
			select = where.limit(count);
		
		return cassandraTemplate.query(select, rowMapper);
	}

	/**
	 * @param where
	 * @param key
	 * @return
	 * @throws InvalidCriteriaException 
	 */
	private Map<String, Clause> buildCriteriaMap(Where where, RestSearchKey key) throws InvalidCriteriaException {
		
		Iterator<RestCriteria> crtsIter = key.criteriasIterator();

		try{

			Map<String, Clause> crtsMap = new HashMap<String, Clause>();

			while(crtsIter.hasNext()){

				handleSingleCriteria(where, key, crtsIter, crtsMap);
			}

			return crtsMap;

		} catch(Exception e){
			
			throw new InvalidCriteriaException(e);
		}
	}

	private void handleSingleCriteria(Where where, RestSearchKey key,
			Iterator<RestCriteria> crtsIter, Map<String, Clause> crtsMap)
					throws ServerFunctionException, InvalidCriteriaException {

		RestCriteria crt;

		CriteriaHandler handler;

		crt = crtsIter.next();

		if(crt.isServerFunction()){

			executeServerFunction(key, crt);
		}

		handler = criteriaHandlers.get(crt.readCriteriaFunction());

		if(handler == null){
			
			throw new InvalidCriteriaException(new CriteriaNotFoundException(crt.readCriteriaFunction()));
		}

		handler.handle(crt, crtsMap, where);
	}

	private void executeServerFunction(RestSearchKey key, RestCriteria crt)
			throws ServerFunctionException {
		
		String functionName = crt.readServerFunctionName();

		ServerFunctionHandler functionHandler = serverFunctionHandlers.get(functionName);

		functionHandler.handle(
				crt, 
				this, 
				crt.readCriteriaName(), 
				key.getResourceName());
	}

}
