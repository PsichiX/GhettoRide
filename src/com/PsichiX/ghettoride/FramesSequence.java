package com.PsichiX.ghettoride;

import java.util.ArrayList;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.XeUtils.*;

public class FramesSequence
{
	private ArrayList<Frame> _frames = new ArrayList<Frame>();
	private float _delay = 1.0f;
	
	public void apply(Sprite spr, float time)
	{
		int frame = (int)(time / _delay) % _frames.size();
		_frames.get(frame)._subImage.apply(spr);
	}
	
	public static class Frame
	{
		protected SpriteSheet.SubImage _subImage;
		protected float _xOffset = 0.0f;
		protected float _yOffset = 0.0f;
		
		public Frame(SpriteSheet.SubImage sub, float xoff, float yoff)
		{
			_subImage = sub;
			_xOffset = xoff;
			_yOffset = yoff;
		}
	}
	
	public static class Animator
	{
		private FramesSequence _owner;
		private Sprite _target;
		private float _time = 0.0f;
		
		public Animator(FramesSequence owner, Sprite target)
		{
			_owner = owner;
			_target = target;
		}
		
		public void setOwner(FramesSequence owner)
		{
			_owner = owner;
		}
		
		public FramesSequence getOwner()
		{
			return _owner;
		}
		
		public void setTarget(Sprite spr)
		{
			_target = spr;
		}
		
		public Sprite getTarget()
		{
			return _target;
		}
		
		public void setTime(float val)
		{
			_time = val;
		}
		
		public float getTime()
		{
			return _time;
		}
		
		public void update(float dt, float speed)
		{
			_time += dt * speed;
			_owner.apply(_target, _time);
		}
	}
}
