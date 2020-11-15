package org.orienteer.ocommodorebot;

import java.util.List;

import org.apache.wicket.ThreadContext;
import org.orienteer.ocommodorebot.dao.IOTelegramBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import ru.ydn.wicket.wicketorientdb.utils.DBClosure;

@Slf4j
@ExtensionMethod({BotUtils.class})
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
			boolean hadContext = ThreadContext.exists();
			if(!hadContext) ThreadContext.setApplication(OCommodoreBotWebApplication.lookupApplication());
			try {
				DBClosure.sudo(db -> {
					updates.forEach(u -> {
						log.info("Update: "+u);
						try {
							processUpdate(u);
						} catch (Exception e) {
							log.error("Problem during processing", e);
						}
					});
					return null;
				});
			} finally {
				if(!hadContext) ThreadContext.detach();
			}
		}
		return UpdatesListener.CONFIRMED_UPDATES_ALL;
	}
	
	private void processUpdate(Update u) throws Exception {
		Message newMessage = u.message();
		if(newMessage!=null) {
			telegramBot.execute(new SendMessage(newMessage.chat().id(), newMessage.text()));
		}
		String botCommand = newMessage.getBotCommand();
		if(botCommand!=null && botCommand.startsWith("/start")) {
			String[] args = newMessage.text().split("\\s");
			if(args.length<2) telegramBot.execute(new SendMessage(newMessage.chat().id(), "Please define plan name"));
			else telegramBot.execute(new SendMessage(newMessage.chat().id(), "Starting plan "+args[1]));
		}
	}
}
