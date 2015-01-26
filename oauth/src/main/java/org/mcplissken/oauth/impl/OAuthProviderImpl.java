/**
 * 
 */
package org.mcplissken.oauth.impl;

import java.util.Map;

import org.mcplissken.oauth.OAuthAuthorizer;
import org.mcplissken.oauth.OAuthBuilder;
import org.mcplissken.oauth.OAuthInvoker;
import org.mcplissken.oauth.OAuthProvider;
import org.mcplissken.oauth.OauthProviderNotSupported;
import org.mcplissken.repository.models.account.Oauth;
import org.mcplissken.repository.models.account.Profile;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 19, 2015
 */
public class OAuthProviderImpl implements OAuthProvider {
	
	private Map<String, OAuthBuilder> oauthBuilders;
	
	/**
	 * @param oauthBuilders the oauthBuilders to set
	 */
	public void setOauthBuilders(Map<String, OAuthBuilder> oauthBuilders) {
		this.oauthBuilders = oauthBuilders;
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.oauth.OAuthService#authorizer(java.lang.String)
	 */
	@Override
	public OAuthAuthorizer authorizer(String provider) throws OauthProviderNotSupported {
		
		OAuthBuilder builder = getOauthBuilder(provider);
		
		return builder.buildAuthorizer();
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.oauth.OAuthService#profile(java.lang.String)
	 */
	@Override
	public OAuthInvoker<Profile> profile(String provider, Oauth oauth) throws OauthProviderNotSupported {
		
		OAuthBuilder builder = getOauthBuilder(provider);
		
		return builder.buildProfileInvoker(oauth);
	}
	
	private OAuthBuilder getOauthBuilder(String provider)
			throws OauthProviderNotSupported {
		
		OAuthBuilder oAuthBuilder = oauthBuilders.get(provider);

		if(oAuthBuilder == null){
			
			throw new OauthProviderNotSupported(provider);
			
		}
		
		return oAuthBuilder;
	}

}
