package com.PsichiX.ghettoride;

import com.PsichiX.XenonCoreDroid.XeApplication.*;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeSense.EventData;
import com.PsichiX.XenonCoreDroid.XeUtils.*;
import com.PsichiX.ghettoride.gui.DistanceTravelled;
import com.PsichiX.ghettoride.gui.GoodBonusGui;
import com.PsichiX.ghettoride.gui.JumpBonusGui;
import com.PsichiX.ghettoride.gui.NiggaIndicator;
import com.PsichiX.ghettoride.gui.StopBonusGui;
import com.PsichiX.ghettoride.gui.SyringeBackgroundGui;
import com.PsichiX.ghettoride.gui.SyringeFrontGui;
import com.PsichiX.XenonCoreDroid.XeEcho;
import com.PsichiX.ghettoride.physics.CollisionManager;

public class GameState extends State implements CommandQueue.Delegate
{
	private SyringeFrontGui _syringeFront;
	private SyringeBackgroundGui _syringeBackground;
	private NiggaIndicator _niggaIndicator;
	private DistanceTravelled _distanceTravelled;
	private GoodBonusGui _goodBonusGui;
	private StopBonusGui _stopBonusGui;
	private JumpBonusGui _jumpBonusGui;
	
	private Camera2D _cam;
	private Scene _scn;
	private ShapeComparator.DescZ _sorter = new ShapeComparator.DescZ();
	private ActorsManager _actors = new ActorsManager();
	private CommandQueue _cmds = new CommandQueue();
	private SpriteSheet _bgSheet;
	private Parallax _parallax;
	private Parallax.Layer _parallaxBg;
	private CollisionManager _collmgr = new CollisionManager();
	private Player _player;
	private SpriteSheet _animSheet;
	private FramesSequence _playerAnim = new FramesSequence();
	private XeEcho.Sound _heartSnd;
	private NiggaCrew _niggaCrew;
	
	@Override
	public void onEnter()
	{
		_cmds.setDelegate(this);
		
		_heartSnd = getApplication().getEcho().loadSound("heart", R.raw.heartbeat);
		_cmds.queueCommand(this, "PlayBeat", null);
		
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		
		_animSheet = (SpriteSheet)getApplication().getAssets().get(R.raw.animations_sheet, SpriteSheet.class);
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("player")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("player1")));
		_playerAnim.addFrame(new FramesSequence.Frame(_animSheet.getSubImage("player2")));
		_playerAnim.setDelay(0.5f);
		
		_bgSheet = (SpriteSheet)getApplication().getAssets().get(R.raw.background_sheet, SpriteSheet.class);
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
		
		_player = new Player(getApplication().getAssets());
		_player.setAnimation(_playerAnim);
		_player.onAttach(_collmgr);
		_player.setPosition(0, 0, -1);
		
		for(int i=0; i<5; i++) {
			Platform tmp = new Platform(getApplication().getAssets());
			_collmgr.attach(tmp);
			_actors.attach(tmp);
			_scn.attach(tmp);
			tmp.calculate();
			tmp.calculateStartPos();
		}
		
		Floor floor1 = new Floor(getApplication().getAssets());
		floor1.setSize(_cam.getViewWidth(), _cam.getViewHeight()*0.1f);
		floor1.setPosition(-_cam.getViewWidth()*0.5f, _cam.getViewHeight()*0.5f);
		floor1.onAttach(_collmgr);
		
		Floor floor2 = new Floor(getApplication().getAssets());
		floor2.setSize(_cam.getViewWidth(), _cam.getViewHeight()*0.1f);
		floor2.setPosition(_cam.getViewWidth()*0.5f, _cam.getViewHeight()*0.5f);
		floor2.onAttach(_collmgr);
		
		_niggaCrew = new NiggaCrew(getApplication().getAssets());
		_niggaCrew.setPosition(_cam.getViewPositionX() - _cam.getViewWidth(), floor1.getRecf().top);
		_actors.attach(_niggaCrew);
		_collmgr.attach(_niggaCrew);
		_scn.attach(_niggaCrew);
		
		_player.setFloorTop(floor1.getRecf().top);
		
		_actors.attach(_player);
		_actors.attach(floor1);
		_actors.attach(floor2);
		
		_scn.attach(_player);
		_scn.attach(floor1);
		_scn.attach(floor2);
		
		_syringeBackground = new SyringeBackgroundGui(getApplication().getAssets());
		_syringeBackground.setPosition(_cam.getViewPositionX(), -_cam.getViewHeight()*0.4f, -1f);
		_scn.attach(_syringeBackground);
		
		_syringeFront = new SyringeFrontGui(getApplication().getAssets());
		_syringeFront.setPosition(_cam.getViewPositionX(), -_cam.getViewHeight()*0.4f, -1f);
		_scn.attach(_syringeFront);
		
		_niggaIndicator = new NiggaIndicator();
		_niggaIndicator.setPosition(_cam.getViewPositionX()-_cam.getViewWidth()*0.5f, -_cam.getViewHeight()*0.5f, -1f);
		_niggaIndicator.build(getApplication().getAssets(), gatNigaDistanceX(_player, _niggaCrew));
		_scn.attach(_niggaIndicator);
		
		_distanceTravelled = new DistanceTravelled();
		_distanceTravelled.setPosition(_cam.getViewPositionX()-_cam.getViewWidth()*0.5f, -_cam.getViewHeight()*0.5f + _niggaIndicator.getHeight(), -1f);
		_distanceTravelled.build(getApplication().getAssets(), _player.getDistanceTravelled());
		_scn.attach(_distanceTravelled);
		
		_goodBonusGui = new GoodBonusGui();
		_goodBonusGui.setPosition(_syringeBackground.getPositionX() + _syringeBackground.getWidth(), -_cam.getViewHeight()*0.5f, -1f);
		_goodBonusGui.build(getApplication().getAssets(), _player.getGoodBonus());
		_scn.attach(_goodBonusGui);
		
		_stopBonusGui = new StopBonusGui();
		_stopBonusGui.setPosition(_syringeBackground.getPositionX() + _syringeBackground.getWidth(), -_cam.getViewHeight()*0.5f + _goodBonusGui.getHeight(), -1f);
		_stopBonusGui.build(getApplication().getAssets(), _player.getStopBonus());
		_scn.attach(_stopBonusGui);
		
		_jumpBonusGui = new JumpBonusGui();
		_jumpBonusGui.setPosition(_syringeBackground.getPositionX() + _syringeBackground.getWidth(), -_cam.getViewHeight()*0.5f + _goodBonusGui.getHeight()*2, -1f);
		_jumpBonusGui.build(getApplication().getAssets(), _player.getJumpBonus());
		_scn.attach(_jumpBonusGui);
		
		getApplication().getTimer().reset();
	}
	
	private float gatNigaDistanceX(ActorSprite a, ActorSprite b) {
		return a.getPositionX() - b.getPositionX() - b.getWidth();
	}
	
	@Override
	public void onExit()
	{
		_scn.detachAll();
		_actors.detachAll();
		_heartSnd.stop();
		getApplication().getEcho().unloadAll();
	}
	
	@Override
	public void onInput(Touches ev)
	{
		_actors.onInput(ev);
	}
	
	@Override
	public void onSensor(EventData ev) {
		_actors.onSensor(ev);
	}
	
	@Override
	public void onUpdate()
	{
		getApplication().getSense().setCoordsOrientation(-1);
		
		float dt = getApplication().getTimer().getDeltaTime() * 0.001f;
		//float dt = 1.0f / 30.0f;
		
		getApplication().getPhoton().clearDrawCalls();
		_cmds.run();
		_actors.onUpdate(dt);
		_collmgr.test();
		_cam.setViewPosition(_player.getPositionX() + _cam.getViewWidth()*0.1f, 0);
		_parallax.setArea(
				_cam.getViewPositionX() - _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY() - _cam.getViewHeight() * 1.0f,
				_cam.getViewWidth() * 2.0f,
		 		_cam.getViewHeight() * 2.0f
				);
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
		
		//_scnGui.update(dt);
	}
	
	public void onCommand(Object sender, String cmd, Object data)
	{
		if(cmd.equals("PlayBeat"))
		{
			_heartSnd.play(0.5f, 0.5f, true, 1.0f);
		}
	}
	
	private void updataGUI() {
		_syringeBackground.setPosition(_cam.getViewPositionX(), _cam.getViewPositionY()-_cam.getViewHeight()*0.4f);
		
		_syringeFront.setPosition(_cam.getViewPositionX(), _cam.getViewPositionY()-_cam.getViewHeight()*0.4f);
		_syringeFront.setSize(_player.getNormPlayerSpeed());
		
		_niggaIndicator.setPosition(_cam.getViewPositionX()-_cam.getViewWidth()*0.5f, -_cam.getViewHeight()*0.5f);
		_niggaIndicator.build(getApplication().getAssets(), gatNigaDistanceX(_player, _niggaCrew));
		
		_distanceTravelled.setPosition(_cam.getViewPositionX()-_cam.getViewWidth()*0.5f, -_cam.getViewHeight()*0.5f/*4f*/ + _niggaIndicator.getHeight(), -1f);
		_distanceTravelled.build(getApplication().getAssets(), _player.getDistanceTravelled());
		
		_goodBonusGui.setPosition(_syringeBackground.getPositionX() + _syringeBackground.getWidth(), -_cam.getViewHeight()*0.5f, -1f);
		_goodBonusGui.build(getApplication().getAssets(), _player.getGoodBonus());
		
		_stopBonusGui.setPosition(_syringeBackground.getPositionX() + _syringeBackground.getWidth(), -_cam.getViewHeight()*0.5f + _goodBonusGui.getHeight(), -1f);
		_stopBonusGui.build(getApplication().getAssets(), _player.getStopBonus());
		
		_jumpBonusGui.setPosition(_syringeBackground.getPositionX() + _syringeBackground.getWidth(), -_cam.getViewHeight()*0.5f + _goodBonusGui.getHeight()*2, -1f);
		//_jumpBonusGui.build(getApplication().getAssets(), _player.getJumpBonus());
		_jumpBonusGui.build(getApplication().getAssets(), getApplication().getPhoton().getDrawCallsCount());
	}
}
