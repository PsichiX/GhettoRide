package com.PsichiX.ghettoride.physics;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorsManager;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.R;

public class GoodTabs extends Collectibles {

	public GoodTabs(XeAssets assets) {
		super(null);
		Material mat = (Material)assets.get(R.raw.good_tab, Material.class);
		Image img = (Image)assets.get(R.drawable.good_tab, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
}
