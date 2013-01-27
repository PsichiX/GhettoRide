package com.PsichiX.ghettoride.common;

import java.util.ArrayList;

import com.PsichiX.XenonCoreDroid.XeUtils.Message;

public class LevelInfo
{
	private ArrayList<Level> _levels = new ArrayList<Level>();
	
	public ArrayList<Level> getLevels()
	{
		return _levels;
	}
	
	public static class Level
	{
		protected int _level;
		protected String _name;
		protected int _themeId;
		
		public Level(int level, String name, int themeId)
		{
			_level = level;
			_name = name;
			_themeId = themeId;
		}
		
		public int getLevel()
		{
			return _level;
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
