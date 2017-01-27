package com.example.borodin.cecheckinout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

// TODO: 1/15/2017 check for internet avalebility  
public class MainActivity extends AppCompatActivity
{
	private final static String TAG = "MainActivity_TEST";

	private EditText projectName;
	private ArrayList<Project> projects = null;
	private Project p = null;
	private ProgressBar progressBar;
	private DataCopliteList listener = new DataCopliteList();
	private Menu main_menu;

	private boolean isUptodate;
	private boolean isOnline;
	private checkDBForUpdates chekForUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		isUptodate = false;
		isOnline = isOnlinecheck();
		if (!isOnline)
		{
			Utilities.print(TAG, "There is no Internet !!!");
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
			dlgAlert.setMessage("This is an alert with no consequence");
			dlgAlert.setTitle("App Title");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//dismiss the dialog
						}
					});
			dlgAlert.create().show();
		}
		else
			Utilities.print(TAG, "Internet connection detected !");

		setVew();
		setlisteners();

		if (isFirstTime()) firstTimeInit();
    }

	private boolean isOnlinecheck()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
				connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
		{
			return true;
		}
		else
			return false;
	}

	@Override
	protected void onResume()
	{
		// propose update if the local data base is older then one on the server
		isDBcorent();
		super.onResume();
	}

	private void isDBcorent()
	{
		// Cheking for all tables in DB if the one on the phone is the same time that on ont the server
		Utilities.print(TAG, "Starting isDBcorent ");

		if(Utilities.isNetworkAvailable(this))
		{
			chekForUp = new checkDBForUpdates(this, progressBar, listener);
			chekForUp.execute(getString(R.string.api_update) );
		}
	}

	private boolean isFirstTime()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		return preferences.getBoolean("FirstTime", true);
	}
	private void firstTimeInit()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("FirstTime", false);
		Utilities.print(TAG, "First Time Run");
		editor.apply();
		syncSQLiteMySQLDB();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		main_menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		switch (id)
		{
			case R.id.refresh:
				// update db from server
				Log.d(TAG, "Syncing data manually !");
				syncSQLiteMySQLDB();
				item.setIcon(getResources().getDrawable(R.drawable.ic_action_refresh));
				return true;
			case R.id.download_man:
				// GpsLocator locator = new GpsLocator();
				// locator.turnGPSOn();
				return true;
			case R.id.setings:
				// test();
				startSetings();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startSetings()
	{
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	private void setVew()
	{
		projectName = (EditText) findViewById(R.id.projectNames);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		updateProgects();
	}

	private void setlisteners()
	{
		projectName.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showProjectChooser();
			}
		});
	}

	private void updateProgects()
	{
		ProjectSQLiteOpenHelper phelper;
		SQLiteDatabase db;
		phelper = new ProjectSQLiteOpenHelper(this);
		db = phelper.getReadableDatabase();
		projects = phelper.readAllProgects(db);
		db.close();
	}

	public void syncSQLiteMySQLDB()
	{
		if (Utilities.isNetworkAvailable(this))
		{
			new SyncSQLDB(this, progressBar, listener).execute(getString(R.string.api_getprojects),
					getString(R.string.api_getallquestions),
					getString(R.string.api_getallinquestions));
		}
	}

	public void showProjectChooser()
	{
		if (projects.size() == 0)
		{
			Toast.makeText(this.getApplicationContext(), "Projects list not ready!", Toast.LENGTH_LONG).show();
			return;
		}
		Collections.sort(projects, new ProjectComparator());
		String[] Arr = new String[projects.size()];
		for (int i=0; i < Arr.length; i++)
			Arr[i] = projects.get(i).getName();
		int sizeOfprojects = 0;
		if (projects.size() > 0)
			sizeOfprojects = projects.size()-1;

		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setTitle("Choose a Project");
		dialog.setContentView(R.layout.project_chooser_dialog);
		final NumberPicker projectchooser = (NumberPicker)dialog.findViewById(R.id.projectPicker);
		projectchooser.setMinValue(0);
		projectchooser.setMaxValue(sizeOfprojects);
		projectchooser.setDisplayedValues(Arr);

		Button ok = (Button)dialog.findViewById(R.id.button_set);
		Button cancel = (Button)dialog.findViewById(R.id.button_cancel);

		ok.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int x = projectchooser.getValue();
				p = projects.get(x);
				projectName.setText(p.getName());
				dialog.dismiss();
				showSiteChooser();
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
	public void showSiteChooser()
	{
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setTitle(R.string.siteChooserTitlel);
		dialog.setContentView(R.layout.site_chooser_dialog);
		final EditText sitechooser = (EditText)dialog.findViewById(R.id.site_choser);

		Button ok = (Button)dialog.findViewById(R.id.site_choser_button_set);
		Button cancel = (Button)dialog.findViewById(R.id.site_choser_button_cancel);

		ok.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String SITENAME = sitechooser.getText().toString();
				dialog.dismiss();
				Intent intent = new Intent(MainActivity.this, CheckInOutActivity.class);
				intent.putExtra("project", p);
				intent.putExtra("SITENAME", SITENAME);
				startActivity(intent);
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
	private class DataCopliteList implements OnDataUptaded
	{
		@Override
		public void dataUpToDate()
		{
			updateProgects();
			isDBcorent();
		}

		@Override
		public void checkFinished(boolean isUp)
		{
			if (!isUp) main_menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_action_refresh_red));
			else main_menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_action_refresh));
			isUptodate  = chekForUp.isuptodate;
		}
	}
}
