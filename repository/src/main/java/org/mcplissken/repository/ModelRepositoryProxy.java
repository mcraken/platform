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
import org.mcplissken.repository.index.IndexDocumentObjectFactory;
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
	 * @see org.mcplissken.repository.StructuredRepository#read(org.mcplissken.repository.key.RestSearchKey)
	 */
	@Override
	public <T> List<T> read(RestSearchKey key) throws InvalidCriteriaException {
		
		return structuredRepository.read(key);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.StructuredRepository#write(org.mcplissken.repository.models.RestModel)
	 */
	@Override
	public void write(RestModel model) {
		
		structuredRepository.write(model);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.StructuredRepository#createSimpleSelectionAdapter(java.lang.String)
	 */
	@Override
	public <T> SimpleSelectionAdapter<T> createSimpleSelectionAdapter(String modelName) {
		
		return structuredRepository.createSimpleSelectionAdapter(modelName);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.StructuredRepository#write(java.util.List)
	 */
	@Override
	public void write(List<RestModel> entities) {
		
		structuredRepository.write(entities);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.IndexRepository#indexPorter(java.lang.String, java.lang.String[])
	 */
	@Override
	public IndexPorter indexPorter(String coreName) {
		
		return indexRepository.indexPorter(coreName);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.IndexRepository#queryAdapter(java.lang.String, org.mcplissken.repository.index.IndexDocumentObjectFactory, java.lang.String[])
	 */
	@Override
	public <T> IndexQueryAdapter<T> queryAdapter(String coreName,
			IndexDocumentObjectFactory<T> documentFactory, String[] fieldNames) {
		
		return indexRepository.queryAdapter(coreName, documentFactory, fieldNames);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.ModelRepository#createContent(java.lang.String, java.lang.String, java.lang.String, java.io.File)
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
	 * @see org.mcplissken.repository.StructuredRepository#update(org.mcplissken.repository.models.RestModel)
	 */
	@Override
	public void update(RestModel model) {
		
		structuredRepository.update(model);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.StructuredRepository#delete(org.mcplissken.repository.models.RestModel)
	 */
	@Override
	public void delete(RestModel model) {
		
		structuredRepository.delete(model);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.StructuredRepository#registerMapper(java.lang.String, org.mcplissken.repository.BasicRowMapper)
	 */
	@Override
	public <T> void registerMapper(String modelName, BasicRowMapper<T> mapper) {
		
		structuredRepository.registerMapper(modelName, mapper);
	}

}
