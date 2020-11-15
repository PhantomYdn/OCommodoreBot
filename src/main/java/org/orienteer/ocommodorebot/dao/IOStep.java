package org.orienteer.ocommodorebot.dao;

import org.orienteer.core.component.visualizer.UIVisualizersRegistry;
import org.orienteer.core.dao.DAOField;
import org.orienteer.core.dao.DAOOClass;
import org.orienteer.core.dao.IODocumentWrapper;
import org.orienteer.core.dao.ODocumentWrapperProvider;

import com.google.inject.ProvidedBy;
import com.orientechnologies.orient.core.record.impl.ODocument;

@ProvidedBy(ODocumentWrapperProvider.class)
@DAOOClass(value = "OStep", superClasses = "V", nameProperty = "title")
public interface IOStep extends IODocumentWrapper{

	@DAOField("title")
	public String getTitle();
	
	@DAOField("duration")
	public Integer getDuration();
	
	@DAOField(value = "responsible", 
			  linkedClass = "OUser", 
			  visualization = UIVisualizersRegistry.VISUALIZER_LISTBOX)
	public ODocument getResponsible();
	
	public default String getResponsibleTelegram() {
		ODocument user = getResponsible();
		return user!=null?user.field("telegram"):null;
	}
}
