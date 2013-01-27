package com.PsichiX.ghettoride;

import android.graphics.RectF;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorsManager;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.physics.AdrenalinTabs;
import com.PsichiX.ghettoride.physics.Collectibles;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.physics.GoodTabs;
import com.PsichiX.ghettoride.physics.ICollidable;
import com.PsichiX.ghettoride.physics.JumpTab;
import com.PsichiX.ghettoride.physics.ObstacleNet;
import com.PsichiX.ghettoride.physics.StopTab;

public class Platform extends ActorSprite implements ICollidable {
	private static int LAST_PLATFORM_ROLL = 3;
	public static float LAST_PLATFORM_POS_X = 0f;
	public static float ULTIMATE_CAPSULE_DISTANCE = -1.0f;
	
	private CollisionManager collisionManager;
	private Theme theme;
	
	public Platform(Theme th) {
		super(null);
		theme = th;
		Material mat = (Material)theme.getOwner().get(R.raw.box_material, Material.class);
		Image img = (Image)theme.getOwner().get(R.drawable.box, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
	}
	
	public void calculate() {
		Camera2D cam = (Camera2D)getScene().getCamera();
		minPosX = getWidth()*0.5f;
		minPosY = cam.getViewHeight()*0.5f;
	}
	
	private float minPosY = 0f;
	private float minPosX = 0f;
	
	@Override
	public void onUpdate(float dt)
	{
		Camera2D cam = (Camera2D)getScene().getCamera();	
		if(getPositionX() < cam.getViewPositionX() - cam.getViewWidth()*0.5f - getWidth())
		{
			calculateNewPos();
			if(	ULTIMATE_CAPSULE_DISTANCE >= 0.0f &&
				cam.getViewPositionX() > ULTIMATE_CAPSULE_DISTANCE - cam.getViewWidth() &&
				getManager() != null
				)
				getManager().detach(this);
			else
				addItem();
		}
	}
	
	@Override
	public void onDetach(ActorsManager man)
	{
		super.onDetach(man);
		if(getCollisionManager() != null)
			getCollisionManager().detach(this);
	}
	
	public void calculateStartPos() {
		calculateNewPos();
	}
	
	private void calculateNewPos() {
		float newPosX = LAST_PLATFORM_POS_X + minPosX + GlobalRandom.getRandom().nextInt((int)minPosX);
		LAST_PLATFORM_POS_X = newPosX + getWidth();
		
		int segmentPos = 0;
		do {
			segmentPos = Math.max(2, GlobalRandom.getRandom().nextInt(6) - 3 + LAST_PLATFORM_ROLL);
			segmentPos = Math.min(7, segmentPos);
		} while(segmentPos == LAST_PLATFORM_ROLL);
		LAST_PLATFORM_ROLL = segmentPos;
		float newPosY = minPosY - segmentPos * getHeight();
		setPosition(newPosX, newPosY);
	}
	
	private float spawnProp = 0.1f;
	
	protected void addItem() {
		if(GlobalRandom.getRandom().nextFloat() > spawnProp) {
			if(GlobalRandom.getRandom().nextFloat() > 0.45) {
				Collectibles tmp;
				if(GlobalRandom.getRandom().nextFloat() > 0.5) {
					 tmp = new AdrenalinTabs(theme);
				} else if(GlobalRandom.getRandom().nextFloat() > 0.34) {
					tmp = new StopTab(theme);
				} else if(GlobalRandom.getRandom().nextFloat() > 0.17) {
					tmp = new JumpTab(theme);
				} else {
					tmp = new GoodTabs(theme);
				}
				tmp.setPosition(getPositionX() + getWidth()/2 + GlobalRandom.getRandom().nextInt((int)getWidth()/2), getRecf().top);
				getManager().attach(tmp);
				getScene().attach(tmp);
				getCollisionManager().attach(tmp);
			} else {
				ObstacleNet tmp = new ObstacleNet(theme);
				tmp.setPosition(getPositionX() + getWidth()/2 + GlobalRandom.getRandom().nextInt((int)getWidth()/2), getRecf().top);
				getManager().attach(tmp);
				getScene().attach(tmp);
				getCollisionManager().attach(tmp);
			}
			spawnProp = 0.1f;
		} else {
			spawnProp += 0.2f;
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
	}
}
