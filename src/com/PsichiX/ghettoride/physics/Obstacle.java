package com.PsichiX.ghettoride.physics;

import android.graphics.RectF;
import android.util.Log;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorsManager;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;

public class Obstacle extends ActorSprite implements ICollidable {
	private boolean isActiv = true;
	
	public Obstacle(Material arg0) {
		super(arg0);
	}
	
	@Override
	public void onUpdate(float dt) {
		Camera2D cam = (Camera2D)getScene().getCamera();
		if(getPositionX() < cam.getViewPositionX() - cam.getViewWidth()*0.5f - getWidth()) {
			getManager().detach(this);
			Log.d("del", "delete obst");
		}
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
	public void onDetach(ActorsManager arg0) {
		super.onDetach(arg0);
		getCollisionManager().detach(this);
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
	
	public boolean inactiv() {
		if(isActiv) {
			isActiv = false;
			return true;
		} else {
			return false;
		}
	}
}
