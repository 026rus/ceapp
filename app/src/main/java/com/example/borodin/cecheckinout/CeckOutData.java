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
		// TODO: 2/12/2017 read all saved data

		context = that;
	}

	public String getJeson()
	{
		String string;
		String  timein = "NULL",
				timeout = "NULL";
		if (tin != null ) timein = "" + tin.getTime();
		if (tout != null ) timeout = "" + tout.getTime();
		string = "[{\"cename\":\"" + cename + "\",\"site\":\"" + site + "\",\"tin\":" + timein + ", \"tout\":"+ timeout + ",\"pid\":"+pid + "} ]";

		return string;
	}

	public boolean storeCheckOutData()
	{
		try
		{
			Long 	timein = null,
					timeout = null;
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
			return false;
		}
		return true;
	}

	public void readCheckOutData()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.cename = preferences.getString("cename", null);
		this.site 	= preferences.getString("site", null);
		Long timein	= preferences.getLong("tin", 0);
		Long timeout= preferences.getLong("tout", 0);
		if (timein  > 0 ) this.tin	= new Timestamp(timein);
		else this.tin = null;
		if (timeout > 0 ) this.tout	= new Timestamp(timeout);
		else this.tout = null;
		this.pid 	= preferences.getInt("pid", 0);
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
