package com.PsichiX.ghettoride;

import android.graphics.RectF;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.physics.Bullet;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.physics.ICollidable;

public class NiggaCrew extends ActorSprite implements ICollidable{
	private float NIGGA_CREW_SPEED = 200f;//300f;
	private float _posX;
	
	private CollisionManager collisionManager;
	
	private Player _player;
	private Theme theme;
	
	private FramesSequence.Animator _animator;
	
	public NiggaCrew(Theme th, FramesSequence anim) {
		super(null);
		theme = th;
		_animator = new FramesSequence.Animator(anim, this);
		_animator.setDelay(0.05f);
	}
	
	private float lastShootTime = 0f;
	private float SHOOOT_INTERVAL = 2.5f;
	
	@Override
	public void onUpdate(float dt) {
		_animator.update(dt, 1.0f);
		setSize(getWidth() /** 0.8f*/, getHeight() /** 0.8f*/);
		setOffsetFromSize(1.0f, 1.0f);
		
		_posX = getPositionX() + NIGGA_CREW_SPEED*dt;
		setPosition(_posX, getPositionY(), -1f);
		
		lastShootTime += dt;
		
		//Camera2D cam = (Camera2D) getScene().getCamera();
		//if(getPositionX() + getWidth() > cam.getViewPositionX() - cam.getViewWidth()*0.5f) {
			if(lastShootTime > SHOOOT_INTERVAL) {
				Bullet bullet = new Bullet(theme);
				float posX = getPositionX();
				float posY = getPositionY() - getHeight()*0.5f;
				bullet.setPosition(posX, posY);
				//bullet.setVector(posX, posY, _player.getPositionX(), _player.getPositionY() - _player.getHeight()*0.7f);
				getManager().attach(bullet);
				getCollisionManager().attach(bullet);
				getScene().attach(bullet);
				bullet.setSpeed(NIGGA_CREW_SPEED*1.9f);
				lastShootTime = 0f;
			}
		//}
	}

	public void resetSpeed() {
		NIGGA_CREW_SPEED = 0f;
	}
	
	public void setPlayer(Player _player) {
		this._player = _player;
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
		// TODO Auto-generated method stub
	}
}
