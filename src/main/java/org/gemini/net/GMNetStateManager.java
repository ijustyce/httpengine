package org.gemini.net;

import org.apache.http.HttpHost;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 
 * @author luopeng (luopeng@staff.sina.com.cn)
 */
public class GMNetStateManager {
	private static Context sContext;

	public static NetState CUR_NETSTATE = NetState.Mobile;

	public enum NetState {
		Mobile, WIFI, NOWAY
	}

	public class NetStateReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			sContext = context;
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
					.getAction())) {
				WifiManager wifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				WifiInfo info = wifiManager.getConnectionInfo();
				if (!wifiManager.isWifiEnabled() || -1 == info.getNetworkId()) {
					CUR_NETSTATE = NetState.Mobile;
				}
			}
		}

	}

	public static HttpHost getAPN() {
		HttpHost proxy = null;
		Uri uri = Uri.parse("content://telephony/carriers/preferapn");
		Cursor cursor = null;
		if (null != sContext) {
			cursor = sContext.getContentResolver().query(uri, null, null, null,
					null);
		}
		if (cursor != null && cursor.moveToFirst()) {
			String proxyStr = cursor.getString(cursor.getColumnIndex("proxy"));
			if (proxyStr != null && proxyStr.trim().length() > 0) {
				proxy = new HttpHost(proxyStr, 80);
			}
			cursor.close();
		}
		return proxy;
	}
}
