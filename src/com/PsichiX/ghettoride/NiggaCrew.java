package com.PsichiX.ghettoride;

import android.graphics.RectF;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.physics.Bullet;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.physics.ICollidable;

public class NiggaCrew extends ActorSprite implements ICollidable{
	private float NIGGA_CREW_SPEED = 300f;
	private float _posX;
	
	private CollisionManager collisionManager;
	
	private Player _player;
	private Theme theme;
	
	public NiggaCrew(Theme theme) {
		super(null);
		this.theme = theme;
		Material mat = (Material)theme.getOwner().get(R.raw.nigga_crew_mat, Material.class);
		Image img = (Image)theme.getOwner().get(R.drawable.nigga_crew, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(1f, 1f);
	}
	
	private float lastShootTime = 0f;
	private float SHOOOT_INTERVAL = 2.5f;
	@Override
	public void onUpdate(float dt) {
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
				bullet.setSpeed(NIGGA_CREW_SPEED*1.4f);
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
