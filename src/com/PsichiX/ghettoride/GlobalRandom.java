package com.PsichiX.ghettoride;

import java.util.Random;

public class GlobalRandom {
	private static Random rand;
	
	public static void init() {
		rand = new Random(System.currentTimeMillis());
	}
	
	public static Random getRandom() {
		return rand;
	}
}
