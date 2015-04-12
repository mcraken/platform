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
package org.cradle.reporting.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.cradle.reporting.SystemReportingService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 2, 2014
 */
public class Log4JSystemReportingService implements SystemReportingService {
	
	private String path;
	
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	public void init(){

		PropertyConfigurator.configure(path);
		
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.system.reporting.SystemReportingService#info(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void info(String reporter, String channel, String message) {
		
		Logger logger = Logger.getLogger(channel);
		
		logger.info(reporter + "|" + message);
	}

	/* (non-Javadoc)
	 * @see org.cradle.system.reporting.SystemReportingService#exception(java.lang.String, java.lang.String, java.lang.Exception)
	 */
	@Override
	public void exception(String reporter, String channel, Throwable ex) {

		Logger logger = Logger.getLogger(channel);

		logger.info(reporter, ex);
	}

}
