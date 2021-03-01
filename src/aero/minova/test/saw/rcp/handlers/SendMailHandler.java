package aero.minova.test.saw.rcp.handlers;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import aero.minova.test.saw.rcp.events.EventConstants;
import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.Group;

public class SendMailHandler {

	@Inject
	IEventBroker broker;

	@Execute
	public void execute(MPart part) {
		broker.send(EventConstants.SEND_MAIL, "");
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

	public static void sendMail(Group g) {
		Desktop desktop;
		if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {

			String recipients = ",";
			for (Contact c : g.getMembers()) {
				if (!c.getMail().equals("")) {
					recipients += c.getMail() + ",";
				}
			}

			URI mailto;
			try {
				mailto = new URI("mailto:" + recipients);
				desktop.mail(mailto);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("desktop doesn't support mailto");
		}
	}

}
