package pl.edu.agh.eis.amlprofile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.aware.providers.WiFi_Provider.WiFi_Data;

public class ContextReciever extends BroadcastReceiver {
	
	private String getCurrentSsid(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo.isConnected()) {
			final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			if (connectionInfo != null)
			{
				String ssid = connectionInfo.getSSID();
				if (ssid != null && !ssid.equals("")) {
					return ssid;
				}
			}
		}
		return null;
	}	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String ssid = getCurrentSsid(context);
		
		if (ssid != null) {
			
			Log.w("reciever", ssid);
		} else {
			Log.w("reciever", "null");
		}
		
//		Cursor sensorData = context.getContentResolver().query(
//				WiFi_Data.CONTENT_URI, null, null, null, WiFi_Data.TIMESTAMP + " DESC LIMIT 1");
//		
//		String ssid = sensorData.getString(3);
//		Cursor sensorData = context.getContentResolver().query(
//				Accelerometer_Data.CONTENT_URI, tableColumns, whereCondition, whereArguments, order_by);

	}

}
