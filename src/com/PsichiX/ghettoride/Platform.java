package com.PsichiX.ghettoride;

import android.graphics.RectF;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Sprite;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.physics.ICollidable;

public class Platform extends Sprite implements ICollidable {
	private CollisionManager collisionManager;
	
	public Platform(XeAssets assets) {
		super(null);
		Material mat = (Material)assets.get(R.raw.box_material, Material.class);
		Image img = (Image)assets.get(R.drawable.box, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
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

	private RectF collisionRect;
	@Override
	public void setRectF(RectF rec) {
		collisionRect = rec;
	}

	@Override
	public RectF getRecf() {
		if(collisionRect != null) {
			return collisionRect;
		} else {
			float left = getPositionX() - getOffsetX();
			float top = getPositionY() - getOffsetY();
			return new RectF(left, top, left + getWidth(), top + getHeight());
		}
	}

	@Override
	public void onCollision(ICollidable o) {
		// TODO Auto-generated method stub
		
	}
}
