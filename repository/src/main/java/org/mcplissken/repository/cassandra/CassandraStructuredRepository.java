/**
 * 
 */
package org.mcplissken.repository.cassandra;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mcplissken.repository.BasicRowMapper;
import org.mcplissken.repository.StructuredRepository;
import org.mcplissken.repository.cassandra.criteriahandlers.CriteriaHandler;
import org.mcplissken.repository.cassandra.mappers.AccountRowMapper;
import org.mcplissken.repository.cassandra.mappers.ContentRowMapper;
import org.mcplissken.repository.cassandra.mappers.OauthRowMapper;
import org.mcplissken.repository.cassandra.mappers.ProfileRowMapper;
import org.mcplissken.repository.cassandra.mappers.RoleRowMapper;
import org.mcplissken.repository.cassandra.mappers.TrackingLogRowMapper;
import org.mcplissken.repository.cassandra.query.CassandraSimpleSelectionAdapter;
import org.mcplissken.repository.key.RestCriteria;
import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.CriteriaNotFoundException;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;
import org.mcplissken.repository.key.exception.ServerFunctionException;
import org.mcplissken.repository.key.functions.ServerFunctionHandler;
import org.mcplissken.repository.models.RestModel;
import org.mcplissken.repository.query.SimpleSelectionAdapter;
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
	public <T> void registerMapper(String modelName, BasicRowMapper<T> mapper){
		
		rowMappers.put(modelName, new CassandraRowMapper<T>(mapper));
		
	}
	
	private void registerDefaultRowMappers() {
		
		registerMapper("account", new AccountRowMapper());
		
		registerMapper("role", new RoleRowMapper());
		
		registerMapper("content", new ContentRowMapper());
		
		registerMapper("trackinglog", new TrackingLogRowMapper());

		registerMapper("profile", new ProfileRowMapper());
		
		registerMapper("oauth", new OauthRowMapper());
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mcplissken.repository.ModelRepository#write(java.lang.Object)
	 */
	@Override
	public void write(RestModel model) {
		
		cassandraTemplate.insert(model, writeOptions);
	}

	
	@Override
	public void write(List<RestModel> models) {
		
		cassandraTemplate.insertAsynchronously(models, writeOptions);
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.repository.StructuredRepository#update(org.mcplissken.repository.models.RestModel)
	 */
	@Override
	public void update(RestModel model) {
		
		cassandraTemplate.update(model, writeOptions);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.StructuredRepository#delete(org.mcplissken.repository.models.RestModel)
	 */
	@Override
	public void delete(RestModel model) {
		
		cassandraTemplate.delete(model);
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.repository.ModelRepository#createSimpleSelectionAdapter(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> SimpleSelectionAdapter<T> createSimpleSelectionAdapter(String modelName) {

		return new CassandraSimpleSelectionAdapter<T>(
				modelName, 
				cassandraTemplate,
				 (CassandraRowMapper<T>) rowMappers.get(modelName)
				);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.ModelRepository#read(org.mcplissken.repository.key.RestSearchKey)
	 */
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
