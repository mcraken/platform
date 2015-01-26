/**
 * 
 */
package org.mcplissken.oauth;

import org.mcplissken.repository.models.account.Oauth;
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
