package org.orienteer.ocommodorebot;

import org.orienteer.ocommodorebot.dao.IOTelegramBot;

public interface IBotHandlerFactory {
	OBotHandler create(IOTelegramBot bot);
}
