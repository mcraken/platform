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
package org.mcplissken.cache.ehcache;

import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.search.attribute.AttributeExtractor;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 27, 2015
 */
public class ExtractorSupportedSearchAttribute extends SearchAttribute {
	
	private AttributeExtractor attributeExtractor;
	
	public ExtractorSupportedSearchAttribute(
			AttributeExtractor attributeExtractor) {
		
		this.attributeExtractor = attributeExtractor;
	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.config.SearchAttribute#constructExtractor(java.lang.ClassLoader)
	 */
	@Override
	public AttributeExtractor constructExtractor(ClassLoader loader) {
		
		return attributeExtractor != null ? attributeExtractor : super.constructExtractor(loader); 
		
	}
	
}
