package com.PsichiX.ghettoride;

import android.graphics.RectF;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.physics.Bullet;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.physics.ICollidable;

public class NiggaCrew extends ActorSprite implements ICollidable{

	private float NIGGA_CREW_SPEED = Player.MAX_SPEED_X*0.3f;
	private float _posX;
	
	private CollisionManager collisionManager;
	private XeAssets assets;
	
	private Player _player;
	
	public NiggaCrew(XeAssets assets) {
		super(null);
		this.assets = assets;
		Material mat = (Material)assets.get(R.raw.nigga_crew_mat, Material.class);
		Image img = (Image)assets.get(R.drawable.nigga_crew, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
	
	public void setPlayer(Player _player) {
		this._player = _player;
	}
	
	private float lastShootTime = 0f;
	private float SHOOOT_INTERVAL = 1.5f;
	@Override
	public void onUpdate(float dt) {
		_posX = getPositionX() + NIGGA_CREW_SPEED*dt;
		setPosition(_posX, getPositionY(), -1f);
		
		lastShootTime += dt;
		getScene().getCamera();
		//if(getPositionX() + getWidth() > cam.getViewPositionX() - cam.getViewWidth()*0.5f) {
			if(lastShootTime > SHOOOT_INTERVAL) {
				Bullet bullet = new Bullet(assets);
				float posX = getPositionX() + getWidth();
				float posY = getPositionY() - getHeight()*0.5f;
				bullet.setPosition(posX, posY);
				bullet.setVector(posX, posY, _player.getPositionX(), _player.getPositionY() - _player.getHeight()*0.7f);
				getManager().attach(bullet);
				getCollisionManager().attach(bullet);
				getScene().attach(bullet);
				lastShootTime = 0f;
			}
		//}
	}

	public void resetSpeed() {
		NIGGA_CREW_SPEED = 0f;
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
