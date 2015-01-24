/**
 * 
 */
package org.mcplissken.gateway.restful.filter.tracking;

import java.util.List;

import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.repository.models.RestModel;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 12, 2015
 */
public interface TrackingLogCreator {

	public List<RestModel> create(HttpAdapter httpAdapter);
}
