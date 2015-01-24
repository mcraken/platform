/**
 * 
 */
package org.mcplissken.oauth;

import org.mcplissken.repository.models.account.Oauth;
import org.mcplissken.repository.models.account.Profile;



/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Dec 22, 2014
 */
public interface OAuthBuilder {
	
	public OAuthAuthorizer buildAuthorizer();
	
	public OAuthInvoker<Profile> buildProfileInvoker(Oauth oauth);
	
}
