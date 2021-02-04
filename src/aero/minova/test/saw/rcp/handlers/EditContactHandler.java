 
package aero.minova.test.saw.rcp.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

public class EditContactHandler {
	@Execute
	public void execute() {
		System.out.println("Hallo");
	}
	
	
	@CanExecute
	public boolean canExecute() {
		System.out.println("canExecute Hallo");
		return true;
	}
		
}