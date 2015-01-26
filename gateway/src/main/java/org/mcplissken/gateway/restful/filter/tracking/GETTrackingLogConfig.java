/**
 * 
 */
package org.mcplissken.gateway.restful.filter.tracking;

import java.util.List;

import org.mcplissken.repository.key.RestSearchKey;

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
