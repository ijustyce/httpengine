package org.gemini.httpengine.parser;

import java.io.IOException;

import org.gemini.httpengine.net.GMHttpParameters;

public interface HttpRequestParser {
	public byte[] parse(GMHttpParameters httpParams) throws IOException;

	public String pareContentType();
}
