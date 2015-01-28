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
package org.mcplissken.localization;

import org.mcplissken.repository.index.Core;
import org.mcplissken.repository.index.Index;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 21, 2015
 */
@Core("localized")
public class Localized {
	
	@Index
	private String id;
	
	@Index
	private String language;
	
	@Index
	private String desc;

	public Localized(String id, String language, String desc) {
		this.id = id;
		this.language = language;
		this.desc = desc;
	}

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

