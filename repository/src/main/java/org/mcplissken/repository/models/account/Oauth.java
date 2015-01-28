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
package org.mcplissken.repository.models.account;

import org.mcplissken.repository.models.RestModel;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 18, 2015
 */
@Table("oauth")
public class Oauth implements RestModel{
	
	@PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String email;
	@PrimaryKeyColumn(name="oauth_provider", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String provider;
	@Column("oauth_token")
	private String token;
	private String secret;
	@Column("raw_response")
	private String rawResponse;
	@Column("last_access")
	private Long lastAccess;
	
	public Oauth(String email, String provider, String token, String secret,
			String rawResponse, Long lastAccess) {
		
		this.email = email;
		this.provider = provider;
		this.token = token;
		this.secret = secret;
		this.rawResponse = rawResponse;
		this.lastAccess = lastAccess;
	}

	public Oauth(String provider, String token, String secret,
			String rawResponse) {
		
		this.provider = provider;
		this.token = token;
		this.secret = secret;
		this.rawResponse = rawResponse;
		
		this.lastAccess = System.currentTimeMillis();
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public String getProvider() {
		return provider;
	}

	public String getToken() {
		return token;
	}

	public String getSecret() {
		return secret;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	public Long getLastAccess() {
		return lastAccess;
	}
	
	public void updateLastAccess(){
		
		lastAccess = System.currentTimeMillis();
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.models.RestModel#getUniqueId()
	 */
	@Override
	public Object getUniqueId() {
		return email + "." + provider;
	}
	
}
