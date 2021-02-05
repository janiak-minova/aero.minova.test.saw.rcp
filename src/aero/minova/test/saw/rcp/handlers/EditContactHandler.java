 
package aero.minova.test.saw.rcp.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import aero.minova.test.saw.rcp.events.EditEventConstants;

public class EditContactHandler {
	
	@Inject IEventBroker broker;
	boolean currentlyEditable = false;
	
	
	@Execute
	public void execute(MApplication application, EModelService service) {
		currentlyEditable = !currentlyEditable;
		broker.send(EditEventConstants.TOPIC_EDIT, currentlyEditable);

		System.out.println("Editable: " + currentlyEditable );
	}
	
	@CanExecute
	public boolean canExecute() {
		//System.out.println("canExecute Hallo");
		return true;
	}
	
}