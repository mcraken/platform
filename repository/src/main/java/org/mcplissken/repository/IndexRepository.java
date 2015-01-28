/**
 * 
 */
package org.mcplissken.repository;

import org.mcplissken.repository.index.CoreAnnotationIsNotPresent;
import org.mcplissken.repository.index.IndexPorter;
import org.mcplissken.repository.index.IndexQueryAdapter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public interface IndexRepository {

	public enum CoreLanguage {

		ENGLISH("en"),  ARABIC("ar");

		public String value;

		CoreLanguage(String value){
			this.value = value;
		}
	}

	public <T> IndexPorter<T> indexPorter(Class<T> type) throws CoreAnnotationIsNotPresent;

	public <T> IndexPorter<T> indexPorter(Class<T> type, CoreLanguage language) throws CoreAnnotationIsNotPresent;

	public <T> IndexQueryAdapter<T> queryAdapter(String coreName);

	public <T> void registerIndexMapper(String modelName, BasicRowMapper<T> mapper);
}