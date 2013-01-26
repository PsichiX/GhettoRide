package com.PsichiX.ghettoride;

import com.PsichiX.XenonCoreDroid.XeActivity;
import com.PsichiX.XenonCoreDroid.XeAssets;
import com.PsichiX.XenonCoreDroid.XeUtils.*;

import java.util.HashMap;

public class Theme extends XeAssets.Asset
{
	private HashMap<String, Integer> _assets = new HashMap<String, Integer>();
	
	protected Theme(XeAssets a, String n)
	{
		super(a, n);
	}
	
	protected Theme(XeAssets a, Integer id)
	{
		super(a, id);
	}
	
	protected boolean onLoadAsset(Xml xml, Resource res)
	{
		XeActivity cntx = (XeActivity)getOwner().getContext();
		Xml.Element elm = xml.get("Theme");
		if(elm == null)
			return false;
		for(Xml.Element child : elm.getElements())
		{
			if(!child.getName().equals("Asset"))
				continue;
			String name = child.getValue("", "name");
			String id = child.getValue("", "id");
			int rid = Resource.getId(cntx, id);
			if(rid > 0)
				_assets.put(name, rid);
		}
		return true;
	}
	
	public XeAssets.Asset getAsset(String name, Class<? extends XeAssets.Asset> cls)
	{
		return getOwner().get(_assets.get(name), cls);
	}
}
