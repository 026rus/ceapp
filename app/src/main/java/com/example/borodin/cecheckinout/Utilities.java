package com.example.borodin.cecheckinout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by borodin on 6/10/2016.
 */
public final class Utilities
{
	private static final String TAG = "Utilities_TEST";
	public static final String newline = "\n";

	public static void print(String tag, String str)
	{
		// uncoment befor testing app
		Log.d(tag, str);
	}

	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static boolean isStoregeAvailable(Context context)
	{
		return true;
	}


	public static String getTimeZon()
	{
		TimeZone t = TimeZone.getDefault();
		return t.getDisplayName(false, TimeZone.SHORT);
	}

	public static String getTime()
	{
		Calendar c = Calendar.getInstance();
		int seconds = c.get(Calendar.SECOND);
		int mint = c.get(Calendar.MINUTE);
		int hour = c.get(Calendar.HOUR);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);

		int week = c.get(Calendar.DAY_OF_WEEK);

		String strweek = "";
		switch (week)
		{
			case Calendar.MONDAY:
				strweek = "Monday";
				break;
			case Calendar.TUESDAY:
				strweek = "Tuesday";
				break;
			case Calendar.WEDNESDAY:
				strweek = "Wednesday";
				break;
			case Calendar.THURSDAY:
				strweek = "Thursday";
				break;
			case Calendar.FRIDAY:
				strweek = "Friday";
				break;
			case Calendar.SATURDAY:
				strweek = "Saturday";
				break;
			case Calendar.SUNDAY:
				strweek = "Sunday";
				break;
		}

		String strMonth = "";
		switch (month)
		{
			case Calendar.JANUARY:
				strMonth = "January";
				break;
			case Calendar.FEBRUARY:
				strMonth = "February";
				break;
			case Calendar.MARCH:
				strMonth = "March";
				break;
			case Calendar.APRIL:
				strMonth = "April";
				break;
			case Calendar.MAY:
				strMonth = "May";
				break;
			case Calendar.JUNE:
				strMonth = "June";
				break;
			case Calendar.JULY:
				strMonth = "July";
				break;
			case Calendar.AUGUST:
				strMonth = "August";
				break;
			case Calendar.SEPTEMBER:
				strMonth = "September";
				break;
			case Calendar.OCTOBER:
				strMonth = "October";
				break;
			case Calendar.NOVEMBER:
				strMonth = "November";
				break;
			case Calendar.DECEMBER:
				strMonth = "December";
				break;
		}

		String strmin = "";
		if (mint < 10) strmin = "0" + mint;
		else strmin = "" + mint;

		String strsec = "";
		if (seconds < 10) strsec = "0" + seconds;
		else strsec = "" + seconds;

		// TODO: 1/9/2017 make sure the time in 24 format  
		return strweek + " " + strMonth + " " + day + " " + year + "   " + hour + ":" + strmin;
	}

	public static String getRealPathFromURI(Context context, Uri uri)
	{
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

	public static String makeFileNmae(String str)
	{
		String result = str.replaceAll("[|?*<\":>+\\[\\]/']", "_");
		return result;
	}
}
