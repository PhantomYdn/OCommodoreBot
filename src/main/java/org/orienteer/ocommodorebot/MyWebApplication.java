package org.orienteer.ocommodorebot;

import org.orienteer.core.OrienteerWebApplication;
import org.orienteer.core.module.PerspectivesModule;

public class MyWebApplication extends OrienteerWebApplication
{
	@Override
	public void init()
	{
		super.init();
		mountPackage("org.orienteer.ocommodorebot.web");
		registerModule(DataModel.class);
	}
	
}
