/**
 * @author Sherief Shawky(raken123@yahoo.com)
 */
package org.mcplissken.repository.key;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mcplissken.repository.key.exception.InvalidCriteriaSyntaxException;

/**
 * @author Sherief Shawky(raken123@yahoo.com)
 *
 */
public class RestSearchKey {
	
	public static final String SEACRH_KEY = "search_key";
	
	private int start;
	private int count;
	private String select;
	private String resourceName;
	private String queryName;
	private ArrayList<RestCriteria> criterias;
	
	private boolean selectionRequired;
	
	public RestSearchKey(){
		
	}
	
	public RestSearchKey(String resourceName, RestCriteria...crts){
		
		this.resourceName = resourceName;
		
		this.criterias = new ArrayList<RestCriteria>();
		
		for(RestCriteria crt:crts)
			this.criterias.add(crt);
	}
	
	/**
	 * @return the selectionRequired
	 */
	public boolean isSelectionRequired() {
		return selectionRequired;
	}
	
	/**
	 * @return the queryName
	 */
	public String getQueryName() {
		return queryName;
	}
	
	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public ArrayList<RestCriteria> getCriterias() {
		return criterias;
	}
	
	public void setCriterias(ArrayList<RestCriteria> criterias) {
		this.criterias = criterias;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void parseAllCriterias() throws InvalidCriteriaSyntaxException{
		
		for(RestCriteria crt : criterias)
			try {
				
				crt.parse();
				
			} catch (Exception e) {
				
				throw new InvalidCriteriaSyntaxException(e);
			}
		
	}
	
	public String hasProperty(String name){
		
		for(RestCriteria crt : criterias){
			if(crt.propertyNameEquals(name))
				return crt.getCrudeValue();
		}
		
		return null;
	}
	
	public Iterator<RestCriteria> criteriasIterator(){
		
		return criterias.iterator();
	}
	
	public String[] parseColumns(){
		return select.split(",");
	}
	
	public Iterator<RestProjection> projectionIterator(){
		
		if(select == null)
			return null;
		
		List<RestProjection> projections = new ArrayList<RestProjection>();
		
		StringTokenizer projectionList = new StringTokenizer(select, ";");

		while(projectionList.hasMoreTokens()){
			
			projections.add(new RestProjection(projectionList.nextToken()));
		}
		
		return projections.iterator();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object paramObject) {
		
		RestSearchKey other = (RestSearchKey) paramObject;
		
		if(this == other)
			return true;
		
		return new EqualsBuilder()
		.append(this.resourceName, other.resourceName)
		.append(this.start, other.start)
		.append(this.count, other.count)
		.append(this.criterias.toArray(), other.criterias.toArray())
		.isEquals(); 
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		
		 return new HashCodeBuilder()
		 .append(resourceName)
		 .append(start)
		 .append(count)
		 .append(criterias.toArray())
		 .toHashCode();
	}
}
