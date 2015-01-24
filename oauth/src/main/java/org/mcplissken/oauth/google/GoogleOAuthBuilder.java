/**
 * 
 */
package org.mcplissken.oauth.google;

import java.util.Map;

import org.mcplissken.oauth.OAuthAuthorizer;
import org.mcplissken.oauth.OAuthBuilder;
import org.mcplissken.oauth.OAuthInvoker;
import org.mcplissken.repository.ModelRepository;
import org.mcplissken.repository.models.account.Oauth;
import org.mcplissken.repository.models.account.Profile;
import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Dec 23, 2014
 */
public class GoogleOAuthBuilder implements OAuthBuilder {

	private String apiSecret;
	private String apiKey;
	private String scope;
	private String callback;
	private Map<String, String> protectedUrls;
	private ModelRepository repository;

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(ModelRepository repository) {
		this.repository = repository;
	}
	
	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @param protectedUrls the protectedUrls to set
	 */
	public void setProtectedUrls(Map<String, String> protectedUrls) {
		this.protectedUrls = protectedUrls;
	}
	
	/**
	 * @param callback the callback to set
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.security.oauth.OAuthBuilder#buildAuthorizer()
	 */
	@Override
	public OAuthAuthorizer buildAuthorizer() {

		OAuthService service =  new ServiceBuilder()
		.provider(Google2Api.class)
		.apiKey(apiKey)
		.apiSecret(apiSecret)
		.scope(scope)
		.callback(callback)
		.build();

		return new GoogleOauthAuthorizer(service, "google");
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.security.oauth.OAuthBuilder#buildProfileInvoker()
	 */
	@Override
	public OAuthInvoker<Profile> buildProfileInvoker(Oauth oauth) {
		
		OAuthService service =  new ServiceBuilder()
		.provider(Google2Api.class)
		.apiKey(apiKey)
		.apiSecret(apiSecret)
		.build();
		
		return new GoogleProfileInvoker(service, protectedUrls.get("profile"), oauth, repository);
	}

}
