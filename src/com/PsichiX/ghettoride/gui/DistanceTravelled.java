package com.PsichiX.ghettoride.gui;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Font;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Text;
import com.PsichiX.ghettoride.R;

public class DistanceTravelled extends Text {

	public void build(XeAssets assets, float distance) {
		Font font = (Font)assets.get(R.raw.badaboom_font, Font.class);
		Material mat = (Material)assets.get(R.raw.badaboom_material, Material.class);
		float[] color = { 1f, 1f, 1f, 1f};
		mat.setPropertyVec("uColor",  color);
		build(" " + String.format("%.1f", distance/100f) + "m dist travelled.",
				font, mat,
				Font.Alignment.TOP,
				Font.Alignment.LEFT,
				1.0f, 1.0f);
	}
}
