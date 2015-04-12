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

import org.cradle.repository.ModelRepository;
import org.cradle.repository.exception.NoResultException;
import org.cradle.repository.models.account.Oauth;
import org.cradle.repository.query.SimpleSelectionAdapter;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Dec 22, 2014
 */
public abstract class OAuthInvoker<T> {

	private OAuthService service;
	private String protectedUrl;
	
	protected ModelRepository repository;
	protected Oauth oauth;
	
	private OAuthInvoker(OAuthService service, String protectedUrl,
			ModelRepository repository) {
		
		this.service = service;
		this.protectedUrl = protectedUrl;
		this.repository = repository;
	}

	public OAuthInvoker(OAuthService service, String protectedUrl, Oauth oauth, ModelRepository repository) {
		
		this(service, protectedUrl, repository);
		this.oauth = oauth;
	}

	public OAuthInvoker(OAuthService service, String protectedUrl,
			String email, String provider, ModelRepository repository) throws OauthEntityNotFoundException{

		this(service, protectedUrl, repository);
		
		try {

			SimpleSelectionAdapter<Oauth> selectionAdapter = repository.createSimpleSelectionAdapter("oauth");

			oauth =  selectionAdapter
					.eq("email", email)
					.eq("provider", provider)
					.result().get(0);

		} catch (NoResultException e) {

			throw new OauthEntityNotFoundException(email, provider);
		}
	}

	public T invoke() throws  OauthInvokationException{

		OAuthRequest request = new OAuthRequest(Verb.GET, protectedUrl);

		Token accessToken = new Token(oauth.getToken(), oauth.getSecret(), oauth.getRawResponse());

		service.signRequest(accessToken, request);

		request(request);

		Response response = request.send();

		if(response.isSuccessful()){

			T result = response(response);

			updateOauth();
			
			return result;
			
		}else{

			throw new OauthInvokationException();
		}

	}

	protected void updateOauth(){
		
		oauth.updateLastAccess();
		
		repository.update(oauth);
	}
	
	protected abstract void request(OAuthRequest request);

	protected abstract T response(Response response);
}
