/**
 * 
 */
package org.cradle.platform.vertx.handlers;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.cradle.platform.vertx.httpgateway.AsyncInputStream;
import org.cradle.reporting.SystemReportingService;
import org.cradle.repository.ModelRepository;
import org.cradle.repository.exception.NoResultException;
import org.cradle.repository.models.content.Content;
import org.cradle.repository.query.SimpleSelectionAdapter;
import org.vertx.java.core.Handler;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.streams.Pump;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 11, 2014
 */
public class ContentRequestHandler implements Handler<HttpServerRequest>{

	private String contentRoot;
	private SystemReportingService reportingService;
	private ModelRepository repository;

	public ContentRequestHandler(
			ModelRepository repository,
			String contentRoot, 
			SystemReportingService reportingService)  {

		this.contentRoot = contentRoot;
		
		this.repository = repository;
		
		this.reportingService = reportingService;

	}

	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(final HttpServerRequest request) {

		try {

			String path = request.path();

			String contentpath = path.substring(contentRoot.length(), path.length());
			
			Content content = readContentObject(contentpath);
			
			final ByteArrayInputStream input = new ByteArrayInputStream(content.getData());
			
			request.response().headers().add("Content-Length", "" + input.available());
			
			request.response().headers().add("Content-Type",  content.getMimeType());
			
			AsyncInputStream asyncInputStream = new AsyncInputStream(input);

			Pump pump = Pump.createPump(asyncInputStream, request.response());

			asyncInputStream.endHandler(new VoidHandler() {

				@Override
				protected void handle() {
					
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					request.response().end();

				}
			});

			asyncInputStream.exceptionHandler(new Handler<Throwable>() {

				@Override
				public void handle(Throwable e) {

					reportingService.exception(getClass().getSimpleName(), SystemReportingService.CONSOLE, e);
				}
			});

			pump.start();

		} catch(NoResultException e){

			request.response().setStatusCode(404);

			request.response().setStatusMessage(e.getMessage());
			
			request.response().end();
			
			reportingService.exception(getClass().getSimpleName(), SystemReportingService.CONSOLE, e);
			
		} catch (Exception e) {
			
			request.response().setStatusCode(500);

			request.response().end();
			
			reportingService.exception(getClass().getSimpleName(), SystemReportingService.CONSOLE, e);
		}
	}

	private Content readContentObject(String contentpath)
			throws NoResultException {
		
		String[] contentPathParts = contentpath.split("/");
		
		String type = contentPathParts[0];
		
		String id  = contentPathParts[1];
		
		String contentName = contentPathParts[2].split("\\.")[0];
		
		SimpleSelectionAdapter<Content> selectionAdapter = repository.createSimpleSelectionAdapter("content");
		
		reportingService.info(getClass().getSimpleName(), SystemReportingService.CONSOLE, type + "/" + id + "/" + contentName);
		
		Content content = selectionAdapter.eq("id", id).eq("name", contentName).eq("type", type).result().get(0);
		
		return content;
	}

}
