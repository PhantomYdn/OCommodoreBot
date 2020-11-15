package org.orienteer.ocommodorebot;

import org.orienteer.ocommodorebot.dao.IOTelegramBot;

public interface IBotHanlerFactory {
	OBotHandler create(IOTelegramBot bot);
}
