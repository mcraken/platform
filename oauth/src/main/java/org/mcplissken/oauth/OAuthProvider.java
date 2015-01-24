/**
 * 
 */
package org.mcplissken.oauth;

import org.mcplissken.repository.models.account.Oauth;
import org.mcplissken.repository.models.account.Profile;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 19, 2015
 */
public interface OAuthProvider {
	
	public OAuthAuthorizer authorizer(String provider) throws OauthProviderNotSupported;
	
	public OAuthInvoker<Profile> profile(String provider, Oauth oauth) throws OauthProviderNotSupported;
}
