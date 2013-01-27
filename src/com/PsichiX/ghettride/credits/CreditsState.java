package com.PsichiX.ghettride.credits;

import android.util.Log;

import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorsManager;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Font;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Scene;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.SpriteSheet;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Text;
import com.PsichiX.XenonCoreDroid.XeApplication.State;
import com.PsichiX.XenonCoreDroid.XeApplication.Touches;
import com.PsichiX.ghettoride.Parallax;
import com.PsichiX.ghettoride.R;
import com.PsichiX.ghettoride.Theme;

public class CreditsState  extends State {
	private Camera2D _cam;
	private Scene _scn;
	
	private Theme _theme;
	private SpriteSheet _bgSheet;
	private Parallax _parallax;
	private Parallax.Layer _parallaxBg;
	
	//private ActorsManager _actormgr;
	private IAnimable[] animTab;
	private int _themeId;
	
	public CreditsState(int themeId) {
		super();
		this._themeId = themeId;
	}
	
	@Override
	public void onEnter() {
		Log.d("STATE", "ENTER " + getClass().toString());
		//_actormgr = new ActorsManager();
		_theme = (Theme)getApplication().getAssets().get(_themeId, Theme.class);
		
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		_cam.setViewPosition(0f, 0f);
		
		_bgSheet = (SpriteSheet)_theme.getAsset("backgrounds", SpriteSheet.class);
		_parallax = new Parallax();
		_parallax.setScene(_scn);
		_parallaxBg = new Parallax.Layer(_bgSheet.getSubImage("bg"), 0.0f, 0.0f, true);
		_parallax.addLayer(_parallaxBg);
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane2"), 100.0f, 0.0f, false));
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane3"), 250.0f, 0.0f, false));
		_parallax.addLayer(new Parallax.Layer(_bgSheet.getSubImage("crane4"), 750.0f, 0.0f, false));
		_parallax.randomizeLayers(
				_cam.getViewPositionX() - _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY(),
				_cam.getViewPositionX() + _cam.getViewWidth() * 1.0f,
				_cam.getViewPositionY());
		
		_parallaxBg.setPosition(_cam.getViewPositionX(), _cam.getViewPositionY());
		_parallaxBg.setSize(_cam.getViewWidth(), _cam.getViewHeight());
		_parallaxBg.setOffsetFromSize(0.5f, 0.5f);
		
		
		animTab = new IAnimable[8];
		float posY = -_cam.getViewHeight()*0.5f + 50f;
		CreditsLeftText title = new CreditsLeftText();
		title.build(getApplication().getAssets(), "foka studio presents");
		title.setPosition(-_cam.getViewWidth()*0.5f, posY, -1);
		title.setTargetPos(title.getWidth()*0.5f, posY);
		animTab[0] = title;
		posY += title.getHeight();
		_scn.attach(title);
		
		CreditsRightText productName = new CreditsRightText();
		productName.build(getApplication().getAssets(), "\"ghetto ride\"");
		productName.setPosition(_cam.getViewWidth()*0.5f, posY, -1);
		productName.setTargetPos(-productName.getWidth()*0.5f, posY);
		animTab[1] = productName;
		posY += productName.getHeight();
		_scn.attach(productName);
		
		CreditsLeftText programmers  = new CreditsLeftText();
		programmers.build(getApplication().getAssets(), "Engine programmer:");
		programmers.setPosition(-_cam.getViewWidth()*0.5f, posY, -1);
		programmers.setTargetPos(programmers.getWidth()*0.5f, posY);
		animTab[2] = programmers;
		posY += programmers.getHeight();
		_scn.attach(programmers);
		
		CreditsRightText psichix = new CreditsRightText();
		psichix.build(getApplication().getAssets(), "PsichiX");
		psichix.setPosition(_cam.getViewWidth()*0.5f, posY, -1);
		psichix.setTargetPos(-psichix.getWidth()*0.5f, posY);
		animTab[3] = psichix;
		posY += psichix.getHeight();
		_scn.attach(psichix);
		
		CreditsLeftText gameplay  = new CreditsLeftText();
		gameplay.build(getApplication().getAssets(), "Gameplay programmer:");
		gameplay.setPosition(-_cam.getViewWidth()*0.5f, posY, -1);
		gameplay.setTargetPos(gameplay.getWidth()*0.5f, posY);
		animTab[4] = gameplay;
		posY += gameplay.getHeight();
		_scn.attach(gameplay);
		
		CreditsRightText admund = new CreditsRightText();
		admund.build(getApplication().getAssets(), "admund");
		admund.setPosition(_cam.getViewWidth()*0.5f, posY, -1);
		admund.setTargetPos(-admund.getWidth()*0.5f, posY);
		animTab[5] = admund;
		posY += admund.getHeight();
		_scn.attach(admund);
		
		CreditsLeftText graphics  = new CreditsLeftText();
		graphics.build(getApplication().getAssets(), "Graphics:");
		graphics.setPosition(-_cam.getViewWidth()*0.5f, posY, -1);
		graphics.setTargetPos(graphics.getWidth()*0.5f, posY);
		animTab[6] = graphics;
		posY += graphics.getHeight();
		_scn.attach(graphics);
		
		CreditsRightText wx45 = new CreditsRightText();
		wx45.build(getApplication().getAssets(), "wx45");
		wx45.setPosition(_cam.getViewWidth()*0.5f, posY, -1);
		wx45.setTargetPos(-wx45.getWidth()*0.5f, posY);
		animTab[7] = wx45;
		posY += wx45.getHeight();
		_scn.attach(wx45);
	}
	
	private IAnimable isSomeToAnim() {
		for(IAnimable tmp : animTab) {
			if(!tmp.isFinished())
				return tmp;
		}
		return null;
	}
	
	@Override
	public void onUpdate() {
		getApplication().getSense().setCoordsOrientation(-1);
		
		float dt = getApplication().getTimer().getDeltaTime() * 0.001f;
		
		//_actormgr.onUpdate(dt);
		_parallax.onUpdate(dt, 1f);
		
		IAnimable anim = isSomeToAnim();
		if(anim != null) {
			anim.anim(dt);
		}
		
		_scn.update(dt);
	}
	
	@Override
	public void onInput(Touches arg0) {
		;//_actormgr.onInput(arg0);
	}
	
	@Override
	public void onExit() {
		Log.d("STATE", "EXIT " + getClass().toString());
		_scn.detachAll();
		;//_actormgr.detachAll();
	}
	
	
	private float animSpeeed = 800f;
	class CreditsLeftText extends Text implements IAnimable {
		private float animSpeed = animSpeeed;
		private float targetPosX;
		private float targetPosY;
		private boolean isFinishAnim = false;
		
		public void setTargetPos(float x, float y) {
			targetPosX = x;
			targetPosY = y;
		}
		
		public void build(XeAssets assets, String str) {
			Font font = (Font)assets.get(R.raw.badaboom_font, Font.class);
			Material mat = (Material)assets.get(R.raw.badaboom_material, Material.class);
			float[] color = { 1f, 1f, 1f, 1f};
			mat.setPropertyVec("uColor",  color);
			build(str, font, mat,
					Font.Alignment.RIGHT,
					Font.Alignment.TOP,
					1.2f, 1.2f);
		}

		@Override
		public void anim(float dt) {
			float posX = getPositionX() + animSpeed*dt;
			if(posX > targetPosX) {
				posX = targetPosX;
				isFinishAnim = true;
			}
			setPosition(posX, getPositionY());
		}

		@Override
		public boolean isFinished() {
			return isFinishAnim;
		}

		@Override
		public void setFinished(boolean isFinished) {
			isFinishAnim = isFinished;
		}
	}
	
	class CreditsRightText extends Text implements IAnimable {
		private float animSpeed = -animSpeeed;
		private float targetPosX;
		private float targetPosY;
		private boolean isFinishAnim = false;
		
		public void setTargetPos(float x, float y) {
			targetPosX = x;
			targetPosY = y;
		}
		
		public void build(XeAssets assets, String str) {
			Font font = (Font)assets.get(R.raw.badaboom_font, Font.class);
			Material mat = (Material)assets.get(R.raw.badaboom_material, Material.class);
			float[] color = { 1f, 1f, 1f, 1f};
			mat.setPropertyVec("uColor",  color);
			build(str, font, mat,
					Font.Alignment.LEFT,
					Font.Alignment.TOP,
					1.5f, 1.5f);
		}

		@Override
		public void anim(float dt) {
			float posX = getPositionX() + animSpeed*dt;
			if(posX < targetPosX) {
				posX = targetPosX;
				isFinishAnim = true;
			}
			Log.d("pos", "pos: " + posX + " " + targetPosX + " " + getPositionY());
			setPosition(posX, getPositionY());
		}

		@Override
		public boolean isFinished() {
			return isFinishAnim;
		}

		@Override
		public void setFinished(boolean isFinished) {
			isFinishAnim = isFinished;
		}
	}
	
	interface IAnimable {
		public void anim(float dt);
		public boolean isFinished();
		public void setFinished(boolean isFinished);
	}
}
