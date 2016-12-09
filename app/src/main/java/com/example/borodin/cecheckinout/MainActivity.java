package com.example.borodin.cecheckinout;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
	private checkDBForUpdates chekForUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		isUptodate = false;
		setVew();
		setlisteners();

		if (isFirstTime()) firstTimeInit();
    }

	@Override
	protected void onResume()
	{
		//todo  * check for updates !
		// propose update if the local data base is older then one on the server
		// SELECT table_name, auto_increment, update_time, check_time FROM information_schema.tables WHERE  table_schema = 'projects'
		isDBcorent();
		super.onResume();
	}

	private void isDBcorent()
	{
		// need to get table_names form server DB
		// then if ( table_names.size != oldSize ) return false;
		// then for each table in table_name
		// 			if table.time_update != table[i].oldTime return false;
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
