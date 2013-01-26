package com.PsichiX.ghettoride;

import android.graphics.RectF;
import android.util.Log;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Actors.IActor;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.XeApplication.Touch;
import com.PsichiX.XenonCoreDroid.XeApplication.Touches;
import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.ghettoride.physics.AdrenalinTabs;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.physics.GoodTabs;
import com.PsichiX.ghettoride.physics.ICollidable;
import com.PsichiX.ghettoride.physics.JumpTab;
import com.PsichiX.ghettoride.physics.Obstacle;
import com.PsichiX.ghettoride.physics.StopTab;

public class Player extends ActorSprite implements ICollidable {
	private CollisionManager collisionManager;
	
	private float _distanceTraveled = 0f;
	
	private float _histPosX = 0f;
	private float _histPosY = 0f;
	private float _posX = 0.0f;
	private float _posY = 0.0f;
	
	public static float MAX_SPEED_X = 500f;//1000f;
	public static float MIN_SPEED_X = 50f;
	private float _spdX = 250f;//500f;
	private float _spdY = -50f;
	private FramesSequence.Animator _animator;
	
	private float lastY = 0f;
	
	private boolean isOnGround = false;
	private boolean isRollin = false;
	
	private float floorTop = 0f;
	
	private float stopBonusTime = 0f;
	private float goodBonusTime = 0f;
	private float jumpBonusTime = 0f;
	
	public Player(XeAssets assets) {
		super(null);
		Material mat = (Material)assets.get(R.raw.player_material, Material.class);
		Image img = (Image)assets.get(R.drawable.player, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
		_animator = new FramesSequence.Animator(null, this);
	}
	
	public void setAnimation(FramesSequence anim)
	{
		_animator.setOwner(anim);
	}
	
	private boolean isTouchDownPlayer = false;
	private float lastTouchDownX = 0f;
	private float lastTouchDownY = 0f;
	
	private float MIN_INPUT_DIFF = 20f;
	@Override
	public void onInput(Touches ev) {
		Touch touchDown = ev.getTouchByState(Touch.State.DOWN);
		if(touchDown != null)
		{
			Camera2D cam = (Camera2D)getScene().getCamera();
			float[] worldLoc = cam.convertLocationScreenToWorld(touchDown.getX(), touchDown.getY(), -1f);
			lastTouchDownX = worldLoc[0];
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
		/*
		Touch touchUp = ev.getTouchByState(Touch.State.UP);
		if(touchUp != null)
		{
			Camera2D cam = (Camera2D)getScene().getCamera();
			float[] worldLoc = cam.convertLocationScreenToWorld(touchUp.getX(), touchUp.getY(), -1f);
			//if(isTouchDownPlayer) {
				if(worldLoc[1] < lastTouchDownY) {
					jump();
				} else if(worldLoc[1] > lastTouchDownY) {
					fall();
				}
			//}
			//isTouchDownPlayer = false;
		}
		*/
	}
	
	@Override
	public void onUpdate(float dt)
	{
		/*Camera2D cam = (Camera2D)getScene().getCamera();
		float w = cam.getViewWidth() * 0.5f;
		float h = cam.getViewHeight() * 0.5f;*/
		_histPosX = _posX;
		_histPosY = _posY;
		
		_distanceTraveled += _spdX * dt;
		
		_posY -= _spdY * dt;
		_posX += _spdX * dt;
		//Log.d("event", "_posY: " + _posY);
		if(!isOnGround) {
			flying(dt);
		}
		
		setPosition(_posX, _posY);
		
		isOnGround = false;
		
		_animator.update(dt, 1.0f);
		if(stopBonusTime == 0f)
			addSpeed(-dt*0.02f*MAX_SPEED_X);
		
		decreaseBonus(dt);
	}
	
	private void decreaseBonus(float dt) {
		if(stopBonusTime > 0f) {
			stopBonusTime -= dt;
			if(stopBonusTime < 0f)
				stopBonusTime = 0f;
		}
		
		if(goodBonusTime > 0f) {
			goodBonusTime -= dt;
			if(goodBonusTime < 0f)
				goodBonusTime = 0f;
		}
		
		if(jumpBonusTime > 0f) {
			jumpBonusTime -= dt;
			if(jumpBonusTime < 0f)
				jumpBonusTime = 0f;
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
		Log.d("event", "fall " + isOnGround + " " +  _posY + " " + floorTop);
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
		if(o instanceof Platform) {
			if(_histPosY <= _posY && _histPosY <= o.getRecf().top) {
				touchGround(o.getRecf().top);
				isOnGround = true;
			}
		} else if(o instanceof AdrenalinTabs) {
			getManager().detach((IActor) o);
			addSpeed(0.25f*MAX_SPEED_X);
		} else if(o instanceof StopTab) {
			getManager().detach((IActor) o);
			stopBonusTime += 5f;
			addSpeed(0.1f*MAX_SPEED_X);
		} else if(o instanceof GoodTabs) {
			getManager().detach((IActor) o);
			goodBonusTime += 5f;
			addSpeed(0.1f*MAX_SPEED_X);
		} else if(o instanceof JumpTab) {
			getManager().detach((IActor) o);
			jumpBonusTime += 5f;
			addSpeed(0.1f*MAX_SPEED_X);
		} else if(o instanceof Obstacle) {
			if(((Obstacle)o).inactiv() && goodBonusTime == 0f)
				addSpeed(-0.1f*MAX_SPEED_X);
		} else if(o instanceof NiggaCrew) {
			resetSpeed();
			((NiggaCrew)o).resetSpeed();
		} 
	}
	
	private void addSpeed(float deltaSpeed) {
		_spdX += deltaSpeed;
		if(_spdX < MIN_SPEED_X)
			_spdX = MIN_SPEED_X;
		if(_spdX > MAX_SPEED_X)
			_spdX = MAX_SPEED_X;
	}
	
	private void resetSpeed() {
		_spdX = 0f;
	}
	
	public float getNormPlayerSpeed() {
		return _spdX/MAX_SPEED_X;
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
}
