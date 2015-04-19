package org.cradle.platform.httpgateway.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceFilterConfig {

	private List<String> filters = new ArrayList<String>();

	public void setFilters(List<String> filters) {
		this.filters = filters;
	}
	
	public void addToFilters(String filter){
		this.filters.add(filter);
	}
	
	public List<String> getFilters() {
		return filters;
	}
	
	public Filter buildChain(Filter firstFilter,
			Map<String, FilterFactory> filtersFactoryMap) {
		
		for (String type : filters) {
			
			FilterFactory factory = filtersFactoryMap.get(type);
			
			Filter filter =  factory.createFilter();
			
			filter.setNextFilter(firstFilter);

			firstFilter = filter;
		}
		
		return firstFilter;
	}
}
