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
package org.cradle.oauth;

import org.cradle.repository.models.account.Oauth;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 18, 2015
 */
public abstract class OAuthAuthorizer {
	
	protected OAuthService service;
	protected String provider;
	
	public OAuthAuthorizer(OAuthService service, 
			String provider) {
		
		this.service = service;
		this.provider = provider;
	}

	public Oauth finish(HandshakeAdpater handshakeAdpater){
		
		Token accessToken = createAccessToken(handshakeAdpater, service);
		
		return new Oauth(provider, accessToken.getToken(), accessToken.getSecret(), accessToken.getRawResponse());
		
	}
	
	public abstract String start();
	
	protected abstract Token createAccessToken(HandshakeAdpater handshakeAdpater, OAuthService service);
}
