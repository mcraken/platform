/**
 * 
 */
package org.mcplissken.localization;

import java.util.List;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 21, 2015
 */
public interface LocalizationService {
	
	public List<Localized> localize(String language, List<String> ids) throws LocalizationException;
}
