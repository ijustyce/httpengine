package org.gemini.net;

import java.util.Set;
import java.util.TreeMap;

import org.apache.http.params.HttpParams;

public class GMHttpParameters implements HttpParams {
	private final TreeMap<String, Object>	httpParams;

	public GMHttpParameters() {
		httpParams = new TreeMap<String, Object>();
	}

	public GMHttpParameters(GMHttpParameters other) {
		this.httpParams = new TreeMap<String, Object>(other.httpParams);
	}

	@Override
	public HttpParams copy() {
		GMHttpParameters copy = new GMHttpParameters(this);
		return copy;
	}

	public int size() {
		return httpParams.size();
	}

	@Override
	public boolean getBooleanParameter(String name, boolean defaultValue) {
		Object value = httpParams.get(name);
		Boolean result = defaultValue;
		try {
			result = (Boolean) value;
		} catch (ClassCastException e) {
			result = defaultValue;
		} finally {
			result = result == null ? defaultValue : result;
		}
		return result;
	}

	@Override
	public double getDoubleParameter(String name, double defaultValue) {
		Object value = httpParams.get(name);
		Double result = defaultValue;
		try {
			result = ((Number) value).doubleValue();
		} catch (ClassCastException e) {
			result = defaultValue;
		}
		return result;
	}

	@Override
	public int getIntParameter(String name, int defaultValue) {
		Object value = httpParams.get(name);
		int result = defaultValue;
		try {
			result = ((Number) value).intValue();
		} catch (ClassCastException e) {
			result = defaultValue;
		}
		return result;
	}

	@Override
	public long getLongParameter(String name, long defaultValue) {
		Object value = httpParams.get(name);
		long result = defaultValue;
		try {
			result = ((Number) value).longValue();
		} catch (ClassCastException e) {
			result = defaultValue;
		}
		return result;
	}

	@Override
	public Object getParameter(String name) {
		return httpParams.get(name);
	}

	@Override
	public boolean isParameterFalse(String name) {
		Object value = httpParams.get(name);
		Boolean result = null;
		try {
			result = (Boolean) value;
		} catch (ClassCastException e) {
			result = true;
		}
		return result;
	}

	@Override
	public boolean isParameterTrue(String name) {
		Object value = httpParams.get(name);
		Boolean result = null;
		try {
			result = (Boolean) value;
		} catch (ClassCastException e) {
			result = false;
		}
		return result;
	}

	public String getStringParameter(String name) {
		Object obj = httpParams.get(name);
		return obj != null ? obj.toString() : null;
	}

	@Override
	public boolean removeParameter(String name) {
		return httpParams.remove(name) != null;
	}

	@Override
	public HttpParams setBooleanParameter(String name, boolean value) {
		httpParams.put(name, value);
		return this;
	}

	@Override
	public HttpParams setDoubleParameter(String name, double value) {
		httpParams.put(name, value);
		return this;
	}

	@Override
	public HttpParams setIntParameter(String name, int value) {
		httpParams.put(name, value);
		return this;
	}

	@Override
	public HttpParams setLongParameter(String name, long value) {
		httpParams.put(name, value);
		return this;
	}

	@Override
	public HttpParams setParameter(String name, Object value) {
		httpParams.put(name, value);
		return this;
	}

	public Set<String> getNames() {
		return httpParams.keySet();
	}

	@Override
	public String toString() {
		return httpParams.toString();
	}

}