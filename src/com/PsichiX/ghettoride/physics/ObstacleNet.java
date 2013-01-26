package com.PsichiX.ghettoride.physics;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.R;

public class ObstacleNet extends Obstacle {
	
	public ObstacleNet(XeAssets assets) {
		super(null);
		Material mat = (Material)assets.get(R.raw.obstacle_net_material, Material.class);
		Image img = (Image)assets.get(R.drawable.obstacle_net, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
}
