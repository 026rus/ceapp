package com.example.borodin.cecheckinout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by borodin on 6/30/2016.
 */
public class DownloadFile extends AsyncTask<String, Integer, String>
{
	private final static String TAG = "DownloadFile_TEST";
	public static final int progress_bar_type = 0;
	private ProgressDialog progressDialog;
	private ProgressBar progressBar;
	private OnFileDownloaded listener;
	private String filemane;

	public DownloadFile(ProgressBar bar)
	{
		progressBar = bar;
	}

	public DownloadFile(ProgressBar bar, OnFileDownloaded listener)
	{
		progressBar = bar;
		this.listener = listener;
	}

	public void setFileDownloadListener(OnFileDownloaded listener)
	{
		this.listener = listener;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected String doInBackground(String... params)
	{
		int count;

		try
		{
	 		Utilities.print(TAG, "file path: " + params[0]);
			String[] paths = params[0].split("/");
			filemane = paths[paths.length-1];
			// String outFile = String.format("%s%s", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), Resources.getSystem().getString(R.string.manfilenam));
			String outFile = String.format("%s/%s", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), filemane);
			Utilities.print(TAG, "PATH to the file: " + outFile);

			URL url = new URL(params[0]);
			URLConnection connection = url.openConnection();
			connection.connect();

			int LenghtOffile = connection.getContentLength();

			InputStream inputs = new BufferedInputStream(url.openStream(), 8192);


			OutputStream outputs = new FileOutputStream(outFile);
			byte data[] = new byte[1024];
			long total = 0;

			while ( (count = inputs.read(data)) != -1 )
			{
				total += count;
				publishProgress( (int) ( (total * 100 ) / LenghtOffile));
				outputs.write(data, 0, count);
			}

			outputs.flush();
			outputs.close();
			inputs.close();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values)
	{
		super.onProgressUpdate(values);
		if (values[0] > 0)
		{
			progressBar.setProgress(values[0]);
		}
	}

	@Override
	protected void onPostExecute(String s)
	{
		super.onPostExecute(s);
		progressBar.setVisibility(View.INVISIBLE);
		listener.fileDownloadCompleted(filemane);
	}
}
