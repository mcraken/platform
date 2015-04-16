/**
 * 
 */
package org.cradle.platform.gateway;

import java.nio.ByteBuffer;

import org.cradle.platform.gateway.restful.exception.PathNotAccessibleException;
import org.cradle.platform.gateway.restful.exception.RESTfulException;
import org.cradle.security.AuthenticationFailureException;
import org.cradle.security.User;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 11, 2014
 */
public interface HttpAdapter {
	
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_LANGUAGE = "Content-Language";
	public static final String COOKIE = "Cookie";
	public static final String HEADER_SEP = ";";
	public static final String COOKIE_PART_SEP = "=";
	public static final String JSESSIONID = "JSESSIONID";
	
	public User getUser();
	
	public String method();
	
	public String path();
	
	public void setUser(User user);
	
	public void login(String email, String password, boolean rememberMe) throws AuthenticationFailureException;
	
	public void isPathAccessible() throws PathNotAccessibleException;
	
	public String readParameter(String name);
	
	public void writeHeader(String name, String value);
	
	public String readHeader(String name);
	
	public void writeResponse(ByteBuffer buffer);
	
	public String sessionId();
	
	public void setCookie(String name, String value);
	
	public void exception(RESTfulException exception);
	
	public void error(Exception exception);
	
	public String getContentType(); 
	
	public String getContentLangauge();
	
	public void sendRedirect(String uri);
}
