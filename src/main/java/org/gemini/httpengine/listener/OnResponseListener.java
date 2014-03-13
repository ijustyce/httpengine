package org.gemini.httpengine.listener;

import org.gemini.httpengine.net.GMHttpRequest;
import org.gemini.httpengine.net.GMHttpResponse;

public interface OnResponseListener {

	/***
	 * call back when response reached <br/>
	 * <strong> WARNING!!! the thread called is not indicated </strong>
	 * 
	 * @param response
	 * @param request
	 */
	public void onResponse(GMHttpResponse response, GMHttpRequest request);
}
