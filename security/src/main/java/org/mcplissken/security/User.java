/**
 * 
 */
package org.mcplissken.security;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public interface User {
	
	public final String STATUS = "status";
	public final String ANONYMOUS = "anonymous";
	public final String HANDSHAKING = "handshaking";
	public final String AUTHENTICATED = "authenticated";
	
	public String userId();
	
	public String sessionId();
	
	public boolean hasSession();
	
	public boolean isAccessible(String method, String path);
	
	public void login(String email, String password, boolean rememberMe) throws AuthenticationFailureException;
	
	public void addSessionParameter(String name, Object value);
	
	public Object readSessionParameter(String name);
	
}
