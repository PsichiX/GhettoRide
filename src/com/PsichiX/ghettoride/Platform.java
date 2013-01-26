package com.PsichiX.ghettoride;

import java.util.Random;

import android.graphics.RectF;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Image;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.ghettoride.physics.AdrenalinTabs;
import com.PsichiX.ghettoride.physics.CollisionManager;
import com.PsichiX.ghettoride.physics.ICollidable;
import com.PsichiX.ghettoride.physics.ObstacleNet;

public class Platform extends ActorSprite implements ICollidable {
	private static int LAST_PLATFORM_ROLL = 3;
	private static float LAST_PLATFORM_POS_X = 0f;
	
	private CollisionManager collisionManager;
	private Random rand;
	private XeAssets assets;
	
	public Platform(XeAssets assets) {
		super(null);
		this.assets = assets;
		Material mat = (Material)assets.get(R.raw.box_material, Material.class);
		Image img = (Image)assets.get(R.drawable.box, Image.class);
		setMaterial(mat);
		setSizeFromImage(img);
		setOffsetFromSize(0f, 1f);
		
		rand = new Random(System.currentTimeMillis());
	}
	
	public void calculate() {
		Camera2D cam = (Camera2D)getScene().getCamera();
		//minPosY = cam.getViewPositionY() + cam.getViewHeight()*0.4f;//*0.2f;
		//rangePosY = (int)(cam.getViewHeight()*0.4f);//6f);
		
		minPosX = getWidth()*0.5f;//cam.getViewWidth()*0.5f;
		minPosY = cam.getViewHeight()*0.5f;
		//rangePosX = (int)cam.getViewWidth();
		
		/*float[] loc = cam.convertLocationScreenToWorld(0, 450, -1);
		Log.d("min", "plat: " + loc[0] + " " + loc[1]);
		minPlatformPos = loc[1];*/
	}
	
	/*private float minPosY = 0f;
	private int rangePosY = 0;
	private float minPosX = 0f;
	private int rangePosX = 0;*/
	private float minPosY = 0f;
	private float minPosX = 0f;
	@Override
	public void onUpdate(float dt) {
		Camera2D cam = (Camera2D)getScene().getCamera();
		if(getPositionX() == 0 && getPositionY() == 0) {
			calculateNewPost();//cam);
			addItem();
			return;
		}
		
		if(getPositionX() < cam.getViewPositionX() - cam.getViewWidth()*0.5f - getWidth()) {
			calculateNewPost();//cam);
			addItem();
		}
	}
	
	public void calculateStartPos() {
		calculateNewPost();
		//addCollectiables();
	}
	
	private void calculateNewPost(/*Camera2D cam*/) {
		float newPosX = /*cam.getViewPositionX()*/ LAST_PLATFORM_POS_X + minPosX + rand.nextInt((int)minPosX);
		LAST_PLATFORM_POS_X = newPosX + getWidth();
		
		int segmentPos = Math.max(1, rand.nextInt(6) - 3 + LAST_PLATFORM_ROLL);
		segmentPos = Math.min(3, segmentPos);
		LAST_PLATFORM_ROLL = segmentPos;
		//float newPosY = minPosY - rand.nextInt(rangePosY);
		float newPosY = minPosY - segmentPos * getHeight();
		setPosition(newPosX, newPosY);
	}
	
	private void addItem() {
		if(rand.nextFloat() > 0.5f) {
			if(rand.nextFloat() > 0.5) {
				AdrenalinTabs tmp = new AdrenalinTabs(assets);
				tmp.setPosition(getPositionX() + rand.nextInt((int)getWidth()), getRecf().top);
				getManager().attach(tmp);
				getScene().attach(tmp);
				getCollisionManager().attach(tmp);
			} else {
				ObstacleNet tmp = new ObstacleNet(assets);
				tmp.setPosition(getPositionX() + rand.nextInt((int)getWidth()), getRecf().top);
				getManager().attach(tmp);
				getScene().attach(tmp);
				getCollisionManager().attach(tmp);
			}
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
		// TODO Auto-generated method stub
		
	}
}
