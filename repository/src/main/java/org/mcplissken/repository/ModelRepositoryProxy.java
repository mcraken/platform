/**
 * 
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
 * @email 	sherif.shawki@mubasher.info
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
	public List<?> read(RestSearchKey key) throws InvalidCriteriaException {
		
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
	public SimpleSelectionAdapter createSimpleSelectionAdapter(String modelName) {
		
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
	 * @see com.mubasher.market.repository.IndexRepository#indexPorter(java.lang.String, java.lang.String[])
	 */
	@Override
	public IndexPorter indexPorter(String coreName) {
		
		return indexRepository.indexPorter(coreName);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.IndexRepository#queryAdapter(java.lang.String, com.mubasher.market.repository.index.IndexDocumentObjectFactory, java.lang.String[])
	 */
	@Override
	public <T> IndexQueryAdapter<T> queryAdapter(String coreName,
			IndexDocumentObjectFactory<T> documentFactory, String[] fieldNames) {
		
		return indexRepository.queryAdapter(coreName, documentFactory, fieldNames);
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

}
