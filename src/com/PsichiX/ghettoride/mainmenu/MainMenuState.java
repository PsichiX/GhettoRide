package com.PsichiX.ghettoride.mainmenu;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorsManager;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Scene;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.SpriteSheet;
import com.PsichiX.XenonCoreDroid.XeApplication.State;
import com.PsichiX.XenonCoreDroid.XeApplication.Touches;
import com.PsichiX.ghettoride.Parallax;
import com.PsichiX.ghettoride.Platform;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Theme;

public class MainMenuState extends State {
	private Camera2D _cam;
	private Scene _scn;
	
	private Theme _theme;
	private MainMenuPanel _mainMenuPanel;
	private SpriteSheet _bgSheet;
	private Parallax _parallax;
	private Parallax.Layer _parallaxBg;
	
	private ActorsManager _actormgr = new ActorsManager();
	
	private int _themeId;
	
	public MainMenuState(int themeId) {
		super();
		this._themeId = themeId;
	}
	
	@Override
	public void onEnter() {
		_theme = (Theme)getApplication().getAssets().get(_themeId, Theme.class);
		
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		
		_bgSheet = (SpriteSheet)_theme.getAsset("backgrounds", SpriteSheet.class);
		_parallax = new Parallax();
		_parallax.setScene(_scn);
		_parallaxBg = new Parallax.Layer(_bgSheet.getSubImage("bg"), 0.0f, 0.0f, true);
		_parallax.addLayer(_parallaxBg);
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane2"), -10.0f, 0.0f, false));
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane3"), -25.0f, 0.0f, false));
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane4"), -75.0f, 0.0f, false));
		_parallax.randomizeLayers(
				_cam.getViewPositionX() - _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY(),
				_cam.getViewPositionX() + _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY());
		_parallax.setArea(
				_cam.getViewPositionX() - _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY() - _cam.getViewHeight() * 1.0f,
				_cam.getViewWidth() * 2.0f,
		 		_cam.getViewHeight() * 2.0f
				);
		
		_parallaxBg.setPosition(_cam.getViewPositionX(), _cam.getViewPositionY());
		_parallaxBg.setSize(_cam.getViewWidth(), _cam.getViewHeight());
		_parallaxBg.setOffsetFromSize(0.5f, 0.5f);
		
		_mainMenuPanel = new MainMenuPanel(_theme);
		_mainMenuPanel.setPosition(-_cam.getViewWidth()*0.5f, _cam.getViewPositionY() + _mainMenuPanel.getHeight()*0.5f);
		_actormgr.attach(_mainMenuPanel);
		_scn.attach(_mainMenuPanel);
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
	public void onExit()
	{
		_scn.detachAll();
		_actormgr.detachAll();
	}
}
