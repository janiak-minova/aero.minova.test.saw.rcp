package aero.minova.test.saw.rcp.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
	static IEventBroker broker;
	static Database db = Database.getInstance();

	@Execute
	public void execute(MPart part) {
		FileDialog dialog = new FileDialog(new Shell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.vcf" });
		String path = dialog.open();
		String content = "";

		if (path != null) {
			try {
				content = new String(Files.readAllBytes(Paths.get(path)));
			} catch (IOException e) {}
			createContactFromString(content);
		}
	}

	public static List<Contact> createContactFromString(String contactString) {

		List<VCard> vCardList = readVCard(contactString);
		List<Contact> contacts = new ArrayList<Contact>();
		for (VCard vCard : vCardList) {
			Contact c = createContact(vCard);
			contacts.add(c);
		}

		return contacts;
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

	public static List<VCard> readVCard(String vCardString) {
		List<VCard> vCardList = new ArrayList<VCard>();
		for (VCard vCard : Ezvcard.parse(vCardString).all()) {
			vCardList.add(vCard);
		}
		return vCardList;
	}

	// TODO einzelne Einträge seperat behandeln? (Falls einzelne Einträge nicht vorhanden sind)

	public static Contact createContact(VCard vcard) {

		String sid = "-100";
		try {
			sid = vcard.getExtendedProperties("X-CONTACTID").get(0).getValue();
		} catch (IndexOutOfBoundsException e) {}
		int id = Integer.parseInt(sid);
		if (db.getContactById(id) != null) {
			// TODO Duplicate anders behandeln?
			// broker.send(EventConstants.CONTACT_EXISTS, "");
			return db.getContactById(id);
		}

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

		Contact c = db.addContact(company, name, homepage, phonenumber, notes);
		broker.send(EventConstants.NEW_CONTACT, c);

		return c;
	}

}
