package com.PsichiX.ghettride.gamemenu;

import java.util.List;

import android.util.Log;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorsManager;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Scene;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.SpriteSheet;
import com.PsichiX.XenonCoreDroid.XeApplication.State;
import com.PsichiX.XenonCoreDroid.XeApplication.Touch;
import com.PsichiX.XenonCoreDroid.XeApplication.Touches;
import com.PsichiX.ghettoride.MainActivity;
import com.PsichiX.ghettoride.Parallax;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Theme;
import com.PsichiX.ghettoride.Utils.SheredprefUtils;
import com.PsichiX.ghettoride.common.LevelInfo.Level;

public class GameMenuState extends State {
	private Camera2D _cam;
	private Scene _scn;
	
	private Theme _theme;
	private SpriteSheet _bgSheet;
	private Parallax _parallax;
	private Parallax.Layer _parallaxBg;
	
	private ActorsManager _actormgr;
	private List<Level> levelList;
	private int listPos = 0;
	private ActorSpriteConteiner[] conteinerList;
	
	private int _themeId;
	
	public GameMenuState(int themeId) {
		super();
		this._themeId = themeId;
		levelList = MainActivity.levels.getLevels();
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
		_parallax.setArea(
				_cam.getViewPositionX() - _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY() - _cam.getViewHeight() * 1.0f,
				_cam.getViewWidth() * 2.0f,
		 		_cam.getViewHeight() * 2.0f
				);
		
		_parallaxBg.setPosition(_cam.getViewPositionX(), _cam.getViewPositionY());
		_parallaxBg.setSize(_cam.getViewWidth(), _cam.getViewHeight());
		_parallaxBg.setOffsetFromSize(0.5f, 0.5f);
		
		addLevels();
	}
	
	@Override
	public void onUpdate() {
		getApplication().getSense().setCoordsOrientation(-1);
		
		float dt = getApplication().getTimer().getDeltaTime() * 0.001f;
		
		_actormgr.onUpdate(dt);
		_parallax.onUpdate(dt, 1f);
		
		_scn.update(dt);
	}
	
	private float lastTouchDownX = 0f;
	@Override
	public void onInput(Touches ev) {
		Touch touchDown = ev.getTouchByState(Touch.State.DOWN);
		if(touchDown != null) {
			float[] worldLoc = _scn.getCamera().convertLocationScreenToWorld(touchDown.getX(), touchDown.getY(), -1f);
			lastTouchDownX = worldLoc[0];
		}
		
		Touch touchUp = ev.getTouchByState(Touch.State.UP);
		if(touchUp != null) {
			float[] worldLoc = _scn.getCamera().convertLocationScreenToWorld(touchUp.getX(), touchUp.getY(), -1f);
			if(Math.abs(worldLoc[0] - lastTouchDownX) > 50f) {
				fling(worldLoc[0] > lastTouchDownX);
				return;
			}
		}
		
		_actormgr.onInput(ev);
	}
	
	private void fling(boolean fligRight) {
		if(!fligRight) {
			if(listPos < levelList.size()-1) {
				listPos++;
				updatePosX();
			}
		} else {
			if(listPos > 0) {
				listPos--;
				updatePosX();
			}
		}
	}
	
	private void addLevels() {
		conteinerList = new ActorSpriteConteiner[levelList.size()];
		for(int i=0; i<levelList.size(); i++) {
			conteinerList[i] = new ActorSpriteConteiner();
			
			boolean canStart = SheredprefUtils.getInstance().getUnlockLevel() >= levelList.get(i).getLevel();
			conteinerList[i].gameMenuPanel = new GameMenuPanel(_theme, canStart);
			float offsetX = i*_cam.getViewWidth();
			float posX = offsetX - conteinerList[i].gameMenuPanel.getWidth()*0.5f;
			float posY = conteinerList[i].gameMenuPanel.getHeight()*0.5f;
			conteinerList[i].gameMenuPanel.setPosition(posX, posY);
			_actormgr.attach(conteinerList[i].gameMenuPanel);
			_scn.attach(conteinerList[i].gameMenuPanel);
			
			posX = offsetX;
			posY = -conteinerList[i].gameMenuPanel.getHeight()*0.25f;
			conteinerList[i].lvlNameText = new GameName();
			conteinerList[i].lvlNameText.setPosition(posX, posY);
			conteinerList[i].lvlNameText.build(getApplication().getAssets(), levelList.get(i).getName());
			_scn.attach(conteinerList[i].lvlNameText);
		}
	}
	
	private void updatePosX() {
		for(int i=0; i<conteinerList.length; i++) {
			float offsetX = (i-listPos)*_cam.getViewWidth();
			float posX = offsetX - conteinerList[i].gameMenuPanel.getWidth()*0.5f;
			float posY = conteinerList[i].gameMenuPanel.getHeight()*0.5f;
			conteinerList[i].gameMenuPanel.setPosition(posX, posY);
			
			posX = offsetX;
			posY = -conteinerList[i].gameMenuPanel.getHeight()*0.25f;
			conteinerList[i].lvlNameText.setPosition(posX, posY);
		}
	}
	
	@Override
	public void onExit() {
		Log.d("STATE", "EXIT " + getClass().toString());
		_actormgr.detachAll();
		_scn.detachAll();
	}
	
	class ActorSpriteConteiner {
		public float conteinerPosX;
		
		public GameMenuPanel gameMenuPanel;
		public GameName lvlNameText;
	}
}
