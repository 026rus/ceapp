package com.example.borodin.cecheckinout;

import java.util.Comparator;

/**
 * Created by borodin on 8/29/2016.
 */
public class ProjectComparator implements Comparator<Project>
{
	@Override
	public int compare(Project lhs, Project rhs)
	{
		return lhs.getName().compareTo(rhs.getName());
	}
}
