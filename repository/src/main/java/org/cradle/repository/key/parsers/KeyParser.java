package org.cradle.repository.key.parsers;

import java.util.List;

import org.cradle.repository.key.RestSearchKey;
import org.cradle.repository.key.exception.InvalidKeyException;

public interface KeyParser {
	public List<RestSearchKey> parseKey(String key) throws InvalidKeyException;
}
