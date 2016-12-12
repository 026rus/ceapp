package com.example.borodin.cecheckinout;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

public class CheckInOutActivity extends AppCompatActivity implements OnFileDownloaded
{
	private static final String TAG = "CheckInOutActivity_TEST";
	private Project correntProject;
	private MessegeInOut messege;
	private SQLiteDatabase db;
	private ProjectSQLiteOpenHelper dbhelper;
	private ProgressBar progressBar;
	private ArrayList<String> allfiles = null;
	private SharedPreferences preferences;

	// Storage Permissions
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};

	private ArrayList<Question> questions = new ArrayList<Question>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_in_out);
		Utilities.print(TAG, "On Create called !!!");
		initelaze();
		getherInfo();
	}

	private void initelaze()
	{
		Utilities.print(TAG, "Init all UI ");
		preferences = getSharedPreferences(getString(R.string.setings_for_update), Context.MODE_PRIVATE);
		progressBar = (ProgressBar) findViewById(R.id.checkInOutprogressBar);

		String site = getIntent().getExtras().getString("SITENAME");
		correntProject = (Project) getIntent().getParcelableExtra("project");

		Utilities.print(TAG, "THE ID: " + correntProject.getId());
		Utilities.print(TAG, "THE ID: " + correntProject.getName());
		Utilities.print(TAG, "\t\t\t Site: \t\t" + site);
		messege = new MessegeInOut();
		messege.setSiteStoreNumber(site);
		messege.setProjesctName(correntProject.getName());
		dbhelper = new ProjectSQLiteOpenHelper(this);
		db = dbhelper.getReadableDatabase();
		ArrayList<Question> allq = dbhelper.readAllQuestions(db);
		for (int i = 0; i < allq.size(); i++)
		{
			if ((allq.get(i).getProject() == correntProject.getId()) || (allq.get(i).getProject() == 0))
			{
				questions.add(allq.get(i));
			}
		}
		TextView out_p = (TextView) findViewById(R.id.output_project);
		if (out_p != null)
			out_p.setText(correntProject.getName());

		TextView out_s = (TextView) findViewById(R.id.output_site);
		if (out_s != null)
			out_s.setText("Site Number: " + site);

		TextView out_phon = (TextView) findViewById(R.id.output_phone);
		if (out_phon != null)
			out_phon.setText(correntProject.getPhone());

	}

	private void getherInfo()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		messege.setTechName(preferences.getString("thech_name", ""));
		messege.setTechPhoneNumber(preferences.getString("thech_phone", ""));
	}


	/* TODO: 12/12/2016 Add check for questiosn and chtck list on "Check IN" and if there is ont
		then open new Activity to conform all the check boxs and then send the email. If not
		then sen email with just basic "check in" information.
	 */

	public void onClickCheckIn (View v)
	{
		Utilities.print(TAG, "Check In button pressed!");
		// comment for testing check in check list.
		// Utilities.sendEmail(this, Constants.CHECKIN , messege, new String[]{correntProject.getEmail()});

		SQLiteDatabase db;
		ProjectSQLiteOpenHelper dbhelper;
		ArrayList<Question> correntQuestions = null;

		dbhelper = new ProjectSQLiteOpenHelper(this);
		db = dbhelper.getReadableDatabase();

		if (correntProject != null)
			correntQuestions = dbhelper.readInQuestionsByProjectId(db, correntProject.getId());
		else
			Toast.makeText(this, "Problem with project Sorry ", Toast.LENGTH_SHORT).show();

		if (correntQuestions == null || correntQuestions.isEmpty())
			Utilities.print(TAG, "There is no CHeck IN questions for this project.");

		else
		{
			// That is just for testing
			Utilities.print(TAG, "there is " + correntQuestions.size() + " check in questions in this project");
			for(int i=0; i < correntQuestions.size(); i++)
				Utilities.print(TAG, "\t" + i + ") " + correntQuestions.get(i).getQuestionType() + " - " + correntQuestions.get(i).getQestion());
			////////////////////////////////////////////////////////////////////////////////////////

			Intent intent = new Intent(this, InActivity.class);
			intent.putExtra(getString(R.string.IntentTagProject), correntProject);
			intent.putExtra(getString(R.string.IntentTagMessage), messege);
			startActivity(intent);
		}

	}

	public void onClickGoBack(View v)
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	public void onClickCheckOut (View v)
	{
		Intent intent = new Intent(this, OutActivity.class);
		intent.putExtra(getString(R.string.IntentTagProject), correntProject);
		intent.putExtra(getString(R.string.IntentTagMessage), messege);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_project, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		switch (id)
		{
			case R.id.download_man:
				GpsLocator locator = new GpsLocator();
				locator.turnGPSOn();
				Log.d(TAG, "Downloading the manual !");
				checkforManual();
				item.setIcon(getResources().getDrawable(R.drawable.ic_action_download_man));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void checkforManual()
	{
		new AsyncTaskGetFiles().execute(getString(R.string.api_getfiles)+ "?id=" + correntProject.getId());
	}
	private void downloadTheManual(String thefile)
	{
		int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permission != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
		}
		new DownloadFile(progressBar, this).execute(getString(R.string.man_server_loc)+ correntProject.getId()+ "/" + thefile);
	}

	private void openmanfile(String filepath)
	{
		File file = new File(filepath);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	@Override
	public void fileDownloadCompleted(String filename)
	{
		openmanfile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + filename);
	}

	// get files in the project
	private class AsyncTaskGetFiles extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params)
		{
			allfiles = new ArrayList<String>();
			String urlStr = params[0];
			Utilities.print(TAG, urlStr);
			HTTPDataHandler httpDataHandler = new HTTPDataHandler();
			String stream = httpDataHandler.GetHTTPData(urlStr);
			if(stream != null)
			{
				try
				{
					JSONArray arrayfiles = new JSONArray(stream);
					for(int i=0; i<arrayfiles.length(); i++)
					{
						allfiles.add(arrayfiles.get(i).toString());
					}

				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s)
		{
			super.onPostExecute(s);
			progressBar.setVisibility(View.INVISIBLE);

			Utilities.print(TAG, "Find " + allfiles.size() + " files.");

			for (int i=0; i<allfiles.size(); i++)
			{
				Utilities.print(TAG, "########## The line " + i + " ############");
				Utilities.print(TAG, allfiles.get(i));
			}

			if (allfiles.size()>1)
			{
				showFileChooser(allfiles);
			}
			else if (allfiles.size() == 1)
			{
				downloadTheManual(allfiles.get(0));
			}
		}
	}

	private void showFileChooser(final ArrayList<String> files)
	{
		if (files.size() == 0)
		{
			Toast.makeText(this.getApplicationContext(), "There is no files available for download!", Toast.LENGTH_LONG).show();
			return;
		}
		String[] Arr = new String[files.size()];
		for (int i=0; i < Arr.length; i++)
			Arr[i] = files.get(i);
		int size = 0;
		if (files.size() > 0)
			size = files.size()-1;

		final Dialog dialog = new Dialog(this);
		dialog.setTitle("Choose a File");
		dialog.setContentView(R.layout.file_chooser_dialog);
		final NumberPicker filechooser = (NumberPicker)dialog.findViewById(R.id.filePicker);
		filechooser.setMinValue(0);
		filechooser.setMaxValue(size);
		filechooser.setDisplayedValues(Arr);

		Button ok = (Button)dialog.findViewById(R.id.file_choser_button_set);
		Button cancel = (Button)dialog.findViewById(R.id.file_choser_button_cancel);

		ok.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int x = filechooser.getValue();
				downloadTheManual(files.get(x));
				dialog.dismiss();
			}
		});

		cancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
		dialog.show();
	}

}
