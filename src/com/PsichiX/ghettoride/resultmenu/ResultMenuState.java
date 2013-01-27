package com.PsichiX.ghettoride.resultmenu;

import android.util.Log;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorsManager;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Scene;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.SpriteSheet;
import com.PsichiX.XenonCoreDroid.XeApplication.State;
import com.PsichiX.XenonCoreDroid.XeApplication.Touches;
import com.PsichiX.ghettoride.Parallax;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Result;
import com.PsichiX.ghettoride.Theme;

public class ResultMenuState extends State {
	private Camera2D _cam;
	private Scene _scn;
	
	private Theme _theme;
	private ResultMenuDialog _resultMenuDialog;
	private SpriteSheet _bgSheet;
	private Parallax _parallax;
	private Parallax.Layer _parallaxBg;
	
	private ActorsManager _actormgr;
	
	private Result result;
	private int _themeId;
	
	public ResultMenuState(int themeId, Result result) {
		super();
		this._themeId = themeId;
		this.result = result;
	}
	
	@Override
	public void onEnter() {
		Log.d("STATE", "ENTER " + getClass().toString());
		_actormgr = new ActorsManager();
		_theme = (Theme)getApplication().getAssets().get(_themeId, Theme.class);
		
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		_cam.setViewPosition(0f, 0f);
		
		_bgSheet = (SpriteSheet)_theme.getAsset("backgrounds", SpriteSheet.class);
		_parallax = new Parallax();
		_parallax.setScene(_scn);
		_parallaxBg = new Parallax.Layer(_bgSheet.getSubImage("bg"), 0.0f, 0.0f, true);
		_parallax.addLayer(_parallaxBg);
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane2"), 100.0f, 0.0f, false));
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane3"), 250.0f, 0.0f, false));
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane4"), 750.0f, 0.0f, false));
		_parallax.randomizeLayers(
				_cam.getViewPositionX() - _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY(),
				_cam.getViewPositionX() + _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY());
		
		_parallaxBg.setPosition(_cam.getViewPositionX(), _cam.getViewPositionY());
		_parallaxBg.setSize(_cam.getViewWidth(), _cam.getViewHeight());
		_parallaxBg.setOffsetFromSize(0.5f, 0.5f);
		
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
		_parallax.onUpdate(dt, 1f);
		
		_scn.update(dt);
	}
	
	@Override
	public void onInput(Touches arg0) {
		_actormgr.onInput(arg0);
	}
	
	@Override
	public void onExit() {
		Log.d("STATE", "EXIT " + getClass().toString());
		_scn.detachAll();
		_actormgr.detachAll();
	}
}
