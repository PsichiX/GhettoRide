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
	
	public Level getLevelByThemeId(int themeId)
	{
		for(Level lvl : _levels)
			if(lvl.getThemeId() == themeId)
				return lvl;
		return null;
	}
	
	public static class Level
	{
		protected int _level;
		protected String _name;
		protected int _themeId;
		protected float _finishDistance;
		
		public Level(int level, String name, int themeId, float finishDist)
		{
			_level = level;
			_name = name;
			_themeId = themeId;
			_finishDistance = finishDist;
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
		
		public float getFinishDistance()
		{
			return _finishDistance;
		}
	}
}
