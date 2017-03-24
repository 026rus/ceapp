package com.hpceapp.borodin.cecheckinout;
import android.provider.BaseColumns;

/**
 * Created by borodin on 6/10/2016.
 * To prevent accidentally inheritance of this class.
 */
public final class ProjectDatabaseContract
{
	public static final int    DATABASE_VERSION    = 2;
	public static final String DATABASE_NAME       = "projects.db";
	public static final String TEXT_TYPE           = " TEXT";
	public static final String INT_TYPE            = " integer";
	public static final String COMMA_SEP           = ", ";

	/* To prevent someone from accidentally instantiating the contract class,
	 * give it an empty private constructor. */
	private ProjectDatabaseContract() {}

	/* Inner class that defines the table contents
	 * Make it abstract to prevent accidentally instantiating this class. */

	public static abstract class ProjectColumns implements BaseColumns
	{

		public static final String TABLE_NAME       = "projectsTable";
		public static final String COLUMN_NAME      = "name";
		public static final String COLUMN_EMAIL     = "email";
		public static final String COLUMN_PHONE     = "phone";
		public static final String COLUMN_FILES     = "files";
		public static final String COLUMN_UPDTIME   = "time_updated";

		public static final String CREATE_TABLE =
				"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
						_ID + " INTEGER PRIMARY KEY, " +
						COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
						COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
						COLUMN_PHONE + TEXT_TYPE + COMMA_SEP +
						COLUMN_FILES + TEXT_TYPE + COMMA_SEP +
						COLUMN_UPDTIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")";
		public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

	public static abstract class QuestionsColumns implements BaseColumns
	{
		public static final String TABLE_NAME       = "questionsTable";
		public static final String COLUMN_QUESTION  = "question";
		public static final String COLUMN_PROJECT   = "project";
		public static final String COLUMN_TYPE  	= "type";
		public static final String COLUMN_ORDER  	= "orderq";

		public static final String CREATE_TABLE =
				"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
						_ID + " INTEGER PRIMARY KEY, " +
						COLUMN_QUESTION + TEXT_TYPE + COMMA_SEP +
						COLUMN_PROJECT + TEXT_TYPE + COMMA_SEP +
						COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
						COLUMN_ORDER + INT_TYPE + ")";
		public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

	public static abstract class QuestionsInColumns implements BaseColumns
	{
		public static final String TABLE_NAME       = "checkinquestionsTable";
		public static final String COLUMN_QUESTION  = "question";
		public static final String COLUMN_PROJECT   = "project";
		public static final String COLUMN_TYPE  	= "type";
		public static final String COLUMN_ORDER  	= "orderq";

		public static final String CREATE_TABLE =
				"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
						_ID + " INTEGER PRIMARY KEY, " +
						COLUMN_QUESTION + TEXT_TYPE + COMMA_SEP +
						COLUMN_PROJECT + TEXT_TYPE + COMMA_SEP +
						COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
						COLUMN_ORDER + INT_TYPE + ")";
		public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}
