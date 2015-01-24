/**
 * 
 */
package org.mcplissken.repository.key.generator;

import java.util.List;
import java.util.Map;

import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidKeyException;
import org.mcplissken.repository.key.parsers.GsonKeyParser;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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
	 * @see com.mubasher.market.broadcast.tracks.minute.key.KeyGenerator#generateKeys(java.util.Map)
	 */
	@Override
	public List<RestSearchKey> generateKeys(Map<String, Object> params) throws InvalidKeyException {
		
		searchKeysJson = searchKeysJson.replaceAll("WINDOW_VALUE", String.valueOf(params.get("WINDOW_VALUE")));
		
		List<RestSearchKey> searchKeys = parser.parseKey(searchKeysJson);

		return searchKeys;
	}

}
