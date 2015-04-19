package org.cradle.platform.httpgateway.restful.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RESTfullServiceFilterConfig {

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
	
	public RESTfulFilter buildChain(RESTfulFilter firstFilter,
			Map<String, RESTfulFilterFactory> filtersFactoryMap) {
		
		for (String type : filters) {
			
			RESTfulFilterFactory factory = filtersFactoryMap.get(type);
			
			RESTfulFilter filter =  factory.createFilter();
			
			filter.setNextFilter(firstFilter);

			firstFilter = filter;
		}
		
		return firstFilter;
	}
}
