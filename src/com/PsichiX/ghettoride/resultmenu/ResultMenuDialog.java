package com.PsichiX.ghettoride.resultmenu;

import android.util.Log;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.XeApplication.Touch;
import com.PsichiX.XenonCoreDroid.XeApplication.Touches;
import com.PsichiX.ghettoride.GameState;
import com.PsichiX.ghettoride.MainActivity;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Theme;

public class ResultMenuDialog extends ActorSprite {
	private Theme theme;
	
	public ResultMenuDialog(Theme th) {
		super(null);
		theme = th;
		Material mat = (Material)theme.getOwner().get(R.raw.result_menu_dialog, Material.class);
		Image img = (Image)theme.getOwner().get(R.drawable.result_menu_dialog, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
	
	@Override
	public void onInput(Touches ev) {
		Touch touchDown = ev.getTouchByState(Touch.State.DOWN);
		if(touchDown != null)
		{
			Camera2D cam = (Camera2D)getScene().getCamera();
			float[] worldLoc = cam.convertLocationScreenToWorld(touchDown.getX(), touchDown.getY(), -1f);
			if(worldLoc[1] > cam.getViewPositionY() + getHeight()*0.25f && 
				worldLoc[1] < cam.getViewPositionY() + getHeight()*0.5f) {
				if(worldLoc[0] > cam.getViewPositionX() - getWidth()*0.5f && 
					worldLoc[0] < cam.getViewPositionX()) {
					Log.d("TOUCH", "RESTART");
					MainActivity.app.changeState(new GameState(theme.getId()));
				} else if(worldLoc[0] > cam.getViewPositionX() &&
						worldLoc[0] < cam.getViewPositionX() + getWidth()*0.5f) {
					Log.d("TOUCH", "TO MAIN MENU");
					MainActivity.app.popState();
				}
			}
		}
	}
}
