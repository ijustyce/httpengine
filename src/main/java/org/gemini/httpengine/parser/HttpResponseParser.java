package org.gemini.httpengine.parser;

public interface HttpResponseParser {
	public Object handleResponse(byte[] response);
}
