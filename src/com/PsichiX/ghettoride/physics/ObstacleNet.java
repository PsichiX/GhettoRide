package com.PsichiX.ghettoride.physics;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Theme;

public class ObstacleNet extends Obstacle {
	
	public ObstacleNet(Theme theme) {
		super(null);
		Material mat = (Material)theme.getOwner().get(R.raw.obstacle_net_material, Material.class);
		Image img = (Image)theme.getOwner().get(R.drawable.obstacle_net, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
}
