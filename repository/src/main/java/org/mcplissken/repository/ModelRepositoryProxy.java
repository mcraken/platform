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
package org.mcplissken.repository;

import java.io.File;
import java.util.List;

import org.mcplissken.repository.exception.ContentException;
import org.mcplissken.repository.index.CoreAnnotationIsNotPresent;
import org.mcplissken.repository.index.IndexPorter;
import org.mcplissken.repository.index.IndexQueryAdapter;
import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;
import org.mcplissken.repository.models.RestModel;
import org.mcplissken.repository.models.content.Content;
import org.mcplissken.repository.query.SimpleSelectionAdapter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 20, 2014
 */
public class ModelRepositoryProxy implements ModelRepository{

	private StructuredRepository structuredRepository;
	private IndexRepository indexRepository;

	/**
	 * @param structuredRepository the structuredRepository to set
	 */
	public void setStructuredRepository(
			StructuredRepository structuredRepository) {

		this.structuredRepository = structuredRepository;

	}

	/**
	 * @param indexRepository the indexRepository to set
	 */
	public void setIndexRepository(IndexRepository indexRepository) {

		this.indexRepository = indexRepository;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.StructuredRepository#read(com.mubasher.market.repository.key.RestSearchKey)
	 */
	@Override
	public <T> List<T> read(RestSearchKey key) throws InvalidCriteriaException {

		return structuredRepository.read(key);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.StructuredRepository#write(com.mubasher.market.repository.models.RestModel)
	 */
	@Override
	public void write(RestModel model) {

		structuredRepository.write(model);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.StructuredRepository#createSimpleSelectionAdapter(java.lang.String)
	 */
	@Override
	public <T> SimpleSelectionAdapter<T> createSimpleSelectionAdapter(String modelName) {

		return structuredRepository.createSimpleSelectionAdapter(modelName);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.StructuredRepository#write(java.util.List)
	 */
	@Override
	public void write(List<RestModel> entities) {

		structuredRepository.write(entities);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.ModelRepository#createContent(java.lang.String, java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public Content createContent(String id, String name, String type,
			File contentFile) throws ContentException {

		try {

			return new Content(id, name, type, contentFile);

		} catch (Exception e) {

			throw new ContentException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.StructuredRepository#update(com.mubasher.market.repository.models.RestModel)
	 */
	@Override
	public void update(RestModel model) {

		structuredRepository.update(model);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.StructuredRepository#delete(com.mubasher.market.repository.models.RestModel)
	 */
	@Override
	public void delete(RestModel model) {

		structuredRepository.delete(model);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.ModelRepository#registerStructuredMapper(java.lang.String, java.lang.String, com.mubasher.market.repository.BasicRowMapper)
	 */
	@Override
	public <T> void registerMapper(String target, String modelName,
			BasicRowMapper<T> mapper) throws UnknowRepositoryTargetException {

		if(target.equalsIgnoreCase("structured")){

			registerStructuredMapper(modelName, mapper);

		} else if(target.equalsIgnoreCase("index")){

			registerIndexMapper(modelName, mapper);

		} else{

			throw new UnknowRepositoryTargetException(target);
		}
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.StructuredRepository#registerMapper(java.lang.String, com.mubasher.market.repository.BasicRowMapper)
	 */
	@Override
	public <T> void registerStructuredMapper(String modelName, BasicRowMapper<T> mapper) {

		structuredRepository.registerStructuredMapper(modelName, mapper);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.IndexRepository#registerIndexMapper(java.lang.String, com.mubasher.market.repository.BasicRowMapper)
	 */
	@Override
	public <T> void registerIndexMapper(String modelName,
			BasicRowMapper<T> mapper) {

		indexRepository.registerIndexMapper(modelName, mapper);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.IndexRepository#indexPorter(java.lang.Class)
	 */
	@Override
	public <T> IndexPorter<T> indexPorter(Class<T> type)
			throws CoreAnnotationIsNotPresent {

		return indexRepository.indexPorter(type);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.IndexRepository#indexPorter(java.lang.Class, com.mubasher.market.repository.IndexRepository.CoreLanguage)
	 */
	@Override
	public <T> IndexPorter<T> indexPorter(Class<T> type, CoreLanguage language)
			throws CoreAnnotationIsNotPresent {

		return indexRepository.indexPorter(type, language);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.IndexRepository#queryAdapter(java.lang.String)
	 */
	@Override
	public <T> IndexQueryAdapter<T> queryAdapter(String coreName) {

		return indexRepository.queryAdapter(coreName);
	}

}