package com.PsichiX.ghettoride.gui;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Font;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Text;
import com.PsichiX.ghettoride.R;

public class TabAmmountGui extends Text {
	
	public void build(XeAssets assets, int ammount, float[] color) {
		Font font = (Font)assets.get(R.raw.badaboom_font, Font.class);
		Material mat = (Material)assets.get(R.raw.badaboom_material, Material.class);
		mat.setPropertyVec("uColor",  color);
		build(" x" + String.format("%d", ammount),
				font, mat,
				Font.Alignment.TOP,
				Font.Alignment.RIGHT,
				1.0f, 1.0f);
	}
}
