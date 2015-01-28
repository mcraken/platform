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
package org.mcplissken.repository.models.content;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.tika.Tika;
import org.mcplissken.repository.models.RestModel;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 30, 2014
 */
@Table("content")
public class Content implements RestModel{
	
	@PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String id;
	
	@PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String name;
	
	@PrimaryKeyColumn(ordinal = 2, type = PrimaryKeyType.PARTITIONED)
	private String type;

	private Map<String, String> metainfo;
	private ByteBuffer data;
	
	public Content(String id, String name, String type, File result) throws Exception{
		this.id = id;
		this.name = name;
		this.type = type;
		this.metainfo = new HashMap<>();
		
		String mimeType = new Tika().detect(result);
		
		String extension = mimeType.split("/")[1];
		
		metainfo.put("mimeType", mimeType);
		
		metainfo.put("extension", extension);
		
	}

	public Content(String id, String name, String type,
			Map<String, String> metaInfo, ByteBuffer data) {
		
		this.id = id;
		this.name = name;
		this.type = type;
		this.metainfo = metaInfo;
		this.data = data;
	}
	
	public String getMimeType(){
		
		return metainfo.get("mimeType");
	}
	
	public String getExtension(){
		return metainfo.get("extension");
	}

	public long getSize(){
		
		return Long.parseLong(metainfo.get("size"));
	}
	
	/**
	 * @return the data
	 */
	public byte[] getData() {
		
		byte[] result = new byte[data.remaining()];
		
		data.get(result);
		
		return result;
		
	}
	
	public boolean isImage(){
		
		String fileType = getMimeType().split("/")[0];
		
		if(fileType.equals("image") || fileType.equals("application"))
			return true;
		else 
			return false;
	}
	
	public void writeAsImage(File result) throws IOException{
		
		BufferedImage originalImage = ImageIO.read(result);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		ImageIO.write(originalImage, getExtension(), baos);
		
		baos.flush();
		
		byte[] imageInByte = baos.toByteArray();
		
		data = ByteBuffer.wrap(imageInByte);
		
		baos.close();
	}
	
	public void writeAsFile(File result) throws IOException{
		
		Path path = Paths.get(result.getPath());
		
		byte[] bytes = Files.readAllBytes(path);
		
		data = ByteBuffer.wrap(bytes);
		
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.repository.models.RestModel#getUniqueId()
	 */
	@Override
	public Object getUniqueId() {
		return id + name;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
}
