package com.PsichiX.ghettoride;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;

public class Floor extends Platform {

	public Floor(XeAssets assets) {
		super(assets);
	}
	
	@Override
	public void onUpdate(float arg0) {
		Camera2D cam = (Camera2D)getScene().getCamera();
		if(getPositionX() <= cam.getViewPositionX() - 1.5f*cam.getViewWidth()) {
			setPosition(getPositionX() + 2f*cam.getViewWidth(), getPositionY());
		}
	}

}
