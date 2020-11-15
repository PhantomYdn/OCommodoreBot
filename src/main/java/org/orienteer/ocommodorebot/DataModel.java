package org.orienteer.ocommodorebot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.orienteer.core.OrienteerWebApplication;
import org.orienteer.core.dao.DAO;
import org.orienteer.core.module.AbstractOrienteerModule;
import org.orienteer.core.util.OSchemaHelper;
import org.orienteer.ocommodorebot.dao.IOExecution;
import org.orienteer.ocommodorebot.dao.IOInteraction;
import org.orienteer.ocommodorebot.dao.IOPlan;
import org.orienteer.ocommodorebot.dao.IOStep;
import org.orienteer.ocommodorebot.dao.IOStepExecution;
import org.orienteer.ocommodorebot.dao.IOTelegramBot;
import org.orienteer.ocommodorebot.dao.OBotDAO;

import com.google.inject.Inject;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

public class DataModel extends AbstractOrienteerModule{
	
	@Inject
	private OBotDAO botDAO;
	
	@Inject
	private IBotHandlerFactory botHandlerFactory;
	
	private static List<OBotHandler> botThreads = new ArrayList<OBotHandler>();

	protected DataModel() {
		super("ocommodorebot", 5);
	}
	
	@Override
	public ODocument onInstall(OrienteerWebApplication app, ODatabaseSession db) {
		super.onInstall(app, db);
		OSchemaHelper helper = OSchemaHelper.bind(db);
		DAO.describe(helper, IOTelegramBot.class,
							 IOStep.class,
							 IOPlan.class,
							 IOExecution.class,
							 IOStepExecution.class,
							 IOInteraction.class);
		helper.oClass("OUser")
				.oProperty("telegram", OType.STRING, 30);
		return null;
	}
	
	@Override
	public void onUpdate(OrienteerWebApplication app, ODatabaseSession db, int oldVersion, int newVersion) {
		onInstall(app, db);
	}
	
	@Override
	public void onInitialize(OrienteerWebApplication app, ODatabaseSession db) {
		List<IOTelegramBot> botConfigs = botDAO.findEnabledBots();
		
		for (IOTelegramBot ioTelegramBot : botConfigs) {
			OBotHandler thread = botHandlerFactory.create(ioTelegramBot);
			thread.startBot();
			botThreads.add(thread);
		}
	}
	
	@Override
	public void onDestroy(OrienteerWebApplication app, ODatabaseSession db) {
		Iterator<OBotHandler> it = botThreads.iterator();
		while(it.hasNext()) {
			it.next().stopBot();
			it.remove();
		}
	}
	
}
