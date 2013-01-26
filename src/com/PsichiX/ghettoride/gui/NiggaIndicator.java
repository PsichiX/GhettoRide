package com.PsichiX.ghettoride.gui;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Font;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Text;
import com.PsichiX.ghettoride.R;

public class NiggaIndicator extends Text {
	
	public void build(XeAssets assets, float distance) {
		Font font = (Font)assets.get(R.raw.badaboom_font, Font.class);
		Material mat = (Material)assets.get(R.raw.badaboom_material, Material.class);
		
		build(" " + String.format("%.0f", distance) + " dist",
				font, mat,
				Font.Alignment.TOP,
				Font.Alignment.LEFT,
				1.0f, 1.0f);
	}
}
