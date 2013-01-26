package com.PsichiX.ghettoride.mainmenu;

import android.util.Log;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.XeApplication.Touch;
import com.PsichiX.XenonCoreDroid.XeApplication.Touches;
import com.PsichiX.ghettoride.MainActivity;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Theme;
import com.PsichiX.ghettride.gamemenu.GameMenuState;

public class MainMenuPanel extends ActorSprite {
	private Theme theme;
	
	public MainMenuPanel(Theme th) {
		super(null);
		theme = th;
		Material mat = (Material)theme.getOwner().get(R.raw.main_menu_mat, Material.class);
		Image img = (Image)theme.getOwner().get(R.drawable.main_menu, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
	
	@Override
	public void onInput(Touches ev) {
		//Touch touchDown = ev.getTouchByState(Touch.State.DOWN);
		Touch touchDown = ev.getTouchByState(Touch.State.UP);
		if(touchDown != null)
		{
			Camera2D cam = (Camera2D)getScene().getCamera();
			float[] worldLoc = cam.convertLocationScreenToWorld(touchDown.getX(), touchDown.getY(), -1f);
			if(worldLoc[0] > cam.getViewPositionX() - cam.getViewWidth()*0.5f &&
				worldLoc[0] < cam.getViewPositionX() - cam.getViewWidth()*0.5f + getWidth())
			{
				
				if(worldLoc[1] > cam.getViewPositionY() - getHeight()*0.25f &&
					worldLoc[1] < cam.getViewPositionY()) 
				{
					Log.d("MAIN_MENU", "GRA");
					MainActivity.app.pushState(new GameMenuState(theme.getId()));
				} 
				else if(worldLoc[1] > cam.getViewPositionY()  &&
						worldLoc[1] < cam.getViewPositionY() + getHeight()*0.25f) 
				{
					Log.d("MAIN_MENU", "HI SCORE");
				}
				else if(worldLoc[1] > cam.getViewPositionY() + getHeight()*0.25f &&
						worldLoc[1] < cam.getViewPositionY() + getHeight()*0.5f) 
				{
					Log.d("MAIN_MENU", "CREDITS");
				}
			}
		}
	}
}
