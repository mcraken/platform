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
package org.cradle.repository;

import java.io.File;
import java.util.List;

import org.cradle.repository.exception.ContentException;
import org.cradle.repository.index.CoreAnnotationIsNotPresent;
import org.cradle.repository.index.IndexException;
import org.cradle.repository.index.IndexPorter;
import org.cradle.repository.index.IndexQueryAdapter;
import org.cradle.repository.key.RestSearchKey;
import org.cradle.repository.key.exception.InvalidCriteriaException;
import org.cradle.repository.models.content.Content;
import org.cradle.repository.query.SimpleSelectionAdapter;

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

	@Override
	public <T> List<T> read(RestSearchKey key) throws InvalidCriteriaException {

		return structuredRepository.read(key);
	}

	@Override
	public <T> void write(T model) {

		structuredRepository.write(model);
	}

	@Override
	public <T> SimpleSelectionAdapter<T> createSimpleSelectionAdapter(String modelName) {

		return structuredRepository.createSimpleSelectionAdapter(modelName);
	}

	@Override
	public <T> void write(List<T> entities) {

		structuredRepository.write(entities);
	}

	@Override
	public Content createContent(String id, String name, String type,
			File contentFile) throws ContentException {

		try {

			return new Content(id, name, type, contentFile);

		} catch (Exception e) {

			throw new ContentException(e);
		}
	}

	@Override
	public <T> void update(T model) {

		structuredRepository.update(model);
	}

	@Override
	public <T> void delete(T model) {

		structuredRepository.delete(model);
	}

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

	@Override
	public <T> void registerStructuredMapper(String modelName, BasicRowMapper<T> mapper) {

		structuredRepository.registerStructuredMapper(modelName, mapper);
	}

	@Override
	public <T> void registerIndexMapper(String modelName,
			BasicRowMapper<T> mapper) {

		indexRepository.registerIndexMapper(modelName, mapper);
	}

	@Override
	public <T> IndexPorter<T> indexPorter(Class<T> type)
			throws CoreAnnotationIsNotPresent {

		return indexRepository.indexPorter(type);
	}

	@Override
	public <T> IndexPorter<T> indexPorter(Class<T> type, CoreLanguage language)
			throws CoreAnnotationIsNotPresent {

		return indexRepository.indexPorter(type, language);
	}

	@Override
	public <T> IndexQueryAdapter<T> queryAdapter(String coreName) {

		return indexRepository.queryAdapter(coreName);
	}

	@Override
	public <T> void deleteIndex(List<T> models) throws IndexException {
		
		indexRepository.deleteIndex(models);
	}

	@Override
	public <T> List<T> readIndex(RestSearchKey key) throws IndexException {
		
		return indexRepository.readIndex(key);
	}

	@Override
	public <T> void writeIndex(T model) throws IndexException {
		
		indexRepository.writeIndex(model);
	}

	@Override
	public <T> void writeIndex(List<T> models) throws IndexException {
		
		indexRepository.writeIndex(models);
	}

	@Override
	public <T> void deleteIndex(T model) throws IndexException {
		
		indexRepository.deleteIndex(model);
	}

}