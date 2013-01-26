package com.PsichiX.ghettoride;

import android.graphics.RectF;
import android.util.Log;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.XeApplication.Touch;
import com.PsichiX.XenonCoreDroid.XeApplication.Touches;
import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.XeSense.EventData;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.physics.ICollidable;

public class Player extends ActorSprite implements ICollidable {
	private CollisionManager collisionManager;
	
	private float _histPosX = 0f;
	private float _histPosY = 0f;
	private float _posX = 0.0f;
	private float _posY = 0.0f;
	private float _spdX = 100.0f;
	private float _spdY = 0f;//100.0f;
	
	private float lastY = 0f;
	
	private boolean isOnGround = false;// true;
	private boolean isRollin = false;
	
	public Player(XeAssets assets) {
		super(null);
		Material mat = (Material)assets.get(R.raw.player_material, Material.class);
		Image img = (Image)assets.get(R.drawable.player, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
	
	@Override
	public void onSensor(EventData ev) {
		/*
		if(ev.type == XeSense.Type.ACCELEROMETER) {
			ev.owner.remapCoords(ev.values, 1, 0);
			float[] v = MathHelper.vecNormalize(ev.values[0], ev.values[1], ev.values[2]);
			
			//_movX = Math.max(-0.2f, Math.min(0.2f, v[0])) * 5.0f;
			//_movY = Math.max(-0.2f, Math.min(0.2f, v[1])) * 5.0f;
			float Y = v[1];
			if(lastY == 0f)
				lastY = v[1];
			float diff = Y - lastY;
			
			float BORDER = 0.6f;
			
			if(Math.abs(diff) > BORDER)
				Log.d("event", "border: " + diff + " " + Y + " " + lastY);
			
			if(diff > BORDER && isOnGround) {
				isOnGround = false;
				_spdY = 100f;
				Log.d("event", "JUMP");
			}
			lastY = Y;
			
			Log.d("event", "acc: " + String.format("%.2f", Y) + " diff " + String.format("%.2f", diff));
			
			//Log.d("event", "acc: " + String.format("%.2f", Y) + " diff " + String.format("%.2f", diff));
			//Log.d("event", "acc: " + String.format("%.2f", ev.values[0]) + " diff " + String.format("%.2f", ev.values[1]));
		}
		*/
	}
	
	private boolean isTouchDownPlayer = false;
	private float lastTouchDownX = 0f;
	private float lastTouchDownY = 0f;
	
	@Override
	public void onInput(Touches ev) {
		Touch touchDown = ev.getTouchByState(Touch.State.DOWN);
		if(touchDown != null)
		{
			Camera2D cam = (Camera2D)getScene().getCamera();
			float[] worldLoc = cam.convertLocationScreenToWorld(touchDown.getX(), touchDown.getY(), -1f);
			if(Commons.containst(worldLoc[0], worldLoc[1], this)) {
				isTouchDownPlayer = true;
				lastTouchDownX = worldLoc[0];
				lastTouchDownY = worldLoc[1];
			}
		}
		
		Touch touchUp = ev.getTouchByState(Touch.State.UP);
		if(touchUp != null)
		{
			Camera2D cam = (Camera2D)getScene().getCamera();
			float[] worldLoc = cam.convertLocationScreenToWorld(touchUp.getX(), touchUp.getY(), -1f);
			if(isTouchDownPlayer) {
				if(worldLoc[1] < lastTouchDownY) {
					jump();
				} else if(worldLoc[1] > lastTouchDownY) {
					roll();
				}
			}
			isTouchDownPlayer = false;
		}
	}
	
	@Override
	public void onUpdate(float dt)
	{
		/*Camera2D cam = (Camera2D)getScene().getCamera();
		float w = cam.getViewWidth() * 0.5f;
		float h = cam.getViewHeight() * 0.5f;*/
		_histPosX = _posX;
		_histPosY = _posY;
		
		_posY -= _spdY * dt;
		_posX += _spdX * dt;
		Log.d("event", "_posY: " + _posY);
		if(!isOnGround) {
			flying(dt);
		}
		
		if(isRollin) {
			rolling(dt);
		}
		
		setPosition(
			//Math.max(-w, Math.min(w, _x + _movX * _spdX * dt)),
			//Math.max(-h, Math.min(h, _y + _movY * _spdY * dt))
			//w, h
			_posX, _posY
			);
	}
	
	private void jump() {
		if(isOnGround && !isRollin) {
			isOnGround = false;
			_spdY = 500f;
			Log.d("event", "JUMP");
		}
	}
	
	private void roll() {
		if(isOnGround && !isRollin) {
			isRollin = true;
			Log.d("event", "ROOL");
			this.setAngle(-90f);
		}
	}
	
	private void flying(float dt) {
		/*if(_posY > 0) {
			touchGround(0f);
		} else {*/
			_spdY -= 700f * dt;
		//}
	}
	
	private void touchGround(float groundPosY) {
		_posY = groundPosY;
		isOnGround = true;
		_spdY = 0f;
	}
	
	private float rollingTime = 0f;
	private float rollingMaxTime = 1f;
	private void rolling(float dt) {
		rollingTime += dt;
		if(rollingTime > rollingMaxTime) {
			isRollin = false;
			rollingTime = 0;
			this.setAngle(0f);
		}
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
			Log.d("COL", "BOX COLL");
			if(_histPosY < _posY && _histPosY < o.getRecf().top) {
				touchGround(o.getRecf().top);
			}
		}
	}
}
