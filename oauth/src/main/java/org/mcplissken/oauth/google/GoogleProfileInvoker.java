/**
 * 
 */
package org.mcplissken.oauth.google;

import org.mcplissken.repository.ModelRepository;
import org.mcplissken.repository.models.account.Oauth;
import org.mcplissken.repository.models.account.Profile;
import org.scribe.model.Response;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 18, 2015
 */

class GoogleProfile {
	public String email;
	public String given_name;
	public String family_name;
}

public class GoogleProfileInvoker extends GoogleOAuthInvoker<Profile>{


	public GoogleProfileInvoker(OAuthService service, String protectedUrl,
			Oauth oauth, ModelRepository repository) {
		
		super(service, protectedUrl, oauth, repository);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.security.oauth.OAuthInvoker#response(org.scribe.model.Response)
	 */
	@Override
	protected Profile response(Response response) {
		
		Gson gson = new GsonBuilder().create();
		
		GoogleProfile googleProfile = gson.fromJson(response.getBody(), GoogleProfile.class);
		
		oauth.setEmail(googleProfile.email);
		
		return new Profile(googleProfile.email, googleProfile.given_name, googleProfile.family_name, repository);
	}

}
