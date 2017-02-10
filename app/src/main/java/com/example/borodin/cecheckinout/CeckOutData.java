package com.example.borodin.cecheckinout;

import java.sql.Timestamp;

/**
 * Created by borodin on 2/10/2017.
 */

public class CeckOutData
{
	private String cename;
	private String site;
	private Timestamp tin;
	private Timestamp tout;

	public CeckOutData(String cename, String site, Timestamp tin, Timestamp tout)
	{
		this.cename = cename;
		this.site = site;
		this.tin = tin;
		this.tout = tout;
	}

	public String getJeson()
	{
		String string;
		String  timein = "NULL",
				timeout = "NULL";
		if (tin != null ) timein = "" + tin.getTime();
		if (tout != null ) timeout = "" + tout.getTime();
		string = "[{\"cename\":\"" + cename + "\",\"site\":\"" + site + "\",\"tin\":" + timein + ",\"pid\":15} ]";

		return string;
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
