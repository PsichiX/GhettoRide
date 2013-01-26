package com.PsichiX.ghettoride.gui;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Font;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Text;
import com.PsichiX.ghettoride.R;

public class GoodBonusGui extends Text {
	
	public void build(XeAssets assets, float distance) {
		Font font = (Font)assets.get(R.raw.badaboom_font, Font.class);
		Material mat = (Material)assets.get(R.raw.badaboom_material, Material.class);
		
		build("  G_Bns: " + String.format("%.1f", distance),
				font, mat,
				Font.Alignment.TOP,
				Font.Alignment.RIGHT,
				1.0f, 1.0f);
	}
}
