package com.example.borodin.cecheckinout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by borodin on 11/11/2016.
 */

public class checkDBForUpdates extends AsyncTask<String, Integer, String>
{
	private static final String TAG = "checkDBForUpdate_TEST";
	private final Context That;
	private ProgressBar progressBar;
	private SharedPreferences preferences;
	private OnDataUptaded listenerupdate;
	public boolean isuptodate;

	public checkDBForUpdates (Context context, ProgressBar b, OnDataUptaded onupdate)
	{
		preferences = context.getSharedPreferences(context.getString(R.string.setings_for_update), Context.MODE_PRIVATE);
		That = context;
		listenerupdate = onupdate;
		progressBar = b;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		progressBar.setVisibility(View.VISIBLE);
		isuptodate = false;
	}

	@Override
	protected String doInBackground(String... params)
	{
		String projects;
		{
			String stream = null;
			String urlString = params[0];

			HTTPDataHandler hh = new HTTPDataHandler();
			stream = hh.GetHTTPData(urlString);
			projects = updatedProjects(stream);
		}
		return projects;
	}

	@Override
	protected void onPostExecute(String s)
	{
		isuptodate = s.isEmpty();
		if (!isuptodate)
		{
			String maseg = That.getString(R.string.dboutdatedmeseg) + s;
			Utilities.print(TAG, maseg );

			Toast toast = Toast.makeText(That, maseg, Toast.LENGTH_LONG);
			toast.show();

			listenerupdate.checkFinished(s.isEmpty());
		}
		progressBar.setVisibility(View.INVISIBLE);
	}

	public String updatedProjects(String response)
	{
		String projectUp_name;
		String projectUp_time;
		int    projectUp_num;
		String projectsUpdated = "";

		if (response != null)
		{
			try
			{
				JSONArray arrProjects = new JSONArray(response);
				projectUp_num = arrProjects.length();
				Log.d(TAG, "get " + projectUp_num + " projects");

				for (int i = 0; i < projectUp_num; i++)
				{
					JSONObject object = (JSONObject) arrProjects.get(i);
					Log.d(TAG, "Project Id: " + object.get("projectID"));
					Log.d(TAG, "Project Name: " + object.get("projectNAME"));
					Log.d(TAG, "Project Time Updated: " + object.get("projectTIMEUP"));

					projectUp_name = object.getString("projectNAME");
					projectUp_time = object.getString("projectTIMEUP");

					if ( !preferences.getString(projectUp_name, "").equals(projectUp_time) )
					{
						projectsUpdated += projectUp_name;
						if (i < projectUp_num - 1) projectsUpdated += ", ";
					}
					// I'm not sure if I need to know the id of the project the ben changed
					// object.getInt("projectID");
				}

			} catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return projectsUpdated;
	}
}
