package com.hpceapp.borodin.cecheckinout;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by borodin on 6/10/2016.
 */
public class SyncSQLDB extends AsyncTask<String, Integer, String>
{
	private static final String TAG = "SyncSQLDB_TEST";
	private SQLiteDatabase db;
	private ProjectSQLiteOpenHelper phelper;
	private final Context That;
	private ProgressBar progressBar;
	private OnDataUptaded listener;
	private SharedPreferences preferences;

	public SyncSQLDB (Context context, ProgressBar b, OnDataUptaded listener)
	{
		preferences = context.getSharedPreferences(context.getString(R.string.setings_for_update), Context.MODE_PRIVATE);
		That = context;
		progressBar = b;
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
		{
			String stream = null;
			String urlString = params[0];

			HTTPDataHandler hh = new HTTPDataHandler();
			stream = hh.GetHTTPData(urlString);

			updateSQLiteProjects(stream);
		}
		{
			String stream = null;
			String urlString = params[1];

			HTTPDataHandler hh = new HTTPDataHandler();
			stream = hh.GetHTTPData(urlString);

			updateSQLiteQuestions(stream);
		}
		{
			Utilities.print(TAG, "The number of arguments is : " + params.length);
			if (params.length > 2)
			{
				String stream = null;
				String urlString = params[2];
				HTTPDataHandler hh = new HTTPDataHandler();
				stream = hh.GetHTTPData(urlString);
				updateSQLiteInQuestions(stream);
			}
		}
		return "done";
	}

	@Override
	protected void onPostExecute(String s)
	{
		progressBar.setVisibility(View.INVISIBLE);
	}

	public void updateSQLiteProjects(String response)
	{
		if (response != null)
		{
			try
			{
				SharedPreferences.Editor editor = preferences.edit();
				JSONArray arrProjects = new JSONArray(response);
				Log.d(TAG, "get " + arrProjects.length() + " projects");
				phelper = new ProjectSQLiteOpenHelper(That);
				db = phelper.getReadableDatabase();
				phelper.deleteAllProjects(db);
				for (int i = 0; i < arrProjects.length(); i++)
				{
					JSONObject object = (JSONObject) arrProjects.get(i);

					editor.putString( object.getString("projectNAME"), object.getString("projectTIMEUP"));

					Project p = new Project(object.getInt("projectID"),
							object.getString("projectNAME"),
							object.getString("projectEMAIL"),
							object.getString("projectPHONE"),
							object.getString("projectFILES"),
							object.getString("projectTIMEUP"));

					phelper.addProject(db, p);
				}
				editor.commit();
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
			if (db != null ) db.close();
		}
		listener.dataUpToDate();
	}

	public void updateSQLiteQuestions(String response)
	{
		if (response != null)
		{
			try
			{
				JSONArray arr = new JSONArray(response);
				Log.d(TAG, "get " + arr.length() + " projects");
				phelper = new ProjectSQLiteOpenHelper(That);
				db = phelper.getReadableDatabase();
				phelper.deleteAllQuestions(db);
				for (int i = 0; i < arr.length(); i++)
				{
					JSONObject object = (JSONObject) arr.get(i);

					Question p = new Question(  object.getInt("questionID"),
							object.getString("questionQ"),
							object.getString("questionT"),
							object.getInt("questionP"),
							object.getInt("questionORDER"));

					phelper.addQustion(db, p);
				}
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
			if (db != null ) db.close();
		}
	}

	public void updateSQLiteInQuestions(String response)
	{
		Utilities.print(TAG, " Respons for In Questions: " + response);
		if (response != null)
		{
			try
			{
				JSONArray arr = new JSONArray(response);
				Log.d(TAG, "get " + arr.length() + " projects");
				phelper = new ProjectSQLiteOpenHelper(That);
				db = phelper.getReadableDatabase();
				phelper.deleteAllInQuestions(db);
				for (int i = 0; i < arr.length(); i++)
				{
					JSONObject object = (JSONObject) arr.get(i);
					Utilities.print(TAG, "In Question Id: " + object.get("questionID"));
					Utilities.print(TAG, "In Question : " + object.get("questionQ"));
					Utilities.print(TAG, "In Question type: " + object.get("questionT"));
					Utilities.print(TAG, "In Question project: " + object.get("questionP"));

					Question p = new Question(  object.getInt("questionID"),
												object.getString("questionQ"),
												object.getString("questionT"),
												object.getInt("questionP"),
												object.getInt("questionORDER"));

					phelper.addInQustion(db, p);
				}
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
			if (db != null ) db.close();
		}
	}
}
