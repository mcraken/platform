/**
 * 
 */
package org.mcplissken.repository.models.tracking;

import java.util.List;

import org.mcplissken.repository.models.RestModel;
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
	 * @see org.mcplissken.repository.models.RestModel#getUniqueId()
	 */
	@Override
	public Object getUniqueId() {
		return timing;
	}
	
}
