package com.example.borodin.cecheckinout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.sql.Timestamp;

/**
 * Created by borodin on 2/10/2017.
 */

public class UpdateServer
{
	// taking from http://stackoverflow.com/questions/38978995/send-http-post-request-to-server-after-internet-re-connect
	// need to make it work
	// TODO: 2/10/2017  Send http post request to server after internet re-connect
	private static final String TAG = "UpdateServer_TEST";
	private Context that;

	public UpdateServer(Context context)
	{
		that = context;

		int time = (int) (System.currentTimeMillis()/1000);
		Timestamp t1 = new Timestamp(time);
		time += 1000;
		Timestamp t2 = new Timestamp(time);

		Utilities.print(TAG, "Time stemp is:" + t1.getTime());

		CeckOutData data = new CeckOutData(context, "CE Test Name", "Test Sete", t1, t2, 15);
		Utilities.print(TAG, "Just msde this one: " + data.getJeson());
		SendingDataToServer send = new SendingDataToServer(data);
		send.execute(that.getString(R.string.api_ip) + "/" + that.getString(R.string.api_responselog));

		// registerReceiver(connectionListener, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

	}

	private BroadcastReceiver connectionListener = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
			// if ((networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) & (networkConnectionHandler.isConnected()))
			// {
			// 	sendBroadcastMessage(getString(R.string.FORCED_NETWORK_CONNECTION_ESTABLISHED));
			// 	Utilities.print(TAG, "wifi connection established");
			// } else
			// {
			// 	sendBroadcastMessage(getString(R.string.FORCED_NETWORK_CONNECTION_LOST));
			// 	Utilities.print(TAG, "no wifi connection");
			// }
		}
	};
}
