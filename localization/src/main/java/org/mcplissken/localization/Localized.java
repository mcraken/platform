/**
 * 
 */
package org.mcplissken.localization;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 21, 2015
 */
public class Localized {
	
	private String id;
	
	private String language;
	
	private String desc;

	public String getId() {
		return id;
	}

	public String getLanguage() {
		return language;
	}

	public String getDesc() {
		return desc;
	}
	
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
