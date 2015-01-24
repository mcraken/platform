/**
 * 
 */
package org.mcplissken.gateway.restful.filter.tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidKeyException;
import org.mcplissken.repository.key.parsers.KeyParser;
import org.mcplissken.repository.models.RestModel;
import org.mcplissken.repository.models.tracking.TrackingLog;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 12, 2015
 */
public class GETTrackingLogCreator implements TrackingLogCreator {

	private Map<String, GETTrackingLogConfig> config;
	private KeyParser parser;
	
	/**
	 * @param config the config to set
	 */
	public void setConfig(Map<String, GETTrackingLogConfig> config) {
		this.config = config;
	}

	/**
	 * @param parser the parser to set
	 */
	public void setParser(KeyParser parser) {
		this.parser = parser;
	}
	
	/* (non-Javadoc)
	 * @see com.mubasher.gateway.restful.filter.tracking.TrackingLogCreator#create(com.mubasher.gateway.HttpAdapter)
	 */
	@Override
	public List<RestModel> create(HttpAdapter httpAdapter) {

		try {
			
			String key = httpAdapter.readHeader(RestSearchKey.SEACRH_KEY);

			if(key == null)
				throw new InvalidKeyException();

			List<RestSearchKey> parsedKeys = parser.parseKey(key);
			
			ArrayList<RestModel> trackingLogs = new ArrayList<>();
			
			String resourceName = null;
			
			GETTrackingLogConfig trackingConfig = null;
			
			ArrayList<String> resourceIdValue = null;
			
			for(RestSearchKey parsedKey : parsedKeys){
				
				resourceName = parsedKey.getResourceName();
				
				trackingConfig = config.get(resourceName);
				
				if(trackingConfig == null){
					
					return null;
					
				} else {
					
					resourceIdValue = new ArrayList<>();
					
					if(trackingConfig.isRelative(parsedKey, resourceIdValue))
						trackingLogs.add(new TrackingLog("GET", resourceName, httpAdapter.getUser().userId(), resourceIdValue));
					
				}
				
			}
			
			return trackingLogs;
			
		} catch (InvalidKeyException e) {
			
			return null;
		}
		
	}

}
