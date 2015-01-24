/**
 * 
 */
package org.mcplissken.repository.key.parsers;

import java.lang.reflect.Type;
import java.util.List;

import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidKeyException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author Raken
 *
 */
public class GsonKeyParser implements KeyParser{

	private Gson gson;
	
	
	public GsonKeyParser(){
		gson = new GsonBuilder().create();
	}

	@Override
	public List<RestSearchKey> parseKey(String key) throws InvalidKeyException {
		
		Type listType = new TypeToken<List<RestSearchKey>>() {}.getType();
		
		try{
			List<RestSearchKey> keys = gson.fromJson(key, listType);
			
			for(RestSearchKey rKey:keys)
				rKey.parseAllCriterias();
			
			return keys;
			
		}catch(Exception e){
			throw new InvalidKeyException(e);
		}
	}

}
