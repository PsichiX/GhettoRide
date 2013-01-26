package com.PsichiX.ghettoride;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.Shape;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Sprite;

public class Commons
{	
	public static boolean containst(float x, float y, Sprite actor)
	{
		float minX = actor.getPositionX() - actor.getOffsetX();
		float maxX = minX + actor.getWidth();
		if(minX > x || maxX < x)
			return false;
		
		float minY = actor.getPositionY() - actor.getOffsetY();
		float maxY = minY + actor.getHeight();
		if(minY > y || maxY < y)
			return false;
		
		return true;
	}
	
	public static float modulo(float val, float min, float max)
	{
		float mmin = Math.min(min, max);
		float mmax = Math.max(min, max);
		float r = val - mmin;
		float s = mmax - mmin;
		return (r - ((float)Math.floor(r / s) * s)) + mmin;
	}
	
	public static float distanceX(Shape a, Shape b) {
		return a.getPositionX() - b.getPositionX();
	}
}
