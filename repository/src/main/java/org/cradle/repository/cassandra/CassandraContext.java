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

import org.cradle.reporting.SystemReportingService;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

/**
 * @author 	Sherief Shawky
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
		
		reportingService.info(
				this.getClass().getSimpleName(), SystemReportingService.CONSOLE, 
				"Core Connections Per Host: " + cluster.getConfiguration().getPoolingOptions().getCoreConnectionsPerHost(HostDistance.LOCAL)
				);
		
		reportingService.info(
				this.getClass().getSimpleName(), SystemReportingService.CONSOLE, 
				"Max Connections Per Host: " + cluster.getConfiguration().getPoolingOptions().getMaxConnectionsPerHost(HostDistance.LOCAL)
				);
		
		reportingService.info(
				this.getClass().getSimpleName(), SystemReportingService.CONSOLE, 
				"Min Simultaneous Connections Per Host: " + cluster.getConfiguration().getPoolingOptions().getMinSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL)
				);
		
		reportingService.info(
				this.getClass().getSimpleName(), SystemReportingService.CONSOLE, 
				"Max Simultaneous Connections Per Host: " + cluster.getConfiguration().getPoolingOptions().getMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL)
				);

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
