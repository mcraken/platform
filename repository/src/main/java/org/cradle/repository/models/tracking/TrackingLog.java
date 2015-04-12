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
package org.cradle.repository.models.tracking;

import java.util.List;

import org.cradle.repository.models.RestModel;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 12, 2015
 */
@Table("trackinglog")
public class TrackingLog implements RestModel{
	
	@PrimaryKey
	private Long timing;
	
	private String method;
	
	@Column("resource_name")
	private String resourceName;
	
	@Column("resource_id")
	private List<String> resourceId;
	
	@Column("user_id")
	private String userId;
	
	public TrackingLog(String method, String resourceName,
			List<String> resourceId, String userId, Long timing) {
		
		this.method = method;
		this.resourceName = resourceName;
		this.resourceId = resourceId;
		this.userId = userId;
		this.timing = timing;
	}

	public TrackingLog(String method, String resourceName, String userId, List<String> resourceId) {
		
		this(method, resourceName, resourceId, userId, System.currentTimeMillis());
		
	}
	
	public void addResourceId(String id){
		
		resourceId.add(id);
	}
	
	public String getMethod() {
		return method;
	}

	public String getResourceName() {
		return resourceName;
	}

	public List<String> getResourceId() {
		return resourceId;
	}

	public String getUserId() {
		return userId;
	}

	public Long getTiming() {
		return timing;
	}

	/* (non-Javadoc)
	 * @see org.cradle.repository.models.RestModel#getUniqueId()
	 */
	@Override
	public Object getUniqueId() {
		return timing;
	}
	
}
