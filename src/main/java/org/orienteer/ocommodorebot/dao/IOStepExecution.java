package org.orienteer.ocommodorebot.dao;

import java.util.Date;
import java.util.List;

import org.orienteer.core.component.visualizer.UIVisualizersRegistry;
import org.orienteer.core.dao.DAOField;
import org.orienteer.core.dao.DAOOClass;
import org.orienteer.core.dao.IODocumentWrapper;
import org.orienteer.core.dao.ODocumentWrapperProvider;

import com.google.inject.ProvidedBy;
import com.orientechnologies.orient.core.record.impl.ODocument;

@ProvidedBy(ODocumentWrapperProvider.class)
@DAOOClass("OStepExecution")
public interface IOStepExecution extends IODocumentWrapper {
	
	@DAOField(inverse = "stepExecutions")
	public IOExecution getExecution();
	public void setExecution(IOExecution execution);
	
	@DAOField(inverse = "stepExecutions")
	public IOStep getStep();
	public void setStep(IOStep step);
	
	public Date getStarted();
	public void setStarted(Date timestamp);
	
	public Date getFinished();
	public void setFinished(Date timestamp);
	
	public Integer getMessageId();
	public void setMessageId(Integer messageId);
	
	@DAOField(value = "executor", 
			  linkedClass = "OUser")
	public ODocument getExecutor();
	public void setExecutor(ODocument setExecutor);
	
	@DAOField(value = "interactions", inverse = "stepExecution", visualization = UIVisualizersRegistry.VISUALIZER_TABLE)
	public List<IOInteraction> getInteractions();
	
	public default void markAsDone(Date timestamp, ODocument user) {
		setFinished(timestamp);
		setExecutor(user);
	}
}
