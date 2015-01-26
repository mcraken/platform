/**
 * 
 */
package org.mcplissken.repository;

import java.io.File;

import org.mcplissken.repository.exception.ContentException;
import org.mcplissken.repository.models.content.Content;



/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jul 14, 2014
 */
public interface ModelRepository  extends StructuredRepository, IndexRepository{
	
	public Content createContent(String id, String name, String type, File contentFile) throws ContentException;
	
}
