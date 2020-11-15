package org.orienteer.ocommodorebot.dao;

import java.util.Date;
import java.util.List;

import org.orienteer.core.component.visualizer.UIVisualizersRegistry;
import org.orienteer.core.dao.DAOField;
import org.orienteer.core.dao.DAOOClass;
import org.orienteer.core.dao.IODocumentWrapper;
import org.orienteer.core.dao.ODocumentWrapperProvider;

import com.google.inject.ProvidedBy;

@ProvidedBy(ODocumentWrapperProvider.class)
@DAOOClass("OExecution")
public interface IOExecution extends IODocumentWrapper {
	
	@DAOField("plan")
	public IOPlan getPlan();
	public void setPlan(IOPlan plan);
	
	@DAOField(value = "started", defaultValue = "sysdate()")
	public Date getStarted();
	public IOExecution setStarted(Date date);
	
	@DAOField(value = "finished")
	public Date getFinished();
	public IOExecution setFinished(Date date);
	
	@DAOField(value = "interactions", inverse = "execution", visualization = UIVisualizersRegistry.VISUALIZER_TABLE)
	public List<IOInteraction> getInteractions();
	
	public default void markFinished() {
		setFinished(new Date());
	}
	
	public default boolean wasFinished() {
		return getFinished()!=null;
	}
}
