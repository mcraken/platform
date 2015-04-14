package org.cradle.osgi.vertx.handlers;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public final class FileRequestHandler implements Handler<HttpServerRequest> {

	private String fileRoot;
	private String webRoot;

	public FileRequestHandler(String fileRoot, String webRoot) {
		this.fileRoot = fileRoot;
		this.webRoot = webRoot;
	}

	public void handle(HttpServerRequest req) {
        
		String path = req.path();
		
        String file = getTargetFile(path);
        
        String targetPath = fileRoot + "/" + file;
        
        req.response().sendFile(targetPath);
    }
	
	private String getTargetFile(String path){
		
		return path.substring(webRoot.length(), path.length());
	}

}