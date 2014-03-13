package org.gemini.httpengine.listener;

public interface OnProgressUpdateListener {
	/***
	 * called when download progress update
	 * 
	 * @param progress
	 *            indicates percentage, maximum is 100
	 * @param value
	 *            the value string
	 */
	public void onUpdate(int progress, String value);
}
