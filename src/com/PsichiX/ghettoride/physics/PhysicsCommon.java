package com.PsichiX.ghettoride.physics;

import android.graphics.RectF;

public class PhysicsCommon {
	
	public static boolean isCollision(RectF a, RectF b) {
		if(a.right > b.left && a.bottom > b.top &&
			a.left < b.right && a.top < b.bottom)
			return true;
		return false;
	}
	
}
