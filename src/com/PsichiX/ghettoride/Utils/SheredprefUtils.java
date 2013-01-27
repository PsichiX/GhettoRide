package com.PsichiX.ghettoride.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SheredprefUtils 
{
	private SharedPreferences gameSharedPreferences;
	private SharedPreferences.Editor gameSharedPreferencesEditor;
	
//---------------------------Singleton------------------------------------
	private static SheredprefUtils myInstance;

	public static SheredprefUtils getInstance()
	{
		if(myInstance == null)
			myInstance = new SheredprefUtils();
		return myInstance;
	};
	private SheredprefUtils()
	{};
	
	public void Init(Context _context)
	{
		gameSharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
		gameSharedPreferencesEditor = gameSharedPreferences.edit();
	}

	public int getUnlockLevel()
	{
		int unlockLevel = gameSharedPreferences.getInt("unlock_lvl", 1);
		return unlockLevel;
	}
	
	public void setUnlockLevel(int unlockLevel)
	{
		gameSharedPreferencesEditor.putInt("unlock_lvl", unlockLevel);
		gameSharedPreferencesEditor.commit();
	}
	
	public float getBestScore()
	{
		float unlockLevel = gameSharedPreferences.getFloat("best_score", 0f);
		return unlockLevel;
	}
	
	public void setBestScore(float bestScore)
	{
		gameSharedPreferencesEditor.putFloat("best_score", bestScore);
		gameSharedPreferencesEditor.commit();
	}
	
	public SharedPreferences.Editor getEditor(){
		return gameSharedPreferencesEditor;
	}
}
