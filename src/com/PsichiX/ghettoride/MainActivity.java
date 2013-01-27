package com.PsichiX.ghettoride;

import com.PsichiX.XenonCoreDroid.XeActivity;
import com.PsichiX.XenonCoreDroid.XeApplication;
import com.PsichiX.XenonCoreDroid.Framework.Utils.Utils;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Graphics;
import com.PsichiX.ghettoride.Utils.SheredprefUtils;
import com.PsichiX.ghettoride.common.LevelInfo;
import com.PsichiX.ghettoride.mainmenu.MainMenuState;

public class MainActivity extends XeActivity
{
	public static XeApplication app;
	public static LevelInfo levels;
	
	@Override
	public void onCreate(android.os.Bundle savedInstanceState) 
	{
		// setup application before running it
		XeApplication.SETUP_SOUND_STREAMS = 16;
		XeApplication.SETUP_WINDOW_HAS_TITLE = false;
		XeApplication.SETUP_WINDOW_FULLSCREEN = true;
		XeApplication.SETUP_SCREEN_ORIENTATION = XeApplication.Orientation.LANDSCAPE;
		
		// create application
		super.onCreate(savedInstanceState);
		app = getApplicationCore();
		levels = new LevelInfo();
		levels.getLevels().add(new LevelInfo.Level(1, "Level 1 - Port", R.raw.default_theme, 2000.0f));
		
		Utils.initModule(getApplicationCore().getAssets());
		Graphics.initModule(getApplicationCore().getAssets(), getApplicationCore().getPhoton());
		GlobalRandom.init();
		SheredprefUtils.getInstance().Init(this);
		
		getApplicationCore().getAssets().registerClass(Theme.class);
		getApplicationCore().getTimer().setFixedStep(1000 / 25);
		getApplicationCore().getPhoton().getRenderer().getTimer().setFixedStep(1000 / 30);
		getApplicationCore().getPhoton().getRenderer().setClearBackground(true, 1.0f, 1.0f, 1.0f, 1.0f);
		getApplicationCore().getPhoton().clearDrawCalls();
		//getApplicationCore().run(new GameState(R.raw.default_theme, 1.0f));
		getApplicationCore().run(new MainMenuState(R.raw.default_theme));
	}
}
