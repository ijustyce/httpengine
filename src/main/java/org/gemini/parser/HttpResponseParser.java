package org.gemini.parser;

public interface HttpResponseParser {
	public Object handleResponse(byte[] response);
}
