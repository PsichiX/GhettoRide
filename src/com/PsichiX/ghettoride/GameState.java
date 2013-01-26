package com.PsichiX.ghettoride;

import com.PsichiX.XenonCoreDroid.XeApplication.*;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeSense.EventData;
import com.PsichiX.XenonCoreDroid.XeUtils.*;
import com.PsichiX.ghettoride.physics.CollisionManager;

public class GameState extends State implements CommandQueue.Delegate
{
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
	
	@Override
	public void onEnter()
	{
		_cmds.setDelegate(this);
		
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		
		_bgSheet = (SpriteSheet)getApplication().getAssets().get(R.raw.background_sheet, SpriteSheet.class);
		_parallax = new Parallax();
		_parallax.setScene(_scn);
		_parallaxBg = new Parallax.Layer(_bgSheet.getSubImage("bg"), 0.0f, 0.0f, true);
		_parallax.addLayer(_parallaxBg);
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane2"), -100.0f, 0.0f, false));
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane3"), -40.0f, 0.0f, false));
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane4"), -10.0f, 0.0f, false));
		_parallax.randomizeLayers(
				_cam.getViewPositionX() - _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY(),
				_cam.getViewPositionX() + _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY()
				);

		_player = new Player(getApplication().getAssets());
		_player.onAttach(_collmgr);
		_player.setPosition(0, 0, -1);
		
		Platform box = new Platform(getApplication().getAssets());
		box.onAttach(_collmgr);
		box.setPosition(20, -50);
		
		Platform floor = new Platform(getApplication().getAssets());
		floor.setSize(_cam.getViewWidth(), _cam.getViewHeight()*0.1f);
		floor.setPosition(-_cam.getViewWidth()*0.5f, _cam.getViewHeight()*0.5f);
		floor.onAttach(_collmgr);
		
		_actors.attach(_player);
		_scn.attach(_player);
		_scn.attach(box);
		_scn.attach(floor);
	}
	
	@Override
	public void onExit()
	{
		_scn.detachAll();
		_actors.detachAll();
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
		_cam.setViewPosition(_player.getPositionX() + _cam.getViewWidth()*0.25f, 0);
		_parallax.setArea(
				_cam.getViewPositionX() - _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY() - _cam.getViewHeight() * 1.0f,
				_cam.getViewWidth() * 2.0f,
		 		_cam.getViewHeight() * 2.0f
				);
		_parallax.onUpdate(dt);
		_parallaxBg.setPosition(_cam.getViewPositionX(), _cam.getViewPositionY());
		_parallaxBg.setSize(_cam.getViewWidth(), _cam.getViewHeight());
		_parallaxBg.setOffsetFromSize(0.5f, 0.5f);
		_scn.sort(_sorter);
		_scn.update(dt);
	}
	
	public void onCommand(Object sender, String cmd, Object data)
	{
		
	}
}
