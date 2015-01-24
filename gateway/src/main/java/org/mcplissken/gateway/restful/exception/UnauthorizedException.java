/**
 * 
 */
package org.mcplissken.gateway.restful.exception;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 26, 2014
 */
public class UnauthorizedException extends RESTfulException {

	/**
	 * @param e
	 */
	public UnauthorizedException(Throwable e) {
		super("Unauthorized operation detected", e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.mubasher.gateway.restful.exception.RESTfulException#getErrorCode()
	 */
	@Override
	public int getErrorCode() {
		return 401;
	}

}
