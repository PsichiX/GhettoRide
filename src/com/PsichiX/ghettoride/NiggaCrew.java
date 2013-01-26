package com.PsichiX.ghettoride;

import android.graphics.RectF;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.physics.ICollidable;

public class NiggaCrew extends ActorSprite implements ICollidable{

	private float NIGGA_CREW_SPEED = Player.MAX_SPEED_X*0.3f;
	private float _posX;
	
	private CollisionManager collisionManager;
	
	public NiggaCrew(XeAssets assets) {
		super(null);
		Material mat = (Material)assets.get(R.raw.nigga_crew_mat, Material.class);
		Image img = (Image)assets.get(R.drawable.nigga_crew, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
	
	@Override
	public void onUpdate(float dt) {
		_posX = getPositionX() + NIGGA_CREW_SPEED*dt;
		setPosition(_posX, getPositionY(), -1f);
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
