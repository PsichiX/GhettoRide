package com.PsichiX.ghettoride;

public class TempCommon
{
	public static float modulo(float val, float min, float max)
	{
		float mmin = Math.min(min, max);
		float mmax = Math.max(min, max);
		float r = val - mmin;
		float s = mmax - mmin;
		return (r - ((float)Math.floor(r / s) * s)) + mmin;
	}
}
