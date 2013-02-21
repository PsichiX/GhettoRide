package com.PsichiX.ghettoride.common;

import android.content.Context;
import android.os.Vibrator;

public class Vibration
{
	//--------VibList--------
	private final long[][] VIB_LIST = {{0, 50}, {0, 100}, {0, 200},
									{5, 100, 10, 100},
									{5, 100, 10, 100}};
	
	private static Vibration myInstance;
	private static Vibrator vib;
	
	//---------Singleton-----------
	public static Vibration getInstance() {
		if(myInstance == null)
			myInstance = new Vibration();
		return myInstance;
	};
	
	public void Init(Context _context) {
		vib = (Vibrator)_context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	public void vibrate(int vibID) {
		vib.vibrate(VIB_LIST[vibID], -1);
	}
	
	public void vibrateShort() {
		vib.vibrate(VIB_LIST[0], -1);
	}
}
