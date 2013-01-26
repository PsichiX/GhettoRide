package com.PsichiX.ghettoride.gui;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Sprite;
import com.PsichiX.ghettoride.R;

public class SyringeFrontGui extends Sprite {

	private float MAX_WIDTH;
	
	public SyringeFrontGui(XeAssets assets) {
		super(null);
		Material mat = (Material)assets.get(R.raw.syringe_front_mat, Material.class);
		Image img = (Image)assets.get(R.drawable.syringe_front, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
		
		MAX_WIDTH = getWidth();
	}
	
	public void setSize(float multi) {
		setSize(multi*MAX_WIDTH, getHeight());
	}
}
