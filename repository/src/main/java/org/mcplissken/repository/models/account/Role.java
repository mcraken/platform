/**
 * 
 */
package org.mcplissken.repository.models.account;

import java.io.Serializable;
import java.util.List;

import org.mcplissken.repository.models.RestModel;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 9, 2014
 */
@Table("role")
public class Role implements RestModel, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1159549367935523615L;

	@PrimaryKey
	private String name;
	
	private List<String> permissions;
	
	
	public Role(String name, List<String> permissions) {
		this.name = name;
		this.permissions = permissions;
		
	}

	public boolean equals(Role role){
		
		if(name.equals(role.name))
			return true;
		
		return false;
	}
	
	/**
	 * @return the permissions
	 */
	public List<String> getPermissions() {
		
		return permissions;
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.repository.models.RestModel#getUniqueId()
	 */
	@Override
	public Object getUniqueId() {
		return name;
	}
	
	public boolean nameEquals(String targetName){
		
		return name.equalsIgnoreCase(targetName);
	}
}
