package com.PsichiX.ghettoride.physics;

import android.graphics.RectF;

import com.PsichiX.XenonCoreDroid.XeUtils.MathHelper;
import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Theme;

public class Bullet extends ActorSprite implements ICollidable {
	private float BULLET_SPEED;
	private float BULLET_ROTATION = 360f;
	
	private float angle = 0f;
	
	private Theme theme;
	
	public Bullet(Theme theme) {
		super(null);
		Material mat = (Material)theme.getOwner().get(R.raw.bullet_mat, Material.class);
		Image img = (Image)theme.getOwner().get(R.drawable.bullet, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		//setOffsetFromSize(0f, 1f);
		setOffsetFromSize(0.5f, 0.5f);
	}
	
	public void setSpeed(float speed) {
		BULLET_SPEED = speed;
	}
	
	private float[] vec;
	public void setVector(float a_x, float a_y, float b_x, float b_y) {
		vec = MathHelper.vecNormalize(b_x - a_x, b_y - a_y, 0);
		
		double angleRad = Math.atan2(vec[1], vec[0]);
		float angle = (float) Math.toDegrees(angleRad);
		setAngle(angle);
	}
	
	@Override
	public void onUpdate(float dt) {
		float posX = getPositionX() + dt*vec[0]*BULLET_SPEED;
		float posY = getPositionY() + dt*vec[1]*BULLET_SPEED;
		
		Camera2D cam = (Camera2D)getScene().getCamera();
		if(posX > cam.getViewPositionX() + cam.getViewWidth()) {
			getManager().detach(this);
			return;
		}
		
		if(posY < -cam.getViewHeight()*0.5f + getHeight()) {
			getManager().detach(this);
			return;
		}
		
		setPosition(posX, posY);
		angle += dt*BULLET_ROTATION;
		if(angle > 360f)
			angle -= 360f;
		setAngle(angle);
	}

	private CollisionManager collisionManager;
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
