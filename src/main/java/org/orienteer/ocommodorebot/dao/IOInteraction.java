package org.orienteer.ocommodorebot.dao;

import java.util.Date;

import org.orienteer.core.dao.DAOField;
import org.orienteer.core.dao.DAOOClass;
import org.orienteer.core.dao.IODocumentWrapper;
import org.orienteer.core.dao.ODocumentWrapperProvider;

import com.google.inject.ProvidedBy;

@ProvidedBy(ODocumentWrapperProvider.class)
@DAOOClass("OInteraction")
public interface IOInteraction extends IODocumentWrapper {
	
	public Date getTimestamp();
	public void setTimestamp(Date timestamp);
	
	@DAOField(value = "execution", inverse = "interactions")
	public IOExecution getExecution();
	public void setExecution(IOExecution execution);
	
	@DAOField(value = "stepExecution", inverse = "interactions")
	public IOStepExecution getStepExecution();
	public void setStepExecution(IOStepExecution stepExecution);
	
	public IOStep getStep();
	public void setStep(IOStep step);
	
	public String getText();
	public void setText(String text);
}
