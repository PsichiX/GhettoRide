package com.PsichiX.ghettoride.gui;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Sprite;
import com.PsichiX.ghettoride.R;

public class SyringeBackgroundGui extends Sprite {

	public SyringeBackgroundGui(XeAssets assets) {
		super(null);
		Material mat = (Material)assets.get(R.raw.syringe_background_mat, Material.class);
		Image img = (Image)assets.get(R.drawable.syringe_background, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
}
