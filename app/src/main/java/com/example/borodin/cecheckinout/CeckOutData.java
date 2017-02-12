package com.example.borodin.cecheckinout;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.sql.Timestamp;

/**
 * Created by borodin on 2/10/2017.
 */

public class CeckOutData
{
	private static final String TAG = "CheckOutData_TEST";
	private String cename;
	private String site;
	private Timestamp tin;
	private Timestamp tout;
	private int pid;
	private Context context;

	public CeckOutData(Context that, String cename, String site, Timestamp tin, Timestamp tout, int pid)
	{
		this.cename = cename;
		this.site = site;
		this.tin = tin;
		this.tout = tout;
		this.pid = pid;
		context = that;

	}

	public CeckOutData(Context that)
	{
		context = that;
	}

	public CeckOutData(Context that, MessegeInOut messege, Project p)
	{
		Utilities.print(TAG, "CeckOutData Contractor from massage");
		long time = System.currentTimeMillis();
		Utilities.print(TAG, "Millis: " + time);
		cename 	= messege.getTechName();
		site 	= messege.getSiteStoreNumber();
		tin		= new Timestamp(time);
		pid 	= p.getId();
		context = that;
		printCeckOutData(TAG);
	}

	public String getJeson()
	{
		String string;
		String  timein = "NULL",
				timeout = "NULL";
		if (tin != null ) timein = "" +   (tin.getTime() /1000);
		if (tout != null ) timeout = "" + (tout.getTime()/1000);
		string = "[{\"cename\":\"" + cename + "\",\"site\":\"" + site + "\",\"tin\":" + timein + ", \"tout\":"+ timeout + ",\"pid\":"+pid + "} ]";

		return string;
	}

	public void storeCeckOutData()
	{
		Utilities.print(TAG, "Storing CeckOutData!");
		printCeckOutData(TAG);
		// TODO: 2/12/2017 make this seporet thread so it will run faster;
		try
		{
			long 	timein = 0,
					timeout = 0;
			if (tin != null ) timein = tin.getTime();
			if (tout != null ) timeout = tout.getTime();
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cename", cename);
			editor.putString("site", site);
			editor.putLong("tin", timein);
			editor.putLong("tout", timeout);
			editor.putInt("pid", pid);
			editor.apply();
		}
		catch (Exception e)
		{
			Utilities.print(TAG, "Fail to save data: " + e.getMessage());
		}
	}
	 public void printCeckOutData(String strTAG)
	 {
		 Utilities.print(strTAG, "--------------------------------------------------------------");
		 Utilities.print(strTAG, "cename = " + cename);
		 Utilities.print(strTAG, "site   = " + site);
		 Utilities.print(strTAG, "tin    = " + tin);
		 Utilities.print(strTAG, "tout   = " + tout);
		 Utilities.print(strTAG, "pid    = " + pid);
		 Utilities.print(strTAG, "--------------------------------------------------------------");
	 }

	public void readCeckOutData()
	{
		Utilities.print(TAG, "Start reading data of check in/out pacege.");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.cename = preferences.getString("cename", null);
		Utilities.print(TAG, "CE name: " + this.cename);
		this.site 	= preferences.getString("site", null);
		Long timein	= preferences.getLong("tin", 0);
		Utilities.print(TAG, "Time in: " + timein);
		Long timeout= preferences.getLong("tout", 0);
		if (timein  > 0 ) this.tin	= new Timestamp(timein);
		else this.tin = null;
		if (timeout > 0 ) this.tout	= new Timestamp(timeout);
		else this.tout = null;
		this.pid 	= preferences.getInt("pid", 0);
		printCeckOutData(TAG);
	}
	public boolean deleteCechOutData()
	{
		Utilities.print(TAG, "deleteCechOutData starting 119");
		try
		{
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = preferences.edit();
			editor.remove("cename");
			editor.remove("site");
			editor.remove("tin");
			editor.remove("tout");
			editor.remove("pid");
			editor.apply();
			cename 	= null;
			site	= null;
			tin		= null;
			tout	= null;
			pid		= 0;
		}
		catch (Exception e)
		{
			Utilities.print(TAG, "Fail to save data: " + e.getMessage());
			return false;
		}
		return true;

	}

	public int getPid()
	{
		return pid;
	}

	public void setPid(int pid)
	{
		this.pid = pid;
	}

	public String getCename()
	{
		return cename;
	}

	public void setCename(String cename)
	{
		this.cename = cename;
	}

	public String getSite()
	{
		return site;
	}

	public void setSite(String site)
	{
		this.site = site;
	}

	public Timestamp getTin()
	{
		return tin;
	}

	public void setTin(Timestamp tin)
	{
		this.tin = tin;
	}

	public Timestamp getTout()
	{
		return tout;
	}

	public void setTout(Timestamp tout)
	{
		this.tout = tout;
	}
}
