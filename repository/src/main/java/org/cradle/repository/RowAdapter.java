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

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 26, 2015
 */
public interface RowAdapter {
	
	public String getString(String name);
	
	public Integer getInt(String name);
	
	public Long getLong(String name);
	
	public Double getDouble(String name);
	
	public  <T> List<T> getList(String name, Class<T> type);
	
	public <K, V> Map<K, V> getMap(String name, Class<K> keyType, Class<V> valueType);
	
	public ByteBuffer getBytes(String name);
	
	public UUID getUUID(String name);
}
