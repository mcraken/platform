/**
 * 
 */
package org.mcplissken.repository.cassandra;

import org.mcplissken.reporting.SystemReportingService;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

/**
 * @author 	Sherief Shawky
 * @email 	sh
 * erif.shawki@mubasher.info
 * @date 	Aug 12, 2014
 */
public class CassandraContext {

	private AnnotationConfigApplicationContext context;

	private SystemReportingService reportingService;
	
	public void setReportingService(SystemReportingService reportingService) {
		
		this.reportingService = reportingService;
	}
	
	public void init(){

		context = new AnnotationConfigApplicationContext(CassandraConfig.class);

		printNodeInfo();
	}

	private void printNodeInfo() {

		Cluster cluster = getSession().getCluster();

		Metadata metadata = cluster.getMetadata();

		reportingService.info(this.getClass().getSimpleName(), SystemReportingService.CONSOLE, "Connected to cluster: " + metadata.getClusterName());
		
		for ( Host host : metadata.getAllHosts() ) {
			
			reportingService.info(this.getClass().getSimpleName(), SystemReportingService.CONSOLE, "Datacenter: " + host.getDatacenter() + "; Host: " + host.getAddress() + "; Rack: "
					+ host.getRack());
		}
	}

	public void destroy(){
		
		context.close();
	}

	public CassandraTemplate getCassandraTemplate(){
		
		return (CassandraTemplate) context.getBean("cassandraOperations");
	}

	public Session getSession(){
		return (Session) context.getBean("cassandraSession");
	}

	public WriteOptions getWriteOptions(){
		return (WriteOptions) context.getBean("WriteOptions");
	}

}
