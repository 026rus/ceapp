package com.example.borodin.cecheckinout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by borodin on 6/10/2016.
 */
public class ProjectSQLiteOpenHelper extends SQLiteOpenHelper
{

	public ProjectSQLiteOpenHelper(Context context)
	{
		super(context, ProjectDatabaseContract.DATABASE_NAME, null, ProjectDatabaseContract.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(ProjectDatabaseContract.ProjectColumns.CREATE_TABLE);
		db.execSQL(ProjectDatabaseContract.QuestionsColumns.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL(ProjectDatabaseContract.ProjectColumns.DELETE_TABLE);
		db.execSQL(ProjectDatabaseContract.ProjectColumns.CREATE_TABLE);
		db.execSQL(ProjectDatabaseContract.QuestionsColumns.DELETE_TABLE);
		db.execSQL(ProjectDatabaseContract.QuestionsColumns.CREATE_TABLE);
	}
	// Manage data in the database lite
	// Projects starts here
	public void deleteAllProjects(SQLiteDatabase db)
	{
		db.execSQL(ProjectDatabaseContract.ProjectColumns.DELETE_TABLE);
		db.execSQL(ProjectDatabaseContract.ProjectColumns.CREATE_TABLE);
	}

	public void addProject(SQLiteDatabase db, Project pr)
	{
		db.execSQL(ProjectDatabaseContract.ProjectColumns.CREATE_TABLE);
		ContentValues values = new ContentValues();
		values.put(ProjectDatabaseContract.ProjectColumns._ID, pr.getId());
		values.put(ProjectDatabaseContract.ProjectColumns.COLUMN_NAME, pr.getName());
		values.put(ProjectDatabaseContract.ProjectColumns.COLUMN_EMAIL, pr.getEmail());
		values.put(ProjectDatabaseContract.ProjectColumns.COLUMN_PHONE, pr.getPhone());
		values.put(ProjectDatabaseContract.ProjectColumns.COLUMN_FILES, pr.getFiles());
		db.insert(ProjectDatabaseContract.ProjectColumns.TABLE_NAME, null, values);
	}
	public ArrayList<Project> readAllProgects (SQLiteDatabase db)
	{
		ArrayList<Project> projects = new ArrayList<Project>();
		Cursor c = db.rawQuery("Select * from " + ProjectDatabaseContract.ProjectColumns.TABLE_NAME, null);
		c.moveToFirst();
		while ( c.isAfterLast() == false )
		{
			int id       = c.getInt(c.getColumnIndex(ProjectDatabaseContract.ProjectColumns._ID));
			String name  = c.getString(c.getColumnIndex(ProjectDatabaseContract.ProjectColumns.COLUMN_NAME));
			String email = c.getString(c.getColumnIndex(ProjectDatabaseContract.ProjectColumns.COLUMN_EMAIL));
			String phone = c.getString(c.getColumnIndex(ProjectDatabaseContract.ProjectColumns.COLUMN_PHONE));
			String files = c.getString(c.getColumnIndex(ProjectDatabaseContract.ProjectColumns.COLUMN_FILES));
			String time_up = c.getString(c.getColumnIndex(ProjectDatabaseContract.ProjectColumns.COLUMN_UPDTIME));

			Project p = new Project(id, name, email, phone, files, time_up);
			projects.add(p);
			c.moveToNext();
		}
		return projects;
	}

	// Qestions starts here
	public void deleteAllQuestions(SQLiteDatabase db)
	{
		db.execSQL(ProjectDatabaseContract.QuestionsColumns.DELETE_TABLE);
		db.execSQL(ProjectDatabaseContract.QuestionsColumns.CREATE_TABLE);
	}

	public ArrayList<Question> readAllQuestions (SQLiteDatabase db)
	{
		ArrayList<Question> questions = new ArrayList<Question>();

		Cursor c = db.rawQuery("Select * from " + ProjectDatabaseContract.QuestionsColumns.TABLE_NAME, null);
		c.moveToFirst();
		while ( c.isAfterLast() == false )
		{
			int id          = c.getInt(c.getColumnIndex(ProjectDatabaseContract.QuestionsColumns._ID));
			int project     = c.getInt(c.getColumnIndex(ProjectDatabaseContract.QuestionsColumns.COLUMN_PROJECT));
			String question = c.getString(c.getColumnIndex(ProjectDatabaseContract.QuestionsColumns.COLUMN_QUESTION));
			String questionType = c.getString(c.getColumnIndex(ProjectDatabaseContract.QuestionsColumns.COLUMN_TYPE));
			int order 		= c.getInt(c.getColumnIndex(ProjectDatabaseContract.QuestionsColumns.COLUMN_ORDER));

			Question q = new Question(id, question, questionType, project, order);
			questions.add(q);
			c.moveToNext();
		}
		return questions;
	}

	public ArrayList<Question> readQuestionsByProjectId (SQLiteDatabase db, int p)
	{
		ArrayList<Question> questions = new ArrayList<Question>();

		Cursor c = db.rawQuery("Select * from " + ProjectDatabaseContract.QuestionsColumns.TABLE_NAME + " WHERE "+ ProjectDatabaseContract.QuestionsColumns.COLUMN_PROJECT + "=" + p +
				" OR " + ProjectDatabaseContract.QuestionsColumns.COLUMN_PROJECT + "=0", null);
		c.moveToFirst();
		while ( c.isAfterLast() == false )
		{
			int id          = c.getInt(c.getColumnIndex(ProjectDatabaseContract.QuestionsColumns._ID));
			int project     = c.getInt(c.getColumnIndex(ProjectDatabaseContract.QuestionsColumns.COLUMN_PROJECT));
			String question = c.getString(c.getColumnIndex(ProjectDatabaseContract.QuestionsColumns.COLUMN_QUESTION));
			String questionType = c.getString(c.getColumnIndex(ProjectDatabaseContract.QuestionsColumns.COLUMN_TYPE));
			int order 		= c.getInt(c.getColumnIndex(ProjectDatabaseContract.QuestionsColumns.COLUMN_ORDER));

			Question q = new Question(id, question, questionType, project, order);
			questions.add(q);
			c.moveToNext();
		}
		return questions;
	}

	public void addQustion( SQLiteDatabase db, Question q)
	{
		db.execSQL(ProjectDatabaseContract.QuestionsColumns.CREATE_TABLE);
		ContentValues values = new ContentValues();
		values.put(ProjectDatabaseContract.QuestionsColumns.COLUMN_QUESTION, q.getQestion());
		values.put(ProjectDatabaseContract.QuestionsColumns.COLUMN_PROJECT, q.getProject());
		values.put(ProjectDatabaseContract.QuestionsColumns.COLUMN_TYPE, q.getQuestionType());

		db.insert(ProjectDatabaseContract.QuestionsColumns.TABLE_NAME, null, values);
	}
}
