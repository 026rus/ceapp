package com.example.borodin.cecheckinout;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in);
	}
}
