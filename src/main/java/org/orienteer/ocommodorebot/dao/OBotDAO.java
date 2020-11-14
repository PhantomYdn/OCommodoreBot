package org.orienteer.ocommodorebot.dao;

import java.util.List;

import org.orienteer.core.dao.DAOProvider;
import org.orienteer.core.dao.Query;

import com.google.inject.ProvidedBy;

@ProvidedBy(DAOProvider.class)
public interface OBotDAO {

	@Query("select from OTelegramBot")
	public List<IOTelegramBot> findAllBots();
	
	@Query("select from OTelegramBot where enabled = true")
	public List<IOTelegramBot> findEnabledBots();
}
