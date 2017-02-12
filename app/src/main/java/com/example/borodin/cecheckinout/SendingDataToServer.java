package com.example.borodin.cecheckinout;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by borodin on 2/10/2017.
 */

public class SendingDataToServer extends AsyncTask<String, Integer, String>
{
	private static final String TAG = "SendingDataToServer_TEST";
	private CeckOutData data;

	public SendingDataToServer(CeckOutData data)
	{
		this.data = data;
	}

	@Override
	protected String doInBackground(String... params)
	{
		Utilities.print(TAG, "Just msde this one: " + data.getJeson());
		return POST(params[0], data);
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result)
	{
		Utilities.print(TAG, "Data sended : " + result);
	}


	private String POST(String inURL, CeckOutData mydata)
	{
		String result = "";
		try
		{
			URL url =  new URL(inURL);
			Utilities.print(TAG, "URL is: " + inURL);
			Utilities.print(TAG, "Data to send: " + mydata.getJeson());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(mydata.getJeson());
			writer.flush();
			writer.close();
			os.close();

			int respodseCode = conn.getResponseCode();

			if (respodseCode == HttpURLConnection.HTTP_OK)
			{
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line=br.readLine()) != null) result += line;

				if(br != null)
					try { br.close(); }
					catch (Exception e) { Utilities.print(TAG, "Cant send POST :( " + e.getMessage()); }
			}
			else
			{
				result = "EMPTY";
			}
			conn.connect();

			// Closing conntction and all streams
			if(writer != null)
				try { writer.close(); }
				catch (Exception e) { Utilities.print(TAG, "Cant send POST :( " + e.getMessage()); }
			if(os != null)
				try { os.close(); }
				catch (Exception e) { Utilities.print(TAG, "Cant send POST :( " + e.getMessage()); }


			// delete all the local data after it wos successful sanded
			Utilities.print(TAG, "Closing connection and deletion all the stored data");
			data.deleteCechOutData();
		}
		catch (Exception e)
		{
			Utilities.print(TAG, "Cant send POST :( " + e.getMessage());
		}

		return result;
	}

	public CeckOutData getData()
	{
		return data;
	}

	public void setData(CeckOutData data)
	{
		this.data = data;
	}
}
