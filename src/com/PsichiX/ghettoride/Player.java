package com.PsichiX.ghettoride;

import android.graphics.RectF;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Actors.IActor;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.XeApplication.Touch;
import com.PsichiX.XenonCoreDroid.XeApplication.Touches;
import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.ghettoride.physics.AdrenalinTabs;
import com.PsichiX.ghettoride.physics.Bullet;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.physics.GoodTabs;
import com.PsichiX.ghettoride.physics.ICollidable;
import com.PsichiX.ghettoride.physics.JumpTab;
import com.PsichiX.ghettoride.physics.Obstacle;
import com.PsichiX.ghettoride.physics.StopTab;

public class Player extends ActorSprite implements ICollidable {
	private CollisionManager collisionManager;
	
	private float _distanceTraveled = 0f;
	private float _timePlayed = 0f;
	
	private boolean isAlive = true;
	
	private float _histPosY = 0f;
	private float _posX = 0.0f;
	private float _posY = 0.0f;
	
	private float ADRENALIN_LEVEL = 0.8f;
	private float WORK_AREA;
	private float _spdX = 100f;
	private float _spdY = -50f;
	private FramesSequence.Animator _animator;
	
	private boolean isOnGround = false;
	private boolean isRollin = false;
	
	private NiggaCrew niggaCrew;
	
	private float floorTop = 0f;
	
	private float obstacleTime = 0f;
	private float adrenalineTime = 0f;
	private float stopBonusTime = 0f;
	private float goodBonusTime = 0f;
	private float jumpBonusTime = 0f;
	
	private float deltaAdrenaline = -0.02f;
	
	public Player(XeAssets assets) {
		super(null);
		Material mat = (Material)assets.get(R.raw.player_material, Material.class);
		Image img = (Image)assets.get(R.drawable.player, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
		_animator = new FramesSequence.Animator(null, this);
	}
	
	public void setAnimation(FramesSequence anim) {
		_animator.setOwner(anim);
	}
	
	public void setNiggaCrew(NiggaCrew niggaCrew) {
		this.niggaCrew = niggaCrew;
	}
	
	public void calculate() {
		Camera2D cam = (Camera2D)getScene().getCamera();
		WORK_AREA = cam.getViewWidth()*0.75f;
	}
	
	private float lastTouchDownY = 0f;
	private float MIN_INPUT_DIFF = 20f;
	@Override
	public void onInput(Touches ev) {
		Touch touchDown = ev.getTouchByState(Touch.State.DOWN);
		if(touchDown != null)
		{
			Camera2D cam = (Camera2D)getScene().getCamera();
			float[] worldLoc = cam.convertLocationScreenToWorld(touchDown.getX(), touchDown.getY(), -1f);
			lastTouchDownY = worldLoc[1];
		}
		
		Touch touchUp = ev.getTouchByState(Touch.State.IDLE);
		if(touchUp != null)
		{
			Camera2D cam = (Camera2D)getScene().getCamera();
			float[] worldLoc = cam.convertLocationScreenToWorld(touchUp.getX(), touchUp.getY(), -1f);
			if(lastTouchDownY != -999f && Math.abs(worldLoc[1] - lastTouchDownY) > MIN_INPUT_DIFF) {
				if(worldLoc[1] < lastTouchDownY) {
					jump();
				} else if(worldLoc[1] > lastTouchDownY) {
					fall();
				}
				lastTouchDownY = -999f;
			}
		}
	}
	
	@Override
	public void onUpdate(float dt)
	{
		_histPosY = _posY;
		
		_distanceTraveled += _spdX * dt;
		_timePlayed += dt;
		
		_posY -= _spdY * dt;
		//_posX += _spdX * dt;
		_posX = niggaCrew.getPositionX() + WORK_AREA*ADRENALIN_LEVEL - 1;
		//Log.d("event", "_posY: " + _posY);
		if(!isOnGround) {
			flying(dt);
		}
		
		setPosition(_posX, _posY);
		
		isOnGround = false;
		
		_animator.update(dt, 1.0f);
		if(stopBonusTime == 0f)
			addAdrenaline(dt*deltaAdrenaline);
		
		decreaseBonus(dt);
	}
	
	private void decreaseBonus(float dt) {
		if(stopBonusTime > 0f) {
			stopBonusTime -= dt;
			if(stopBonusTime < 0f) {
				stopBonusTime = 0f;
				deltaAdrenaline = -0.02f;
			}
		}
		
		if(goodBonusTime > 0f) {
			goodBonusTime -= dt;
			if(goodBonusTime < 0f)
				goodBonusTime = 0f;
		}
		
		if(jumpBonusTime > 0f) {
			jumpBonusTime -= dt;
			if(jumpBonusTime < 0f) {
				jumpBonusTime = 0f;
			}
		}
		
		if(adrenalineTime > 0f) {
			adrenalineTime -= dt;
			if(adrenalineTime < 0f) {
				adrenalineTime = 0f;
				deltaAdrenaline = -0.02f;
			}
		}
		
		if(obstacleTime > 0f) {
			obstacleTime -= dt;
			if(obstacleTime < 0f) {
				obstacleTime = 0f;
				deltaAdrenaline = -0.02f;
			}
		}
	}
	
	private void jump() {
		if(isOnGround && !isRollin) {
			isOnGround = false;
			if(jumpBonusTime > 0) {
				_spdY = 1000f;
			} else {
				_spdY = 700f;
			}
		}
	}
	
	private void fall() {
		if(isOnGround && _posY != floorTop) {
			_posY += 0.1f;
			_histPosY = _posY;
			isOnGround = false;
		}
	}
	
	private void flying(float dt) {
		_spdY -= 1700f * dt;
	}
	
	private void touchGround(float groundPosY) {
		_posY = groundPosY;
		isOnGround = true;
		_spdY = -50f;
		
		setPosition(_posX, _posY);
	}
	
	public void setFloorTop(float floorTop) {
		this.floorTop = floorTop;
	}

	@Override
	public void onAttach(CollisionManager m) {
		m.attach(this);
		collisionManager = m;
	}

	@Override
	public void onDetach(CollisionManager m) {
		m.detach(this);
		if(collisionManager == m) {
			collisionManager = null;
		}
	}

	@Override
	public CollisionManager getCollisionManager() {
		return collisionManager;
	}
	
	@Override
	public void setRectF(RectF rec) {
	}

	@Override
	public RectF getRecf() {
		float left = getPositionX() - getOffsetX();
		float top = getPositionY() - getOffsetY();
		return new RectF(left, top, left + getWidth(), top + getHeight());
	}

	@Override
	public void onCollision(ICollidable o) {
		if(o instanceof Platform) 
		{
			if(_histPosY <= _posY && _histPosY <= o.getRecf().top) {
				touchGround(o.getRecf().top);
				isOnGround = true;
			}
		} 
		else if(o instanceof AdrenalinTabs) 
		{
			getManager().detach((IActor) o);
			//addAdrenaline(0.2f);
			adrenalineTime += 3f;
			deltaAdrenaline = 0.1f;
		} 
		else if(o instanceof StopTab) 
		{
			getManager().detach((IActor) o);
			stopBonusTime += 5f;
			//addAdrenaline(0.1f);
			deltaAdrenaline = 0f;
		}
		else if(o instanceof GoodTabs) 
		{
			getManager().detach((IActor) o);
			goodBonusTime += 5f;
			//addAdrenaline(0.1f);
		} 
		else if(o instanceof JumpTab) 
		{
			getManager().detach((IActor) o);
			jumpBonusTime += 5f;
			//addAdrenaline(0.1f);
		} 
		else if(o instanceof Obstacle) 
		{
			if(((Obstacle)o).inactiv() && goodBonusTime == 0f) {
				//addAdrenaline(-0.1f);
				obstacleTime += 1f;
				deltaAdrenaline = -0.06f;
			}
		} 
		else if(o instanceof Bullet) 
		{
			getManager().detach((IActor) o);
			//addAdrenaline(-0.1f);
			obstacleTime += 3f;
			deltaAdrenaline = -0.04f;
		} 
		else if(o instanceof NiggaCrew) 
		{
			resetSpeed();
			isAlive = false;
		} 
	}
	
	private void addAdrenaline(float deltaSpeed) {
		ADRENALIN_LEVEL += deltaSpeed;
		if(ADRENALIN_LEVEL < 0f)
			ADRENALIN_LEVEL = 0f;
		if(ADRENALIN_LEVEL > 1f)
			ADRENALIN_LEVEL = 1f;
	}
	
	private void resetSpeed() {
		_spdX = 0f;
	}
	
	public float getNormPlayerSpeed() {
		return ADRENALIN_LEVEL;
	}
	
	public float getDistanceTravelled() {
		return _distanceTraveled;
	}
	
	public float getStopBonus() {
		return stopBonusTime;
	}
	
	public float getGoodBonus() {
		return goodBonusTime;
	}
	
	public float getJumpBonus() {
		return jumpBonusTime;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public Result getResult() {
		Result result = new Result();
		result.setDistanceTravelled(_distanceTraveled);
		result.setTimePlayed(_timePlayed);
		return result;
	}
}
