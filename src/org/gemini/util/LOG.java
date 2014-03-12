package org.gemini.util;

import android.util.Log;
/***
 * Log Util
 * @author geminiwen
 *
 */
public class LOG {
	public static final boolean DEBUG = false;
	public static final int LEVEL_VERBONSE	= Log.VERBOSE;
	public static final int LEVEL_DEBUG		= Log.DEBUG;
	public static final int LEVEL_INFO		= Log.INFO;
	public static final int LEVEL_WARN		= Log.WARN;
	public static final int LEVEL_ERROR		= Log.ERROR;
	
	private static int level = LEVEL_VERBONSE;
	
	public static void setLevel(int level) {
		LOG.level = level;
		if( !DEBUG ) {
			LOG.level = LEVEL_ERROR + 1;
		}
	}

	public static void v(String tag, String msg) {
		if( level <= LEVEL_VERBONSE ) {
			Log.v(tag,msg);
		}
	}
	
	public static void d(String tag, String msg, Throwable t) {
		if( level <= LEVEL_DEBUG ) {
			Log.d(tag, msg, t);
		}
	}
	
	public static void d(String tag, String msg) {
		LOG.d(tag,msg,null);
	}
	
	public static void i(String tag, String msg, Throwable t) {
		if( level <= LEVEL_INFO ) {
			Log.i(tag,msg,t);
		}
	}
	
	public static void i(String tag, String msg) {
		LOG.i(tag,msg,null);
	}
	
	
	public static void w(String tag, String msg, Throwable t) {
		if( level <= LEVEL_WARN ) {
			Log.w(tag,msg,t);
		}
	}
	
	public static void w(String tag, String msg) {
		LOG.w(tag, msg);
	}
	
	public static void e(String tag, String msg, Throwable t) {
		if( level <= LEVEL_ERROR ) {
			Log.e(tag,msg,t);
		}
	}
	
	public static void e(String tag, String msg) {
		LOG.e(tag,msg,null);
	}
}
