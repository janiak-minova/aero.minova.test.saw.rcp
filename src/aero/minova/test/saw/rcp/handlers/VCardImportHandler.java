package aero.minova.test.saw.rcp.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import aero.minova.test.saw.rcp.model.Database;
import ezvcard.Ezvcard;
import ezvcard.VCard;

public class VCardImportHandler {

	@Inject
	IEventBroker broker;
	Database db = Database.getInstance();

	@Execute
	public void execute(MPart part) {
		FileDialog dialog = new FileDialog(new Shell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.vcf" });
		String path = dialog.open();
		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VCard vcard = readVCard(content);
		Contact c = createContact(vcard);

		broker.send(EventConstants.NEW_CONTACT, c);
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

	public VCard readVCard(String vCardString) {
		VCard vcard = Ezvcard.parse(vCardString).first();
		return vcard;
	}

	// TODO einzelne Einträge seperat behandeln?
	public Contact createContact(VCard vcard) {

		String name = "";
		String company = "";
		String homepage = "";
		String phonenumber = "";
		String notes = "";

		try {
			name = vcard.getFormattedName().getValue();
			company = vcard.getOrganization().getValues().get(0);
			homepage = vcard.getUrls().get(0).getValue();
			phonenumber = vcard.getTelephoneNumbers().get(0).getText();
			notes = vcard.getNotes().get(0).getValue();
		} catch (Exception e) {}

		return db.addContact(company, name, homepage, phonenumber, notes);
	}

}
