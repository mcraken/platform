package org.mcplissken.cache.ehcache;

import net.sf.ehcache.Element;
import net.sf.ehcache.search.attribute.AttributeExtractor;
import net.sf.ehcache.search.attribute.AttributeExtractorException;

import org.mcplissken.cache.CacheAttributeExtractor;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 27, 2015
 */
public class EhcacheAttributeExtractor<T> implements AttributeExtractor{

	private static final long serialVersionUID = 1L;
	
	private CacheAttributeExtractor<T> attributeExtractor;
	
	public EhcacheAttributeExtractor(CacheAttributeExtractor<T> attributeExtractor) {
		this.attributeExtractor = attributeExtractor;
	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.search.attribute.AttributeExtractor#attributeFor(net.sf.ehcache.Element, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object attributeFor(Element element, String attributeName)
			throws AttributeExtractorException {
				
		T cachedObject = (T) element.getObjectValue();
		
		return attributeExtractor.extract(cachedObject);
	}

}
