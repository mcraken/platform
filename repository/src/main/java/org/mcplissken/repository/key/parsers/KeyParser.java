package org.mcplissken.repository.key.parsers;

import java.util.List;

import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidKeyException;

public interface KeyParser {
	public List<RestSearchKey> parseKey(String key) throws InvalidKeyException;
}
