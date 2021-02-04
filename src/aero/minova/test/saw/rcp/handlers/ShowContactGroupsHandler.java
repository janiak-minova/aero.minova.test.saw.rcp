package aero.minova.test.saw.rcp.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.swt.widgets.ToolItem;

import aero.minova.test.saw.rcp.parts.GroupListViewer;

public class ShowContactGroupsHandler {

	@Execute
	public void execute(MPart part) {
		boolean selection = false;
		
		for (MToolBarElement item : part.getToolbar().getChildren()) {
			if ("aero.minova.test.saw.rcp.handledtoolitem.gruppen".equals(item.getElementId())) {
				if (item.getWidget() instanceof ToolItem) {
					selection = ((ToolItem) item.getWidget()).getSelection();
				}
			}
		}
		
		if (part.getObject() instanceof GroupListViewer) ((GroupListViewer) part.getObject()).setGroupListVisible(selection);
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}
}