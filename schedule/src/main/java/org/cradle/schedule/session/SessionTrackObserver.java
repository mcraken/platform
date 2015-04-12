/**
 * 
 */
package org.cradle.schedule.session;

import org.cradle.reporting.SystemReportingService;
import org.cradle.schedule.tracks.Track;
import org.cradle.schedule.tracks.TrackException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 12, 2014
 */
public class SessionTrackObserver extends SessionObserver {

	private Track sessionTrack;

	/**
	 * @param sessionTrack the sessionTrack to set
	 */
	public void setSessionTrack(Track sessionTrack) {

		this.sessionTrack = sessionTrack;
	}

	protected void init(){

		try {

			sessionTrack.initTrack();

		} catch (TrackException e) {

			reportingService.exception(getClass().getSimpleName(), SystemReportingService.FILE, e);
		}
	}

	private void startSessionTrack() {
		
		try {

			sessionTrack.startTrack();

		} catch (TrackException e) {

			reportingService.exception(getClass().getSimpleName(), SystemReportingService.FILE, e);
		}
	}

	
	/* (non-Javadoc)
	 * @see org.cradle.schedule.session.SessionStatusCallback#inSession()
	 */
	@Override
	public void inSession() {
		
		startSessionTrack();
	}

	/* (non-Javadoc)
	 * @see org.cradle.schedule.session.SessionStatusCallback#outSession()
	 */
	@Override
	public void outSession() {
		
	}

	/* (non-Javadoc)
	 * @see org.cradle.schedule.session.SessionStatusCallback#startSession()
	 */
	@Override
	public void startSession() {
		
		startSessionTrack();
	}

	/* (non-Javadoc)
	 * @see org.cradle.schedule.session.SessionStatusCallback#endSession()
	 */
	@Override
	public void endSession() {
		
		try {
			
			sessionTrack.stopTrack();
			
		} catch (TrackException e) {
			
			reportingService.exception(getClass().getSimpleName(), SystemReportingService.FILE, e);
		}
	}

}
