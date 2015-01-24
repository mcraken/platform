/**
 * 
 */
package org.mcplissken.oauth;

import org.mcplissken.repository.ModelRepository;
import org.mcplissken.repository.exception.NoResultException;
import org.mcplissken.repository.models.account.Oauth;
import org.mcplissken.repository.query.SimpleSelectionAdapter;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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

			SimpleSelectionAdapter selectionAdapter = repository.createSimpleSelectionAdapter("oauth");

			oauth =  (Oauth) selectionAdapter
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
