package com.PsichiX.ghettoride.gui;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.SpriteSheet.SubImage;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Theme;

public class TabButtonGui extends ActorSprite {

	public TabButtonGui(Theme theme, SubImage img) {
		super(null);
		Material mat = (Material)theme.getOwner().get(R.raw.gui_mat, Material.class);
		//Image img = (Image)theme.getOwner().get(resImg, Image.class);
		setMaterial(mat);
		img.apply(this);
		setOffsetFromSize(0f, 0f);
	}
}
