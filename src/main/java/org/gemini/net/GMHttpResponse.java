package org.gemini.net;

import java.io.UnsupportedEncodingException;

import org.gemini.parser.HttpResponseParser;
import org.json.JSONException;
import org.json.JSONObject;

/***
 * Http response get from GMHttpEngine request
 * 
 * @author Gemini
 * 
 */
public class GMHttpResponse {

	private byte[] rawData;
	private HttpResponseParser responseParser;

	private boolean isFail = false;

	public GMHttpResponse(byte[] data) {
		if (data != null) {
			this.rawData = data;
		} else {
			isFail = true;
		}

	}

	public byte[] getRawData() {
		if (isFail) {
			throw new RuntimeException("Request is failed");
		}
		return this.rawData;
	}

	public HttpResponseParser getResponseParser() {
		return responseParser;
	}

	public void setResponseParser(HttpResponseParser responseParser) {
		this.responseParser = responseParser;
	}

	public String parseAsString() {
		return parseAsString("UTF-8");
	}

	public String parseAsString(String encode) {
		String ret = null;
		if (isFail) {
			throw new RuntimeException("Request is failed");
		}
		try {
			ret = new String(this.rawData, encode);
		} catch (UnsupportedEncodingException e) {
			isFail = true;
			throw new RuntimeException("Request is failed");
		}
		return ret;
	}

	public JSONObject parseAsJSON() {
		String result = parseAsString();
		JSONObject obj = null;
		try {
			obj = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public Object parseData() {
		if (isFail) {
			throw new RuntimeException("Request is failed");
		}
		return responseParser.handleResponse(this.rawData);
	}
}
