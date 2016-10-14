package com.example.borodin.cecheckinout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.jar.Manifest;

/**
 * Created by borodin on 6/10/2016.
 */
public final class Utilities
{
	public static final String newline = "\n";

	public static void print(String tag, String str)
	{
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

	public static void sendEmail(Context context, MessegeInOut mesg, String[] sto, Uri signature)
	{

		Intent intentout = new Intent(Intent.ACTION_SEND);
		intentout.setType("image/png");
		intentout.putExtra(Intent.EXTRA_EMAIL, sto);
		intentout.putExtra(Intent.EXTRA_SUBJECT, "Checking out Store " + mesg.getSiteStoreNumber());
		intentout.putExtra(Intent.EXTRA_TEXT, mesg.getOutMasseg());
		intentout.putExtra(Intent.EXTRA_STREAM, signature);
		try
		{
			context.startActivity(intentout);
		} catch (ActivityNotFoundException e)
		{
			Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}

	public static void sendEmail(Context context, int mod, MessegeInOut mesg, String[] sto)
	{
		switch (mod)
		{
			case Constants.CHECKIN:
				String sedMessage = mesg.getCheckInMessage();
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				intent.putExtra(Intent.EXTRA_EMAIL, sto);
				intent.putExtra(Intent.EXTRA_SUBJECT, "Checking in Store " + mesg.getSiteStoreNumber());
				intent.putExtra(Intent.EXTRA_TEXT, sedMessage);
				try
				{
					context.startActivity(intent);
				} catch (ActivityNotFoundException e)
				{
					Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.CHECKOUT:
				Intent intentout = new Intent(Intent.ACTION_SEND);
				intentout.setType("message/rfc822");
				intentout.putExtra(Intent.EXTRA_EMAIL, sto);
				intentout.putExtra(Intent.EXTRA_SUBJECT, "Checking out Store " + mesg.getSiteStoreNumber());
				intentout.putExtra(Intent.EXTRA_TEXT, mesg.getOutMasseg());
				try
				{
					context.startActivity(intentout);
				} catch (ActivityNotFoundException e)
				{
					Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	public static String getTimeZon()
	{
		TimeZone t = TimeZone.getDefault();
		return "Phone Time stamp " + t.getDisplayName(false, TimeZone.SHORT) + " :";
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

		return strweek + " " + strMonth + " " + day + " " + year + "   " + hour + ":" + strmin + ":" + strsec;
	}

	public static String getRealPathFromURI(Context context, Uri uri)
	{
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}
}
