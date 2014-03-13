package org.gemini.httpengine.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.gemini.httpengine.net.GMHttpParameters;

public class DefaultHttpRequestParser implements HttpRequestParser {

	/***
	 * parse form body
	 */
	@Override
	public byte[] parse(GMHttpParameters httpParams) throws IOException {
		Set<String> keySet = httpParams.getNames();
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String name : keySet) {
			String value = httpParams.getStringParameter(name);
			NameValuePair p = new BasicNameValuePair(name, value);
			nvps.add(p);
		}
		HttpEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
		InputStream is = entity.getContent();
		int avaliable = is.available();
		byte[] buffer = new byte[avaliable];
		is.read(buffer);
		is.close();
		return buffer;
	}

	@Override
	public String pareContentType() {
		return "application/x-www-form-urlencoded; charset=UTF-8";
	}

}
