package com.PsichiX.ghettoride.common;

import java.util.ArrayList;

public class LevelInfo
{
	private ArrayList<Level> _levels = new ArrayList<Level>();
	
	public ArrayList<Level> getLevels()
	{
		return _levels;
	}
	
	public static class Level
	{
		protected String _name;
		protected int _themeId;
		
		public Level(String name, int themeId)
		{
			_name = name;
			_themeId = themeId;
		}
		
		public String getName()
		{
			return _name;
		}
		
		public int getThemeId()
		{
			return _themeId;
		}
	}
}
