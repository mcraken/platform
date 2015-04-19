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
package org.cradle.platform.httpgateway.filter.tracking;

import java.util.List;

import org.cradle.repository.key.RestSearchKey;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 12, 2015
 */
public class GETTrackingLogConfig {
	
	public List<String> resourceId;
	
	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(List<String> resourceId) {
		this.resourceId = resourceId;
	}
	
	/**
	 * @return the resourceId
	 */
	public List<String> getResourceId() {
		return resourceId;
	}
	
	public boolean isRelative(RestSearchKey key, List<String> resourceIdValue){
		
		String idValue;
		
		for(String id : resourceId){
			
			if((idValue = key.hasProperty(id)) == null)
				return false;
			else
				resourceIdValue.add(idValue);
		}
		
		return true;
	}
}
