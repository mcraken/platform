/**
 * 
 */
package org.mcplissken.repository;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 26, 2015
 */
public abstract class BasicRowMapper<T> {
	
	private ModelRepository repository;
	private String modelName;
	
	public void setRepository(ModelRepository repository) {
		this.repository = repository;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void init(){
		
		repository.registerMapper(modelName, this);
	}
	
	public abstract T map(RowAdapter row);
	
}
