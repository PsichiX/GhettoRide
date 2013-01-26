package com.PsichiX.ghettoride.physics;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorsManager;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Theme;

public class StopTab extends Collectibles {

	public StopTab(Theme theme) {
		super(null);
		Material mat = (Material)theme.getOwner().get(R.raw.stop_tab_mat, Material.class);
		Image img = (Image)theme.getOwner().get(R.drawable.stop_tab, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
}
