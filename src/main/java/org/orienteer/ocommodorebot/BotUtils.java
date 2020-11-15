package org.orienteer.ocommodorebot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BotUtils {
	
	public String getBotCommand(Message message) {
		if(message==null) return null;
		MessageEntity[] entities = message.entities();
		if(entities==null) return null;
		for (MessageEntity messageEntity : entities) {
			if(MessageEntity.Type.bot_command.equals(messageEntity.type())) {
				return message.text().substring(messageEntity.offset(), messageEntity.offset()+messageEntity.length());
			}
		}
		return null;
	}
}
