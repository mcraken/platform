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

import org.cradle.repository.ModelRepository;
import org.cradle.repository.models.account.Oauth;
import org.cradle.repository.models.account.Profile;
import org.scribe.model.Response;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
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
	 * @see org.cradle.security.oauth.OAuthInvoker#response(org.scribe.model.Response)
	 */
	@Override
	protected Profile response(Response response) {
		
		Gson gson = new GsonBuilder().create();
		
		GoogleProfile googleProfile = gson.fromJson(response.getBody(), GoogleProfile.class);
		
		oauth.setEmail(googleProfile.email);
		
		return new Profile(googleProfile.email, googleProfile.given_name, googleProfile.family_name, repository);
	}

}
