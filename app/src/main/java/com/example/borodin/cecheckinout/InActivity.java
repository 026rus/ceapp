package com.example.borodin.cecheckinout;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
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
		makeList();
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

	public void onClickInSend(View v)
	{
		// send email
		Utilities.print(TAG, "Making email for check in !");
	}


	// TODO: 12/16/2016 change to in It is for Out now
	private void sendCheckOut()
	{
		StringBuilder sendmassege = new StringBuilder();
		sendmassege.append("Progect : " + correntProject.getName() + Utilities.newline);
		sendmassege.append("Site: " + correntMessege.getSiteStoreNumber() + Utilities.newline);

		int childcount = inLayout.getChildCount();
		for (int i = 0; i < childcount - 1; i++)
		{
			View view = inLayout.getChildAt(i);
			if (view instanceof CheckBox)
			{
				if (((CheckBox) view).isChecked())
				{
					sendmassege.append(((CheckBox) view).getText().toString() + " is complete" + Utilities.newline);
					String str = ((CheckBox) view).getText().toString() + " is complete" + Utilities.newline;
					correntMessege.addChecklist(str, MessegeInOut.ISCHEKET);
				} else
				{
					sendmassege.append(((CheckBox) view).getText().toString() + " is not complete" + Utilities.newline);
					String str = ((CheckBox) view).getText().toString() + " is not complete" + Utilities.newline;
					correntMessege.addChecklist(str, MessegeInOut.ISNOTCHEKET);
				}
			} else if (view instanceof LinearLayout)
			{
				int inChild = ((LinearLayout) view).getChildCount();
				for (int c = 0; c < inChild; c++)
				{
					View inview = ((LinearLayout) view).getChildAt(c);
					if (inview instanceof EditText)
					{
						sendmassege.append(" : " + ((EditText) inview).getText().toString() + Utilities.newline);
						String str = (" : " + ((EditText) inview).getText().toString() + Utilities.newline);
						correntMessege.addChecklist(str, MessegeInOut.ISTEXT);
					} else if (inview instanceof TextView)
					{
						sendmassege.append(((TextView) inview).getText().toString());
						String str = ((TextView) inview).getText().toString();
						correntMessege.addChecklist(str, MessegeInOut.ISTEXTFILD);
					}
				}
			}
		}
	}

	private void makeList()
	{
		inLayout = (LinearLayout) findViewById(R.id.in_list);
		if (correntQuestions != null)
		{
			for (int i = 0; i < correntQuestions.size(); i++)
			{
				if (correntQuestions.get(i).getQuestionType().toLowerCase().trim().equals("q"))
				{
					CheckBox cb = new CheckBox(this);
					cb.setText(correntQuestions.get(i).getQestion());
					cb.setChecked(false);
					inLayout.addView(cb);
				} else if (correntQuestions.get(i).getQuestionType().toLowerCase().trim().equals("c"))
				{
					LinearLayout ll = new LinearLayout(this);
					ll.setOrientation(LinearLayout.HORIZONTAL);

					TextView tv = new TextView(this);
					tv.setText(correntQuestions.get(i).getQestion());

					EditText et = new EditText(this);
					et.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
					ll.addView(tv);
					ll.addView(et);
					inLayout.addView(ll);
				}
			}
		} else
			Toast.makeText(this, "Problem with Questions Sorry ", Toast.LENGTH_SHORT).show();
	}

	public void onClickInCancel(View v)
	{
		// sending back to maine. 
		// TODO: 12/16/2016 Find out may be need to send to privios Activity instde ? 
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
