package com.example.borodin.cecheckinout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by borodin on 6/24/2016.
 */
public class MessegeInOut implements Parcelable
{
	public static final Integer ISCHEKET = 1;
	public static final Integer ISNOTCHEKET = 0;
	public static final Integer ISTEXT = 2;
	public static final Integer ISTEXTFILD = 3;

	private String ProjesctName;
	private String SiteStoreNumber;
	private String StoreName;
	private String TechName;
	private String TechPhoneNumber;
	private String TechLocation;
	private String OutMasseg;
	private ArrayList<String> checklist;
	private ArrayList<Integer> ischecked;
	private ArrayList<String> filelist;
	private String signatur;

	public MessegeInOut()
	{
		ProjesctName = null;
		SiteStoreNumber = null;
		StoreName = null;
		TechName = null;
		TechPhoneNumber = null;
		TechLocation = null;
		checklist = null;
		ischecked = null;
		filelist = null;
		signatur = null;
	}

	protected MessegeInOut(Parcel in)
	{
		ProjesctName = in.readString();
		SiteStoreNumber = in.readString();
		StoreName = in.readString();
		TechName = in.readString();
		TechPhoneNumber = in.readString();
		TechLocation = in.readString();
		OutMasseg = in.readString();
		checklist = in.readArrayList(String.class.getClassLoader());
		ischecked = in.readArrayList(Boolean.class.getClassLoader());
		filelist = in.readArrayList(String.class.getClassLoader());
		signatur = in.readString();
	}

	public static final Creator<MessegeInOut> CREATOR = new Creator<MessegeInOut>()
	{
		@Override
		public MessegeInOut createFromParcel(Parcel in)
		{
			return new MessegeInOut(in);
		}

		@Override
		public MessegeInOut[] newArray(int size)
		{
			return new MessegeInOut[size];
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(ProjesctName);
		dest.writeString(SiteStoreNumber);
		dest.writeString(StoreName);
		dest.writeString(TechName);
		dest.writeString(TechPhoneNumber);
		dest.writeString(TechLocation);
		dest.writeString(OutMasseg);
		dest.writeList(checklist);
		dest.writeList(ischecked);
		dest.writeList(filelist);
		dest.writeString(signatur);
	}
	public void addFile(String str)
	{
		if (filelist != null)
		{
			filelist.add(str);
		}
		else
		{
			filelist = new ArrayList<>();
			filelist.add(str);
		}
	}

	public String getProjesctName()
	{
		return ProjesctName;
	}

	public void setProjesctName(String projesctName)
	{
		ProjesctName = projesctName;
	}

	public String getSiteStoreNumber()
	{
		return SiteStoreNumber;
	}

	public void setSiteStoreNumber(String siteStoreNumber)
	{
		SiteStoreNumber = siteStoreNumber;
	}

	public String getStoreName()
	{
		return StoreName;
	}

	public void setStoreName(String storeName)
	{
		StoreName = storeName;
	}

	public String getTechName()
	{
		return TechName;
	}

	public void setTechName(String techName)
	{
		TechName = techName;
	}

	public String getTechPhoneNumber()
	{
		return TechPhoneNumber;
	}

	public void setTechPhoneNumber(String techPhoneNumber)
	{
		TechPhoneNumber = techPhoneNumber;
	}

	public String getTechLocation()
	{
		return TechLocation;
	}

	public void setTechLocation(String techLocation)
	{
		TechLocation = techLocation;
	}

	public ArrayList<String> getChecklist()
	{
		return checklist;
	}

	public void addChecklist(String checklist, Integer ischecked)
	{
		if (this.checklist == null || this.ischecked == null || this.checklist.size() != this.ischecked.size())
		{
			this.checklist = new ArrayList<>();
			this.ischecked = new ArrayList<>();
		}
		this.checklist.add(checklist);
		this.ischecked.add(ischecked);
	}
	public void clearCheckList()
	{
		checklist = null;
		ischecked = null;
	}

	public ArrayList<Integer> getIschecked()
	{
		return ischecked;
	}

	public ArrayList<String> getFilelist()
	{
		return filelist;
	}

	public void setFilelist(ArrayList<String> filelist)
	{
		this.filelist = filelist;
	}

	public String getSignatur()
	{
		return signatur;
	}

	public void setSignatur(String signatur)
	{
		this.signatur = signatur;
	}

	public String getOutMasseg()
	{
		String  ssn = (SiteStoreNumber == null) ? "" : SiteStoreNumber;
		String 	pn 	= (ProjesctName == null) ? "" : ProjesctName;
		String 	tn 	= (TechName == null) ? "" : TechName;
		String 	tpn = (TechPhoneNumber == null) ? "" : TechPhoneNumber;

		String retval = "Site/Store Number: " + ssn + "\n" +
				"Project Name: "  + pn + "\n" +
				"Tech Name: " + tn + "\n" +
				"Tech Phone Number: " + tpn + "\n";
		return retval + OutMasseg;
	}

	public void setOutMasseg(String outMasseg)
	{
		OutMasseg = outMasseg;
	}

	public String getCheckInMessage()
	{
		String  ssn = (SiteStoreNumber == null) ? "" : SiteStoreNumber;
		String 	sn	= (StoreName == null) ? "" : StoreName;
		String 	pn 	= (ProjesctName == null) ? "" : ProjesctName;
		String 	tn 	= (TechName == null) ? "" : TechName;
		String 	tpn = (TechPhoneNumber == null) ? "" : TechPhoneNumber;

		// TODO: 8/29/2016  find and delete/populete store name   
		String retval = // not sure what is the for : - "Store Name:  " + sn + "\n" +
				"Site/Store Number: " + ssn + "\n" +
				"Project Name: "  + pn + "\n" +
				"Tech Name: " + tn + "\n" +
				"Tech Phone Number: " + tpn + "\n";

		retval += Utilities.getTimeZon() + "\n";
		retval += Utilities.getTime();
		Utilities.print("TEST", retval);
		return retval;
	}
}
