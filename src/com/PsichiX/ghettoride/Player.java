package com.PsichiX.ghettoride;

import android.util.Log;

import com.PsichiX.XenonCoreDroid.Framework.Actors.ActorSprite;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Material;
import com.PsichiX.XenonCoreDroid.XeSense.EventData;

public class Player extends ActorSprite {

	private float _movX = 0.0f;
	private float _movY = 0.0f;
	private float _spdX = 100.0f;
	private float _spdY = 100.0f;
	
	public Player(Material mat) {
		super(mat);
	}
	
	@Override
	public void onSensor(EventData ev) {
		//if(ev.typ
		Log.d("event", "play: " + ev.values[0] + " " + ev.values[1] + " " + ev.values[2]);
	}
	
	@Override
	public void onUpdate(float dt)
	{
		/*Camera2D cam = (Camera2D)getScene().getCamera();
		float w = cam.getViewWidth() * 0.5f;
		float h = cam.getViewHeight() * 0.2f;
		setPosition(
			Math.max(-w, Math.min(w, _x + _movX * _spdX * dt)),
			Math.max(-h, Math.min(h, _y + _movY * _spdY * dt))
			);*/
	}

}
