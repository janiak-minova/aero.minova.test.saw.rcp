package aero.minova.test.saw.rcp.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import aero.minova.test.saw.rcp.events.EventConstants;
import aero.minova.test.saw.rcp.model.Contact;
import ezvcard.VCard;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.StructuredName;

public class VCardExportHandler {

	@Inject
	IEventBroker broker;

	@Execute
	public void execute(MPart part) {
		broker.send(EventConstants.EXPORT_VCARD, "");
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

	public static void exportVCard(Contact c) {

		String vCardString = getVCardString(c);

		FileDialog dialog = new FileDialog(new Shell(), SWT.SAVE);
		dialog.setFileName(c.getFirstName() + ".vcf");
		String open = dialog.open();
		File file = new File(open);
		try {
			file.createNewFile();
			FileWriter myWriter = new FileWriter(open);
			myWriter.write(vCardString);
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getVCardString(Contact c) {

		VCard vcard = new VCard();

		// TODO change to fit first/last name
		StructuredName sname = new StructuredName();
		if (c.getFirstName().contains(" ")) {
			sname.setFamily(c.getFirstName().split(" ")[1]);
			sname.setGiven(c.getFirstName().split(" ")[0]);
		} else {
			sname.setFamily(c.getFirstName());
		}
		vcard.setStructuredName(sname);

		vcard.setFormattedName(c.getFirstName());
		vcard.setOrganization(c.getCompany());
		vcard.addUrl(c.getHomepage());
		vcard.addTelephoneNumber(c.getPhonenumber(), TelephoneType.HOME);
		vcard.addNote(c.getNotes());

		vcard.addExtendedProperty("X-CONTACTID", c.getId() + "");

		return vcard.write();
	}

}
