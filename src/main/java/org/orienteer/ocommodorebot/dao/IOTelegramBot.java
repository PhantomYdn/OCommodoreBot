package org.orienteer.ocommodorebot.dao;

import org.orienteer.core.dao.DAOField;
import org.orienteer.core.dao.DAOOClass;
import org.orienteer.core.dao.ODocumentWrapperProvider;

import com.google.inject.ProvidedBy;

@ProvidedBy(ODocumentWrapperProvider.class)
@DAOOClass("OTelegramBot")
public interface IOTelegramBot {

	@DAOField("name")
	public String getName();
	
	@DAOField("token")
	public String getToken();
	
	@DAOField(value = "enabled", defaultValue = "true")
	public boolean isEnabled();
}
