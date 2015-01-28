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
package org.mcplissken.oauth.google;

import org.mcplissken.oauth.OAuthInvoker;
import org.mcplissken.oauth.OauthEntityNotFoundException;
import org.mcplissken.repository.ModelRepository;
import org.mcplissken.repository.models.account.Oauth;
import org.scribe.model.OAuthRequest;
import org.scribe.oauth.OAuthService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Dec 23, 2014
 */

public abstract class GoogleOAuthInvoker<T> extends OAuthInvoker<T>{
	

	public GoogleOAuthInvoker(OAuthService service, String protectedUrl,
			Oauth oauth, ModelRepository repository) {
		
		super(service, protectedUrl, oauth, repository);
	}

	/**
	 * @param service
	 * @param protectedUrl
	 * @param email
	 * @param provider
	 * @param repository
	 * @throws OauthEntityNotFoundException
	 */
	public GoogleOAuthInvoker(OAuthService service, String protectedUrl,
			String email, String provider, ModelRepository repository)
			throws OauthEntityNotFoundException {
		
		super(service, protectedUrl, email, provider, repository);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.security.oauth.OAuthInvoker#request(org.scribe.model.OAuthRequest)
	 */
	@Override
	protected void request(OAuthRequest request) {
		
		request.addHeader("GData-Version", "3.0");
	}

}
