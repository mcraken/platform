/**
 * 
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
