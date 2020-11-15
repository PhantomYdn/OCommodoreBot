package org.orienteer.ocommodorebot;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ThreadContext;
import org.orienteer.core.dao.DAO;
import org.orienteer.ocommodorebot.dao.IOExecution;
import org.orienteer.ocommodorebot.dao.IOInteraction;
import org.orienteer.ocommodorebot.dao.IOPlan;
import org.orienteer.ocommodorebot.dao.IOTelegramBot;
import org.orienteer.ocommodorebot.dao.OBotDAO;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
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
	
	@Inject
	private Provider<OBotDAO> botDAOProvider;
	
	private IOExecution execution;
	
	@AssistedInject
	public OBotHandler(@Assisted IOTelegramBot bot) {
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
					if(execution!=null) execution.reload();
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
	
	protected synchronized void processUpdate(Update u) throws Exception {
		Message newMessage = u.message();
		if(newMessage!=null) {
			send(newMessage, newMessage.text());
			if(execution!=null) {
				IOInteraction interaction = DAO.create(IOInteraction.class);
				interaction.setExecution(execution);
				interaction.setText(newMessage.text());
				if(newMessage.date()!=null) interaction.setTimestamp(new Date((long)1000 * newMessage.date())); 
				interaction.save();
			}
		}
		String botCommand = newMessage.getBotCommand();
		if(botCommand!=null) {
			String[] args = newMessage.text().split("\\s");
			if(botCommand.startsWith("/start"))  startPlan(newMessage, botCommand, args);
			else if(botCommand.startsWith("/stop"))  stopPlan(newMessage, botCommand, args);
		}
	}
	
	protected void startPlan(Message newMessage, String botCommand, String[] args) {
		if(execution!=null) send(newMessage, "There is already plan in progress. Please finish it first"); 
		else if(args.length<2) send(newMessage, "Please define plan name");
		else {
			String planName = args[1];
			IOPlan plan = botDAOProvider.get().getPlanByName(planName);
			if(plan!=null) {
				send(newMessage, "Starting plan "+args[1]);
				execution = DAO.create(IOExecution.class);
				execution.setPlan(plan);
				execution.save();
			} else {
				send(newMessage, "Plan "+planName+" was not found");
			}
		}
	}
	
	protected void stopPlan(Message newMessage, String botCommand, String[] args) {
		if(execution==null) send(newMessage, "There are no executions in progress");
		else {
			send(newMessage, "Plan "+execution.getPlan().getName()+" has been terminated");
			execution.markFinished();
			execution.save();
			execution = null;
		}
	}
	
	private void send(Message originalMessage, String text) {
		send(originalMessage.chat().id(), text);
	}
	private void send(Object chatId, String text) {
		telegramBot.execute(new SendMessage(chatId, text));
	}
}
