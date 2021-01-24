
package aero.minova.test.saw.rcp.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

public class ShowContactGroupsHandler {

//	@Inject
//	private IEventBroker eventBroker;

	@Execute
	public void execute() {
//		eventBroker.send(StatusBar.STATUSBAR, "Hallo");
		System.out.println("Hallo");
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}
}