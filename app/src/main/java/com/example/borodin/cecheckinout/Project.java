package com.example.borodin.cecheckinout;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by borodin on 6/10/2016.
 */
public class Project implements Parcelable
{
	private int    id       = -1;
	private String name     = "";
	private String email    = "";
	private String phone    = "";
	private String files    = "";
	private String time_updated    = "";

	public Project(int id, String name, String email, String phone, String files, String time_updated)
	{
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.files = files;
		this.time_updated = time_updated;
	}

	protected Project(Parcel in)
	{
		id = in.readInt();
		name = in.readString();
		email = in.readString();
		phone = in.readString();
		files = in.readString();
		time_updated = in.readString();
	}

	public static final Creator<Project> CREATOR = new Creator<Project>()
	{
		@Override
		public Project createFromParcel(Parcel in)
		{
			return new Project(in);
		}

		@Override
		public Project[] newArray(int size)
		{
			return new Project[size];
		}
	};

	public String toJString()
	{
		String retval = null;
		JSONObject jo = new JSONObject();

		try
		{
			jo.put("name", name);
			jo.put("email", email);
			jo.put("phone", phone);
			jo.put("files", files);
			jo.put("time_updated", time_updated);

			return jo.toString();

		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return retval;
	}

	public JSONObject getJsonObject()
	{
		JSONObject jo = new JSONObject();
		try
		{
			jo.put("id", id);
			jo.put("name", name);
			jo.put("email", email);
			jo.put("phone", phone);
			jo.put("files", files);
			jo.put("time_updated", time_updated);

			return jo;

		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return jo;
	}

	public void fromJson(JSONObject jo)
	{
		try
		{
			id      = jo.getInt("id");
			name    = jo.getString("name");
			email   = jo.getString("email");
			phone   = jo.getString("phone");
			files   = jo.getString("files");
			time_updated   = jo.getString("time_updated");
		} catch (JSONException e)
		{
			id = -1;
			name = "";
			email = "";
			phone = "";
			files = "";
			time_updated = "";
			e.printStackTrace();
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getFiles()
	{
		return files;
	}

	public void setFiles(String files)
	{
		this.files = files;
	}

	public String getTime_updated()
	{
		return time_updated;
	}

	public void setTime_updated(String time_updated)
	{
		this.time_updated = time_updated;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(email);
		dest.writeString(phone);
		dest.writeString(files);
		dest.writeString(time_updated);
	}
}
