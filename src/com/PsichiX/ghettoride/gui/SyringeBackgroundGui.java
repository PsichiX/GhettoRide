package com.PsichiX.ghettoride.gui;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Sprite;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Theme;

public class SyringeBackgroundGui extends Sprite
{
	public SyringeBackgroundGui(Theme theme) {
		super(null);
		Material mat = (Material)theme.getOwner().get(R.raw.syringe_background_mat, Material.class);
		Image img = (Image)theme.getOwner().get(R.drawable.syringe_background, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
}
