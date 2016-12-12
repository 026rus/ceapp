package com.example.borodin.cecheckinout;

import android.Manifest;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InActivity extends AppCompatActivity
{
	private static final String TAG = "InActivity_TEST";
	private LinearLayout inLayout;
	private Project correntProject;
	private MessegeInOut correntMessege;
	private ArrayList<Question> correntQuestions;
	private SQLiteDatabase db;
	private ProjectSQLiteOpenHelper dbhelper;

	// Storage Permissions
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in);
		initialase();
	}

	private void initialase()
	{
		// hide keyboard for the screen on start up.
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		// get all the data for the project acceded in to.
		correntProject = (Project) getIntent().getParcelableExtra(getString(R.string.IntentTagProject));
		correntMessege = (MessegeInOut) getIntent().getParcelableExtra(getString(R.string.IntentTagMessage));

		dbhelper = new ProjectSQLiteOpenHelper(this);
		db = dbhelper.getReadableDatabase();
		if (correntProject != null)
			correntQuestions = dbhelper.readInQuestionsByProjectId(db, correntProject.getId());
		else
			Toast.makeText(this, "Problem with project Sorry ", Toast.LENGTH_SHORT).show();

		TextView tv = (TextView) findViewById(R.id.in_title);
		tv.setText(correntMessege.getSiteStoreNumber());
		setTitle(correntProject.getName());

	}
}
