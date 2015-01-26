/**
 * 
 */
package org.mcplissken.repository.cassandra;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.ConsistencyLevel;
import org.springframework.cassandra.core.RetryPolicy;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jul 12, 2014
 */
@Configuration
@ComponentScan({"org.mcplissken.repository.models"})
@EnableCassandraRepositories(basePackages = {"org.mcplissken.repository.models"})
public class CassandraConfig extends AbstractCassandraConfiguration {

	private static final String TABLES_PROPERTY_KEY = "tables";
	private static final String KEYSPACE_PROPERTY_KEY = "keyspace";
	private static final String KEYSPACE_NAME_PROPERTY_KEY = "keyspace_name";
	private static final String HOSTS_PROPERTY_KEY = "hosts";
	
	private String keySpaceName;
	private String hosts;
	private Properties prop = new Properties();
	private InputStream input = null;

	@Autowired
	private CassandraTemplate cassandraTemplate;

	/* (non-Javadoc)
	 * @see org.springframework.cassandra.config.java.AbstractClusterConfiguration#getContactPoints()
	 */
	@Override
	protected String getContactPoints() {
		
		return hosts;
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration#getKeyspaceName()
	 */
	@Override
	protected String getKeyspaceName() {
		
		return keySpaceName;
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration#session()
	 */
	@Override
	public CassandraSessionFactoryBean session() throws Exception {
		
		loadSchemaProperties();
		
		Session system = cluster().getObject().connect();

		validateKeyspace(system);

		return super.session();
	}

	private void loadSchemaProperties() {
		
		try {
			
			input = new FileInputStream("keyspace_schema/keyspace_schema.properties");
			
			prop.load(input);
			
			keySpaceName = prop.getProperty(KEYSPACE_NAME_PROPERTY_KEY);
			
			hosts = prop.getProperty(HOSTS_PROPERTY_KEY);
			
			input.close();
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
		}
	}
	
	private KeyspaceMetadata readKeySpaceMetedata(Session system) {
		
		return system.getCluster().getMetadata().getKeyspace(getKeyspaceName());
	}
	
	private KeyspaceMetadata getKeyspaceMetadata(Session system) {

		KeyspaceMetadata kmd = readKeySpaceMetedata(system);

		if (kmd == null) {
			
			String keyspaceDDL = prop.getProperty(KEYSPACE_PROPERTY_KEY);
			
			system.execute(keyspaceDDL);

			kmd = readKeySpaceMetedata(system);
		}

		system.execute("USE " + getKeyspaceName() + ";");

		return kmd;
	}

	private void validateKeyspace(Session system) {

		KeyspaceMetadata kmd = getKeyspaceMetadata(system);

		String schemalTables = prop.getProperty(TABLES_PROPERTY_KEY);
		
		String[] allTables = schemalTables.split(",");
		
		for (String tableName : allTables) {
			
			defineTableIfNotExists(tableName, prop.getProperty(tableName), system, kmd);
			
		}
	}

	private void defineTableIfNotExists(String table, String ddl, Session system, KeyspaceMetadata kmd) {

		TableMetadata tmd = kmd.getTable(table);

		if(tmd == null){
			
			defineData(ddl, system);
			
			initData(table, "indices", system);
			
			initData(table, "init", system);
		}
	}

	private void defineData(String ddl, Session system) {
		
		system.execute(ddl);
	}
	
	
	
	private void initData(String table, String prefix, Session system){
		
		String initDDL = prop.getProperty(table + "_" + prefix);
		
		if(initDDL != null){
			
			String[] ddls = initDDL.split("\\|");
			
			for(String ddl : ddls)
				defineData(ddl, system);
		}
	}

	@Bean
	public WriteOptions WriteOptions(){

		WriteOptions options = new WriteOptions();

		options.setConsistencyLevel(ConsistencyLevel.QUOROM);

		options.setRetryPolicy(RetryPolicy.DOWNGRADING_CONSISTENCY);

		return options;
	}

	@Bean
	public CassandraTemplate cassandraOperations() throws Exception{
		
		return new CassandraTemplate(session().getObject());
	}

	@Bean
	public Session cassandraSession(){
		
		return cassandraTemplate.getSession();
	}


}
