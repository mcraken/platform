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
package org.cradle.oauth.impl;

import java.util.Map;

import org.cradle.oauth.OAuthAuthorizer;
import org.cradle.oauth.OAuthBuilder;
import org.cradle.oauth.OAuthInvoker;
import org.cradle.oauth.OAuthProvider;
import org.cradle.oauth.OauthProviderNotSupported;
import org.cradle.repository.models.account.Oauth;
import org.cradle.repository.models.account.Profile;

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
	 * @see org.cradle.oauth.OAuthService#authorizer(java.lang.String)
	 */
	@Override
	public OAuthAuthorizer authorizer(String provider) throws OauthProviderNotSupported {
		
		OAuthBuilder builder = getOauthBuilder(provider);
		
		return builder.buildAuthorizer();
	}

	/* (non-Javadoc)
	 * @see org.cradle.oauth.OAuthService#profile(java.lang.String)
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
