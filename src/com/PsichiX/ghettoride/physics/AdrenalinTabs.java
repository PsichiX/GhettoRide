package com.PsichiX.ghettoride.physics;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorsManager;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.R;

public class AdrenalinTabs extends Collectibles {

	public AdrenalinTabs(XeAssets assets) {
		super(null);
		Material mat = (Material)assets.get(R.raw.adrenalin_tab_material, Material.class);
		Image img = (Image)assets.get(R.drawable.adrenaline_tab, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
	
	@Override
	public void onDetach(ActorsManager arg0) {
		super.onDetach(arg0);
		getCollisionManager().detach(this);
		//getScene().detach(this);
	}
}
