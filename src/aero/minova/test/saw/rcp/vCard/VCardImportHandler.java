package aero.minova.test.saw.rcp.vCard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
import aero.minova.test.saw.rcp.model.Value;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.VCardProperty;

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
			createContactsFromString(content);
		}
	}

	public static List<Contact> createContactsFromString(String contactString) {

		List<VCard> vCardList = readVCard(contactString);
		List<Contact> contacts = new ArrayList<Contact>();
		for (VCard vCard : vCardList) {
			Contact c = createContact(vCard);
			contacts.add(c);
		}

		broker.send(EventConstants.SELECT_CONTACTS, contacts);
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

	public static Contact createContact(VCard vcard) {
		Contact c;
		if (vcard.getStructuredName() != null && db.getContactByName(VCardMapping.getValue(vcard.getStructuredName())) != null) {
			c = db.getContactByName(VCardMapping.getValue(vcard.getStructuredName()));
			broker.send(EventConstants.CONTACT_EXISTS, c);
		} else {
			c = db.addContact();
		}

		for (VCardProperty prop : vcard.getProperties()) {
			String propString = VCardMapping.getPropertyString(prop);
			if (Arrays.asList(VCardOptions.PROPERTIES).contains(propString)) {
				String type = prop.getParameter("TYPE");
				Value value = VCardMapping.getValue(prop);
				if (VCardOptions.TYPES.get(propString) != null && Arrays.asList(VCardOptions.TYPES.get(propString)).contains(type)) {
					c.setProperty(propString, type, value);
				} else {
					c.setProperty(propString, value);
				}
			}
		}

		return c;
	}
}
