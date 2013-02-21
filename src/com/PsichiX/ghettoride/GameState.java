package com.PsichiX.ghettoride;

import android.util.Log;

import com.PsichiX.XenonCoreDroid.XeApplication.*;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeSense.EventData;
import com.PsichiX.XenonCoreDroid.XeUtils.*;
import com.PsichiX.ghettoride.gui.DistanceTravelled;
import com.PsichiX.ghettoride.gui.SyringeBackgroundGui;
import com.PsichiX.ghettoride.gui.SyringeFrontGui;
import com.PsichiX.ghettoride.gui.TabAmmountGui;
import com.PsichiX.ghettoride.gui.TabButtonGui;
import com.PsichiX.XenonCoreDroid.XeEcho;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.resultmenu.ResultMenuState;

public class GameState extends State implements CommandQueue.Delegate
{
	private SyringeFrontGui _syringeFront;
	private SyringeBackgroundGui _syringeBackground;
	//private NiggaIndicator _niggaIndicator;
	private DistanceTravelled _distanceTravelled;
	
	private TabAmmountGui _adrenalinBonusGui;
	private TabButtonGui _adrenalineBtnGui;
	
	private TabAmmountGui _godBonusGui;
	private TabButtonGui _godBtnGui;
	
	private TabAmmountGui _stopBonusGui;
	private TabButtonGui _stopBtnGui;
	
	private TabAmmountGui _jumpBonusGui;
	private TabButtonGui _jumpBtnGui;
	
	private Camera2D _cam;
	private Scene _scn;
	private ShapeComparator.DescZ _sorter;
	private ActorsManager _actors;
	private CommandQueue _cmds;
	private SpriteSheet _bgSheet;
	private Parallax _parallax;
	private Parallax.Layer _parallaxBg;
	private CollisionManager _collmgr;
	private Player _player;
	private SpriteSheet _animSheet;
	private SpriteSheet _animBlackSheet;
	private FramesSequence _playerAnim;
	private FramesSequence _playerBlackAnim;
	private XeEcho.Sound _heartSnd;
	private NiggaCrew _niggaCrew;
	private Theme _theme;
	private int _themeId = 0;
	private float _ultCapsDist = -1.0f;
	private boolean _finishing = false;
	
	public static float ULTIMATE_CAPSULE_DIST = -1.0f;
	
	public GameState(int themeId, float ultimCapsDist)
	{
		super();
		_themeId = themeId;
		_ultCapsDist = ultimCapsDist;
	}
	
	@Override
	public void onEnter()
	{
		ULTIMATE_CAPSULE_DIST = _ultCapsDist;
		
		_sorter = new ShapeComparator.DescZ();
		_actors = new ActorsManager();
		_cmds = new CommandQueue();
		_collmgr = new CollisionManager();
		_playerAnim = new FramesSequence();
		_playerBlackAnim = new FramesSequence();
		
		_theme = (Theme)getApplication().getAssets().get(_themeId, Theme.class);
		
		_cmds.setDelegate(this);
		
		_heartSnd = getApplication().getEcho().loadSound("heart", R.raw.heartbeat);
		_cmds.queueCommand(this, "PlayBeat", null);
		
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		_cam.setViewPosition(0f, 0f);
		
		_animSheet = (SpriteSheet)_theme.getAsset("animations", SpriteSheet.class);
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run1")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run2")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run3")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run4")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run5")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run6")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run7")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run8")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run9")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run10")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("run11")));
		
		_animBlackSheet = (SpriteSheet)_theme.getAsset("animations_black", SpriteSheet.class);
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run1")));
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run2")));
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run3")));
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run4")));
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run5")));
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run6")));
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run7")));
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run8")));
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run9")));
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run10")));
		_playerBlackAnim.addFrame(new FramesSequence.Frame(_animBlackSheet.getSubImage("run11")));
		
		_bgSheet = (SpriteSheet)_theme.getAsset("backgrounds", SpriteSheet.class);
		_parallax = new Parallax();
		_parallax.setScene(_scn);
		_parallaxBg = new Parallax.Layer(_bgSheet.getSubImage("bg"), 0.0f, 0.0f, true);
		_parallax.addLayer(_parallaxBg);
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane2"), -20.0f, 0.0f, false));
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane3"), -50.0f, 0.0f, false));
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane4"), -100.0f, 0.0f, false));
		_parallax.randomizeLayers(
				_cam.getViewPositionX() - _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY(),
				_cam.getViewPositionX() + _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY());
		
		_player = new Player(_playerAnim);
		_collmgr.attach(_player);
		_player.setPosition(0, 0, -1);
		
		Platform.ULTIMATE_CAPSULE_DISTANCE = _ultCapsDist;
		for(int i=0; i<3; i++) {
			Platform tmp = new Platform(_theme);
			_collmgr.attach(tmp);
			_actors.attach(tmp);
			_scn.attach(tmp);
			tmp.calculate();
			tmp.calculateStartPos();
		}
		
		Floor floor1 = new Floor(_theme);
		floor1.setPosition(-_cam.getViewWidth()*0.5f, _cam.getViewHeight()*0.55f);
		_collmgr.attach(floor1);
		_actors.attach(floor1);
		_scn.attach(floor1);
		
		Floor floor2 = new Floor(_theme);
		floor2.setPosition(-_cam.getViewWidth()*0.5f + floor1.getWidth(), _cam.getViewHeight()*0.55f);
		_collmgr.attach(floor2);
		_actors.attach(floor2);
		_scn.attach(floor2);
		
		Floor floor3 = new Floor(_theme);
		floor3.setPosition(-_cam.getViewWidth()*0.5f + floor1.getWidth()*2, _cam.getViewHeight()*0.55f);
		_collmgr.attach(floor3);
		_actors.attach(floor3);
		_scn.attach(floor3);
		
		Floor floor4 = new Floor(_theme);
		floor4.setPosition(-_cam.getViewWidth()*0.5f + floor1.getWidth()*3, _cam.getViewHeight()*0.55f);
		_collmgr.attach(floor4);
		_actors.attach(floor4);
		_scn.attach(floor4);
		
		_niggaCrew = new NiggaCrew(_theme, _playerBlackAnim);
		_niggaCrew.setPosition(-_cam.getViewWidth()*0.4f, floor1.getRecf().top);
		_actors.attach(_niggaCrew);
		_collmgr.attach(_niggaCrew);
		_scn.attach(_niggaCrew);
		
		_niggaCrew.setPlayer(_player);
		_player.setNiggaCrew(_niggaCrew);
		_player.setFloorTop(floor1.getRecf().top);
		
		_actors.attach(_player);
		
		_scn.attach(_player);
		_player.calculate();
		
		_syringeBackground = new SyringeBackgroundGui(_theme);
		_scn.attach(_syringeBackground);
		
		_syringeFront = new SyringeFrontGui(getApplication().getAssets());
		_scn.attach(_syringeFront);
		
		_distanceTravelled = new DistanceTravelled();
		_scn.attach(_distanceTravelled);
		
		SpriteSheet _guiSheet = (SpriteSheet)_theme.getAsset("gui", SpriteSheet.class);
		
		_adrenalineBtnGui = new TabButtonGui(_theme, _guiSheet.getSubImage("adrenaline_tab_btn"));
		_scn.attach(_adrenalineBtnGui);
		
		_adrenalinBonusGui = new TabAmmountGui();
		_scn.attach(_adrenalinBonusGui);
		
		_godBtnGui = new TabButtonGui(_theme, _guiSheet.getSubImage("good_tab_btn"));
		_scn.attach(_godBtnGui);
		
		_godBonusGui = new TabAmmountGui();
		_scn.attach(_godBonusGui);
		
		_stopBtnGui = new TabButtonGui(_theme, _guiSheet.getSubImage("stop_tab_btn"));
		_scn.attach(_stopBtnGui);
		
		_stopBonusGui = new TabAmmountGui();
		_scn.attach(_stopBonusGui);
		
		_jumpBtnGui = new TabButtonGui(_theme, _guiSheet.getSubImage("jump_tab_btn"));
		_scn.attach(_jumpBtnGui);
		
		_jumpBonusGui = new TabAmmountGui();
		_scn.attach(_jumpBonusGui);
		
		getApplication().getTimer().reset();
	}
	
	@Override
	public void onExit()
	{
		Platform.LAST_PLATFORM_POS_X = 0f;
		_scn.detachAll();
		_actors.detachAll();
		_heartSnd.stop();
		getApplication().getEcho().unloadAll();
	}
	
	@Override
	public void onInput(Touches ev)
	{
		Touch touch = ev.getTouchByState(Touch.State.DOWN);
		if(touchHit(touch, _cam, _adrenalineBtnGui) || touchHit(touch, _cam, _adrenalinBonusGui)) {
			Log.d("TOUCH", "adrenalin btn");
			_player.takeTab(TabsType.ADRENALINE);
			return;
		}
		
		if(touchHit(touch, _cam, _godBtnGui) || touchHit(touch, _cam, _godBonusGui)) {
			Log.d("TOUCH", "god btn");
			_player.takeTab(TabsType.GOD);
			return;
		}
		
		if(touchHit(touch, _cam, _stopBtnGui) || touchHit(touch, _cam, _stopBonusGui)) {
			Log.d("TOUCH", "stop btn");
			_player.takeTab(TabsType.STOP);
			return;
		}
		
		if(touchHit(touch, _cam, _jumpBtnGui) || touchHit(touch, _cam, _jumpBonusGui)) {
			Log.d("TOUCH", "jump btn");
			_player.takeTab(TabsType.JUMP);
			return;
		}
		
		if(!_finishing)
			_actors.onInput(ev);
	}
	
	@Override
	public void onSensor(EventData ev) {
		_actors.onSensor(ev);
	}
	
	@Override
	public void onUpdate()
	{
		float dt = getApplication().getTimer().getDeltaTime() * 0.001f;
		//float dt = 1.0f / 30.0f;
		
		getApplication().getPhoton().clearDrawCalls();
		_cmds.run();
		_actors.onUpdate(dt);
		_collmgr.test();
		
		//_cam.setViewPosition(_player.getPositionX() + _cam.getViewWidth()*0.1f, 0);
		float camrx = _cam.getViewPositionX();
		float camry = _cam.getViewPositionY();
		_cam.setViewPosition(_niggaCrew.getPositionX() + _cam.getViewWidth()*0.375f, 0);
		camrx = _cam.getViewPositionX() - camrx;
		camry = _cam.getViewPositionY() - camry;
		
		if(_ultCapsDist >= 0.0f && _cam.getViewPositionX() > _ultCapsDist)
			_finishing = true;
		_parallax.setArea(
				_cam.getViewPositionX() - _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY() - _cam.getViewHeight() * 1.0f,
				_cam.getViewWidth() * 2.0f,
		 		_cam.getViewHeight() * 2.0f
				);
		_parallax.moveLayers(camrx, camry);
		_parallax.onUpdate(dt, _player.getNormPlayerSpeed());
		_parallaxBg.setPosition(_cam.getViewPositionX(), _cam.getViewPositionY());
		_parallaxBg.setSize(_cam.getViewWidth(), _cam.getViewHeight());
		_parallaxBg.setOffsetFromSize(0.5f, 0.5f);
		_heartSnd.setSpeed(0.5f + (_player.getNormPlayerSpeed() * 1.5f));
		float vol = 0.25f + ((1.0f - _player.getNormPlayerSpeed()) * 0.5f);
		_heartSnd.setVolume(vol, vol);
		
		updataGUI();
		
		_scn.sort(_sorter);
		_scn.update(dt);
		
		if(!_player.isAlive()) {
			getApplication().changeState(new ResultMenuState(_themeId, _player.getResult()));
		}
	}
	
	public void onCommand(Object sender, String cmd, Object data)
	{
		if(cmd.equals("PlayBeat"))
		{
			_heartSnd.play(0.5f, 0.5f, true, 1.0f);
		}
	}
	
	float[] notUsedColor = { 1f, 1f, 1f, 1f};
	float[] usedColor = { 1f, 1f, 0f, 0f};
	
	private void updataGUI() {
		_syringeBackground.setPosition(_cam.getViewPositionX(), _cam.getViewPositionY()-_cam.getViewHeight()*0.4f);
		
		_syringeFront.setPosition(_cam.getViewPositionX(), _cam.getViewPositionY()-_cam.getViewHeight()*0.4f);
		_syringeFront.setSize(_player.getNormPlayerSpeed());
		
		_distanceTravelled.setPosition(_syringeBackground.getPositionX() + _syringeBackground.getWidth()/*_cam.getViewPositionX()-_cam.getViewWidth()*0.5f*/, -_cam.getViewHeight()*0.5f/* + _niggaIndicator.getHeight()*/, -1f);
		_distanceTravelled.build(getApplication().getAssets(), _player.getDistanceTravelled());
		
		//float tabBonusGuiPosX = _syringeBackground.getPositionX() + _syringeBackground.getWidth();
		float tabBonusGuiPosX = _cam.getViewPositionX() - _cam.getViewWidth()*0.5f;
		float tabBonusGuiPosY = -_cam.getViewHeight()*0.5f;
		float tabTopMargin = _cam.getViewHeight()*0.05f;
		
		float tabNrBonusGuiPosX = tabBonusGuiPosX + _adrenalineBtnGui.getWidth();
		
		_adrenalinBonusGui.setPosition(tabNrBonusGuiPosX, tabBonusGuiPosY, -1f);
		_adrenalinBonusGui.build(getApplication().getAssets(), _player.getAdrenalineTabsCnt(), getTextColor(_player.getAdrenalineBonus()));
		_adrenalineBtnGui.setPosition(tabBonusGuiPosX, tabBonusGuiPosY, -1f);
		
		_godBonusGui.setPosition(tabNrBonusGuiPosX, tabBonusGuiPosY + _adrenalineBtnGui.getHeight() + tabTopMargin, -1f);
		_godBonusGui.build(getApplication().getAssets(), _player.getGoodTabsCnt(), getTextColor(_player.getGoodBonus()));
		_godBtnGui.setPosition(tabBonusGuiPosX, tabBonusGuiPosY + _adrenalineBtnGui.getHeight() + tabTopMargin, -1f);
		
		_stopBonusGui.setPosition(tabNrBonusGuiPosX, tabBonusGuiPosY + (_adrenalineBtnGui.getHeight() + tabTopMargin)*2, -1f);
		_stopBonusGui.build(getApplication().getAssets(), _player.getStopTabsCnt(), getTextColor(_player.getStopBonus()));
		_stopBtnGui.setPosition(tabBonusGuiPosX, tabBonusGuiPosY + (_adrenalineBtnGui.getHeight() + tabTopMargin)*2, -1f);
		
		_jumpBonusGui.setPosition(tabNrBonusGuiPosX, tabBonusGuiPosY + (_adrenalineBtnGui.getHeight() + tabTopMargin)*3, -1f);
		_jumpBonusGui.build(getApplication().getAssets(), _player.getJumpTabsCnt(), getTextColor(_player.getJumpBonus()));
		_jumpBtnGui.setPosition(tabBonusGuiPosX, tabBonusGuiPosY + (_adrenalineBtnGui.getHeight() + tabTopMargin)*3, -1f);
		
		//Log.d("TIME", String.format("%.3f", _player.getAdrenalineBonus()) + " " + String.format("%.3f", _player.getGoodBonus()) + " " + String.format("%.3f", _player.getStopBonus()) + " " + String.format("%.3f", _player.getJumpBonus()));
	}
	
	private float[] getTextColor(float bonusTime) {
		return bonusTime > 0f ? usedColor : notUsedColor;
	}
	
	
	public static boolean touchHit(Touch t, ICamera cam, Sprite s)
	{
	       if(t == null || cam == null || s == null)
	               return false;
	       float[] loc = cam.convertLocationScreenToWorld(t.getX(), t.getY(), -1.0f);
	       float nx = s.getPositionX() - s.getOffsetX();
	       float ny = s.getPositionY() - s.getOffsetY();
	       float px = nx + s.getWidth();
	       float py = ny + s.getHeight();
	       return loc[0] > nx && loc[0] < px && loc[1] > ny && loc[1] < py;
	}
	
	public static boolean touchHit(Touch t, ICamera cam, Text s)
	{
	       if(t == null || cam == null || s == null)
	               return false;
	       float[] loc = cam.convertLocationScreenToWorld(t.getX(), t.getY(), -1.0f);
	       float nx = s.getPositionX() - s.getOffsetX();
	       float ny = s.getPositionY() - s.getOffsetY();
	       float px = nx + s.getWidth();
	       float py = ny + s.getHeight();
	       return loc[0] > nx && loc[0] < px && loc[1] > ny && loc[1] < py;
	}
}
