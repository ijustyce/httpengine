package org.gemini.net;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.gemini.listener.OnProgressUpdateListener;
import org.gemini.listener.OnResponseListener;
import org.gemini.parser.DefaultHttpRequestParser;
import org.gemini.parser.HttpRequestParser;

import android.content.Context;

/**
 * Request Object for http engine
 * 
 * @author Gemini
 * 
 */
public class GMHttpRequest {
	public static final Long					REQUEST_ID_UNIQUE	= 0xFFFFFFFFl;

	private final Context						context;
	private String								uri;
	private Map<String, Object>					userData;
	private final Map<String, String>			headers;
	private String								taskId;
	private GMHttpParameters					httpParameters;
	private Boolean								isRawData;
	private String								method;
	private WeakReference<OnResponseListener>	onResponseListener;
	private OnProgressUpdateListener			onProgressUpdateListener;
	private HttpRequestParser					requestParser;
	private Boolean								isCanceled;

	public GMHttpRequest(Context context) {
		this.context = context;
		this.isRawData = false;
		this.isCanceled = false;
		this.requestParser = new DefaultHttpRequestParser();
		this.headers = new HashMap<String, String>();
		this.method = GMHttpEngine.HTTP_GET;
	}

	public GMHttpRequest(Context context, String uri,
			GMHttpParameters httpParameters) {
		this(context);
		this.uri = uri;
		this.httpParameters = httpParameters;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Map<String, Object> getUserData() {
		return userData;
	}

	public void setUserData(Map<String, Object> userData) {
		this.userData = userData;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public GMHttpParameters getHttpParameters() {
		return httpParameters;
	}

	public void setHttpParameters(GMHttpParameters httpParameters) {
		this.httpParameters = httpParameters;
	}

	public Boolean getIsRawData() {
		return isRawData;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getContentType() {
		return this.requestParser.pareContentType();
	}

	public void setIsRawData(Boolean isRawData) {
		this.isRawData = isRawData;
	}

	public Context getContext() {
		return context;
	}

	public OnResponseListener getResponseListener() {
		return onResponseListener.get();
	}

	public void setOnResponseListener(OnResponseListener responseListener) {
		this.onResponseListener = new WeakReference<OnResponseListener>(
				responseListener);
	}

	public OnProgressUpdateListener getOnProgressUpdateListener() {
		return onProgressUpdateListener;
	}

	public void setOnProgressUpdateListener(
			OnProgressUpdateListener onProgressUpdateListener) {
		this.onProgressUpdateListener = onProgressUpdateListener;
	}

	public HttpRequestParser getRequestParser() {
		return requestParser;
	}

	public void setRequestParser(HttpRequestParser requestParser) {
		this.requestParser = requestParser;
	}

	public byte[] getHttpEntity() throws IOException {
		return this.requestParser.parse(httpParameters);
	}

	public void addHeader(String key, String value) {
		this.headers.put(key, value);
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	public void cancel() {
		this.isCanceled = true;
	}

	public Boolean isCancel() {
		return this.isCanceled;
	}

}
