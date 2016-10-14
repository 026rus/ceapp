package com.example.borodin.cecheckinout;

import java.sql.Timestamp;

/**
 * Created by borodin on 6/10/2016.
 */
public class Question
{
	private int ID;
	private String qestion;
	private int project;
	private Timestamp time;
	private String questionType;
	private boolean completed;
	private int order;

	public Question(int id, String qestion, String type, int project, int order)
	{
		this.ID = id;
		this.qestion = qestion;
		this.project = project;
		this.completed = false;
		this.questionType = type;
		this.order = order;
	}

	public String getQestion()
	{
		return qestion;
	}

	public void setQestion(String qestion)
	{
		this.qestion = qestion;
	}

	public int getProject()
	{
		return project;
	}

	public void setProject(int project)
	{
		this.project = project;
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int ID)
	{
		this.ID = ID;
	}

	public Timestamp getTime()
	{
		return time;
	}

	public void setTime(Timestamp time)
	{
		this.time = time;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public String getQuestionType()
	{
		return questionType;
	}

	public void setQuestionType(String questionType)
	{
		this.questionType = questionType;
	}

	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}
}
