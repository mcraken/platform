/**
 * 
 */
package org.mcplissken.repository.key.generator;

import java.util.List;
import java.util.Map;

import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidKeyException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 23, 2014
 */
public interface KeyGenerator {
	
	public List<RestSearchKey> generateKeys(Map<String, Object> params) throws InvalidKeyException;
}
