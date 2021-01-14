package aero.minova.test.saw.rcp.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class StatusBar {
	private Label label;
	
	@Inject Logger logger;
	
	@Inject
	private IEventBroker eventBroker;
	public static final String STATUSBAR = "statusbar";

	@Inject
	@Optional
	public void getEvent(@UIEventTopic(STATUSBAR) String message) {
		updateInterface(message);
	}

	@PostConstruct
	public void createGui(Composite parent) {
		label = new Label(parent, SWT.LEFT);
		parent.setLayout(new GridLayout(1, true));
		label.setBackground(parent.getBackground());
		label.setText("Dies ist meine               dfsgsdfgdsgsdgsdfgdsgfds                                Meldung");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));

	}
	
	public void updateInterface(String message) {
	    try {
			Display.getDefault().asyncExec(new Runnable() {
	            @Override
	            public void run() {
	                try {
						label.setText(message);
						label.requestLayout();
						label.getParent().requestLayout();
						label.pack(true);
						label.getParent().pack(true);
						logger.info(message);
	                } catch (Exception e) {
	                    logger.error(e.fillInStackTrace());
	                }
	            }
	        });
	    } catch (Exception exception) {
	        logger.error(exception.fillInStackTrace());
	    }
	}
}