package com.PsichiX.ghettride.gamemenu;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Font;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Text;
import com.PsichiX.ghettoride.R;

public class GameName extends Text {
	
	public void build(XeAssets assets, String str) {
		Font font = (Font)assets.get(R.raw.badaboom_font, Font.class);
		Material mat = (Material)assets.get(R.raw.badaboom_material, Material.class);
		
		build(str, font, mat,
				Font.Alignment.BOTTOM,
				Font.Alignment.CENTER,
				1.0f, 1.0f);
	}
}
