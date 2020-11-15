package org.orienteer.ocommodorebot.service;

import org.orienteer.ocommodorebot.IBotHandlerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class OCommodoreBotModule extends AbstractModule {
	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
			     .build(IBotHandlerFactory.class));
	}
}
