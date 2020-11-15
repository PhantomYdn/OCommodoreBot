package org.orienteer.ocommodorebot.dao;

import org.orienteer.core.dao.DAOField;
import org.orienteer.core.dao.DAOOClass;
import org.orienteer.core.dao.IODocumentWrapper;
import org.orienteer.core.dao.ODocumentWrapperProvider;

import com.google.inject.ProvidedBy;

@ProvidedBy(ODocumentWrapperProvider.class)
@DAOOClass(value = "OPlan", nameProperty = "name")
public interface IOPlan extends IODocumentWrapper{

	@DAOField("name")
	public String getName();
	
	@DAOField(value = "startStep", visualization = "listbox")
	public IOStep getStartStep();
	
	@DAOField("planVersion")
	public Integer getPlanVersion();
}
