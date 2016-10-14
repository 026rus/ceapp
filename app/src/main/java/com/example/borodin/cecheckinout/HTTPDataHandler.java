package com.example.borodin.cecheckinout;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by borodin on 6/10/2016.
 */
public class HTTPDataHandler
{
	private final static String TAG = "HTTPDataHandler_TEST";
	static String stream = null;

	public String GetHTTPData(String urlString)
	{
		try
		{
			URL url = new URL(urlString);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			Log.d(TAG, "openConnectiond to the server: " + urlConnection.getResponseCode() + " = " + urlString);
			if(urlConnection.getResponseCode() == 200)
			{
				Log.d(TAG, "Connection is Good" );
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());

				BufferedReader r = new BufferedReader(new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();
				String line;

				while ( (line = r.readLine()) != null )
				{
					sb.append(line);
				}
				stream = sb.toString();

				urlConnection.disconnect();

				Log.d(TAG, "Disconnected : " + stream);
			}

		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return stream;
	}
}
