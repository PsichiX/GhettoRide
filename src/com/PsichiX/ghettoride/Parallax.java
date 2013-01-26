package com.PsichiX.ghettoride;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.XeUtils.*;
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
	
	public void onUpdate(float dt)
	{
		for(Layer lr : _layers)
		{
			float tx = lr.getPositionX() + (lr._spdX * dt);
			float ty = lr.getPositionY() + (lr._spdY * dt);
			tx = TempCommon.modulo(tx, _areaX, _areaX + _areaW);
			ty = TempCommon.modulo(ty, _areaY, _areaY + _areaH);
			lr.setPosition(tx, ty);
		}
	}
	
	public class Layer extends Sprite
	{
		protected float _spdX = 0.0f;
		protected float _spdY = 0.0f;
		
		public Layer(SpriteSheet.SubImage sheetImg, float spdX, float spdY)
		{
			super(null);
			sheetImg.apply(this);
			_spdX = spdX;
			_spdY = spdY;
		}
	}
}
