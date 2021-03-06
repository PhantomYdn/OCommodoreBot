package org.orienteer.ocommodorebot.dao;

import java.util.List;

import org.orienteer.core.dao.DAOProvider;
import org.orienteer.core.dao.Query;

import com.google.inject.ProvidedBy;
import com.orientechnologies.orient.core.record.impl.ODocument;

@ProvidedBy(DAOProvider.class)
public interface OBotDAO {

	@Query("select from OTelegramBot")
	public List<IOTelegramBot> findAllBots();
	
	@Query("select from OTelegramBot where enabled = true")
	public List<IOTelegramBot> findEnabledBots();
	
	@Query("select from OPlan where name = :name")
	public IOPlan getPlanByName(String name);
	
	@Query("select from OExecution where chatId = :chatId and finished is null")
	public IOExecution getActiveExecutionByChatId(Long chatId);
	
	@Query("select from OStepExecution where execution.chatId = :chatId and messageId = :messageId")
	public IOStepExecution getStepExecution(Long chatId, Integer messageId);
	
	@Query("select from OUser where telegram = :name")
	public ODocument getUserByTelegram(String name);
}
