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
package org.mcplissken.repository.key.generator;

import java.util.List;
import java.util.Map;

import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidKeyException;
import org.mcplissken.repository.key.parsers.GsonKeyParser;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 23, 2014
 */
public class TemplateKeyGenerator implements KeyGenerator {

	private String searchKeysJson;

	private GsonKeyParser parser;

	public void setParser(GsonKeyParser parser) {
		this.parser = parser;
	}

	public void setSearchKeysJson(String searchKeysJson) {
		this.searchKeysJson = searchKeysJson;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.broadcast.tracks.minute.key.KeyGenerator#generateKeys(java.util.Map)
	 */
	@Override
	public List<RestSearchKey> generateKeys(Map<String, Object> params) throws InvalidKeyException {
		
		searchKeysJson = searchKeysJson.replaceAll("WINDOW_VALUE", String.valueOf(params.get("WINDOW_VALUE")));
		
		List<RestSearchKey> searchKeys = parser.parseKey(searchKeysJson);

		return searchKeys;
	}

}
