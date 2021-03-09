package aero.minova.test.saw.rcp.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import aero.minova.test.saw.rcp.constants.EventConstants;

public class DeleteContactHandler {

	@Inject
	IEventBroker broker;

	@Execute
	public void execute(MApplication application, EModelService service) {
		broker.send(EventConstants.DELETE_CONTACT, "");
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

}
