package org.gemini.parser;

import java.io.IOException;

import org.gemini.net.GMHttpParameters;

public interface HttpRequestParser {
	public byte[] parse(GMHttpParameters httpParams) throws IOException;

	public String pareContentType();
}
