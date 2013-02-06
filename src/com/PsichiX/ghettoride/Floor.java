package com.PsichiX.ghettoride;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;

public class Floor extends Platform {

	public Floor(Theme theme) {
		super(theme);
	}
	
	@Override
	public void onUpdate(float arg0) {
		Camera2D cam = (Camera2D)getScene().getCamera();
		if(getPositionX() <= cam.getViewPositionX() - 0.5f*cam.getViewWidth() - getWidth()) {
			if(GlobalRandom.getRandom().nextFloat() > 0.8f)
				setPosition(getPositionX() + 8f*getWidth(), getPositionY());
			else
				setPosition(getPositionX() + 4f*getWidth(), getPositionY());
			addItem();
		}
	}

}
