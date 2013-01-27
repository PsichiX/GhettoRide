package com.PsichiX.ghettride.gamemenu;

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

public class GameMenuPanel extends ActorSprite {
	private Theme theme;
	private boolean canStart;
	
	public GameMenuPanel(Theme th, boolean canStart) {
		super(null);
		theme = th;
		this.canStart = canStart;
		Material mat = (Material)theme.getOwner().get(R.raw.game_menu_mat, Material.class);
		Image img = (Image)theme.getOwner().get(R.drawable.game_menu, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
	
	@Override
	public void onInput(Touches ev) {
		if(!canStart)
			return;
		
		Camera2D cam = (Camera2D)getScene().getCamera();
		if(getPositionX() < -cam.getViewWidth() || getPositionX() > cam.getViewWidth())
			return;
		
		Touch touchDown = ev.getTouchByState(Touch.State.UP);
		if(touchDown != null)
		{
			float[] worldLoc = cam.convertLocationScreenToWorld(touchDown.getX(), touchDown.getY(), -1f);
			if(worldLoc[1] > cam.getViewPositionY() - getHeight()*0.25f &&
				worldLoc[1] < cam.getViewPositionY()) 
			{
				if(worldLoc[0] > cam.getViewPositionX() - getWidth()*0.5f &&
					worldLoc[0] < cam.getViewPositionX())
				{
					Log.d("MAIN_MENU", "STORY");
					MainActivity.app.changeState(new GameState(theme.getId()));
				} 
				else if(worldLoc[0] > cam.getViewPositionX() &&
						worldLoc[0] < cam.getViewPositionX() + getWidth()*0.5f)
				{
					Log.d("MAIN_MENU", "FREE");
					MainActivity.app.changeState(new GameState(theme.getId()));
				}
			}
		}
	}
}
