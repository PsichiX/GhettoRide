package com.PsichiX.ghettoride;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import java.util.ArrayList;

public class Parallax
{
	private ArrayList<Layer> _layers = new ArrayList<Layer>();
	private IScene _scene;
	private float _areaX = 0.0f;
	private float _areaY = 0.0f;
	private float _areaW = 0.0f;
	private float _areaH = 0.0f;
	
	public void setScene(IScene scn)
	{
		for(Layer lr : _layers)
		{
			IScene s = lr.getScene();
			if(s != null)
				s.detach(lr);
			scn.attach(lr);
		}
		_scene = scn;
	}
	
	public void setArea(float x, float y, float w, float h)
	{
		_areaX = x;
		_areaY = y;
		_areaW = w;
		_areaH = h;
	}
	
	public void addLayer(Layer lr)
	{
		if(!_layers.contains(lr))
		{
			_layers.add(lr);
			_scene.attach(lr);
		}
	}
	
	public void removeLayer(Layer lr)
	{
		if(_layers.contains(lr))
		{
			IScene s = lr.getScene();
			if(s != null)
				s.detach(lr);
			_layers.remove(lr);
		}
	}
	
	public void onUpdate(float dt, float factor)
	{
		for(Layer lr : _layers)
		{
			if(!lr._static)
			{
				float tx = lr.getPositionX() + (lr._spdX * dt * factor);
				float ty = lr.getPositionY() + (lr._spdY * dt * factor);
				tx = Commons.modulo(tx, _areaX, _areaX + _areaW);
				ty = Commons.modulo(ty, _areaY, _areaY + _areaH);
				lr.setPosition(tx, ty);
			}
		}
	}
	
	public static class Layer extends Sprite
	{
		protected float _spdX = 0.0f;
		protected float _spdY = 0.0f;
		protected boolean _static = false;
		
		public Layer(SpriteSheet.SubImage sheetImg, float spdX, float spdY, boolean isStatic)
		{
			super(null);
			sheetImg.apply(this);
			setOffsetFromSize(0.5f, 0.5f);
			_spdX = spdX;
			_spdY = spdY;
			_static = isStatic;
		}
	}

	public void randomizeLayers(float minX, float minY, float maxX, float maxY)
	{
		for(Layer lr : _layers)
		{
			float tx = minX + (GlobalRandom.getRandom().nextFloat() * (maxX - minX));
			float ty = minY + (GlobalRandom.getRandom().nextFloat() * (maxY - minY));
			lr.setPosition(tx, ty);
		}
	}
}
