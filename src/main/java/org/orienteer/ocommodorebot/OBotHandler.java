package org.orienteer.ocommodorebot;

import java.util.List;

import org.orienteer.ocommodorebot.dao.IOTelegramBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OBotHandler implements UpdatesListener {
	
	private IOTelegramBot bot;
	private TelegramBot telegramBot;
	
	public OBotHandler(IOTelegramBot bot) {
		this.bot = bot;
		this.telegramBot = new TelegramBot(bot.getToken());
	}
	
	public void startBot() {
		telegramBot.setUpdatesListener(this, new GetUpdates());
		log.info("Bot "+bot.getName()+" has been started");
	}
	
	public void stopBot() {
		telegramBot.removeGetUpdatesListener(); 
		log.info("Bot "+bot.getName()+" has been stopped");
	}

	@Override
	public int process(List<Update> updates) {
		if(updates!=null && updates.size()>0) {
			updates.forEach(u -> {
				log.info("Update: "+u);
				Message newMessage = u.message();
				if(newMessage!=null) {
					telegramBot.execute(new SendMessage(newMessage.chat().id(), newMessage.text()));
				}
			});
		}
		return UpdatesListener.CONFIRMED_UPDATES_ALL;
	}
}
