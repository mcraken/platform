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
package org.cradle.oauth.google;

import org.cradle.oauth.HandshakeAdpater;
import org.cradle.oauth.OAuthAuthorizer;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 18, 2015
 */
public class GoogleOauthAuthorizer extends OAuthAuthorizer {

	/**
	 * @param service
	 * @param provider
	 */
	public GoogleOauthAuthorizer(OAuthService service, String provider) {
		super(service, provider);
	}

	/* (non-Javadoc)
	 * @see org.cradle.security.oauth.OAuthAuthorizer#start(org.scribe.oauth.OAuthService)
	 */
	@Override
	public String start() {
		
		return service.getAuthorizationUrl(null);
	}

	/* (non-Javadoc)
	 * @see org.cradle.security.oauth.OAuthAuthorizer#createAccessToken(org.cradle.security.oauth.HandshakeAdpater, org.scribe.oauth.OAuthService)
	 */
	@Override
	protected Token createAccessToken(HandshakeAdpater handshakeAdpater,
			OAuthService service) {
		
		String code = handshakeAdpater.readParameter("code");
		
		return service.getAccessToken(null, new Verifier(code));
	}

}
