package aero.minova.test.saw.rcp.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import aero.minova.test.saw.rcp.constants.EventConstants;
import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.Database;

public class NewContactHandler {

	@Inject
	IEventBroker broker;

	@Execute
	public void execute(MApplication application, EModelService service) {
		Contact c = Database.getInstance().addContact();
		broker.send(EventConstants.NEW_CONTACT, c);
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

}
