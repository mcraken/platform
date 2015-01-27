/**
 * 
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
