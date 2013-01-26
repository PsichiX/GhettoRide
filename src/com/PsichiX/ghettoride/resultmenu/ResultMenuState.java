package com.PsichiX.ghettoride.resultmenu;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorsManager;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Scene;
import com.PsichiX.XenonCoreDroid.XeApplication.State;
import com.PsichiX.XenonCoreDroid.XeApplication.Touches;
import com.PsichiX.ghettoride.R;

public class ResultMenuState extends State {
	private Camera2D _cam;
	private Scene _scn;
	
	private ResultMenuDialog _resultMenuDialog;
	
	private ActorsManager _actormgr = new ActorsManager();
	
	public ResultMenuState(float distanceTravelled) {
		super();
	}
	
	@Override
	public void onEnter() {
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		
		_resultMenuDialog = new ResultMenuDialog(getApplication().getAssets());
		_resultMenuDialog.setPosition(_cam.getViewPositionX() - _resultMenuDialog.getWidth()*0.5f, _cam.getViewPositionY() + _resultMenuDialog.getHeight()*0.5f);
		_actormgr.attach(_resultMenuDialog);
		_scn.attach(_resultMenuDialog);
	}
	
	@Override
	public void onUpdate() {
		getApplication().getSense().setCoordsOrientation(-1);
		
		float dt = getApplication().getTimer().getDeltaTime() * 0.001f;
		
		_actormgr.onUpdate(dt);
		_scn.update(dt);
	}
	
	@Override
	public void onInput(Touches arg0) {
		_actormgr.onInput(arg0);
	}
}
