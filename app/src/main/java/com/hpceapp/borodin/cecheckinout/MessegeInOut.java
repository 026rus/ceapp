package com.hpceapp.borodin.cecheckinout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by borodin on 6/24/2016.
 */
public class MessegeInOut implements Parcelable
{
	private static final String TAG = "MessageInOut_TEST";
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
	private String InMasseg;
	private ArrayList<String> checklist;
	private ArrayList<Integer> ischecked;
	private ArrayList<String> filelist;
	private ArrayList<String> filelistnames;
	private String signatur;
	private String TimeOut;

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
		filelistnames = null;
		signatur = null;
		TimeOut = null;
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
		InMasseg  = in.readString();
		checklist = in.readArrayList(String.class.getClassLoader());
		ischecked = in.readArrayList(Boolean.class.getClassLoader());
		filelist = in.readArrayList(String.class.getClassLoader());
		filelistnames = in.readArrayList(String.class.getClassLoader());
		signatur = in.readString();
		TimeOut = in.readString();
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
		dest.writeString(InMasseg);
		dest.writeList(checklist);
		dest.writeList(ischecked);
		dest.writeList(filelist);
		dest.writeList(filelistnames);
		dest.writeString(signatur);
		dest.writeString(TimeOut);
	}
	public void addFile(String str, String name)
	{
		if (filelist != null && name != null)
		{
			filelist.add(str);
			filelistnames.add(name);
		}
		else
		{
			filelist = new ArrayList<>();
			filelistnames = new ArrayList<>();
			filelist.add(str);
			filelistnames.add(name);
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

	public ArrayList<String> getFilelistnames()
	{
		return filelistnames;
	}

	public void setFilelist(ArrayList<String> filelist)
	{
		this.filelist = filelist;
	}

	public void setFilelistnames(ArrayList<String> filelistnames)
	{
		this.filelistnames = filelistnames;
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

	public void setInMasseg(String inMasseg) {InMasseg = inMasseg;}

	public String getCheckInMessage()
	{
		Utilities.print(TAG, "Call getCheckInMessage()");
		String  ssn = (SiteStoreNumber == null) ? "" : SiteStoreNumber;
		String 	sn	= (StoreName == null) ? "" : StoreName;
		String 	pn 	= (ProjesctName == null) ? "" : ProjesctName;
		String 	tn 	= (TechName == null) ? "" : TechName;
		String 	tpn = (TechPhoneNumber == null) ? "" : TechPhoneNumber;

		// TODO: 8/29/2016  find and delete/populete store name   
		String retval =
				// not sure what is the for : - "Store Name:  " + sn + Utilities.newline +
				"Site/Store Number: " + ssn + Utilities.newline +
				"Project Name: "  + pn + Utilities.newline +
				"Tech Name: " + tn + Utilities.newline +
				"Tech Phone Number: " + tpn + Utilities.newline;

		if (InMasseg != null ) retval += InMasseg + Utilities.newline;

		retval += Utilities.getTimeZon() + Utilities.newline;
		retval += Utilities.getTime();
		Utilities.print("TEST", retval);
		return retval;
	}

	public String getTimeOut()
	{
		return TimeOut;
	}

	public void setTimeOut(String timeOut)
	{
		TimeOut = timeOut;
	}
}
