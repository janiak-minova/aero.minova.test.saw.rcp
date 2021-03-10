package aero.minova.test.saw.rcp.vCard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import aero.minova.test.saw.rcp.constants.EventConstants;
import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.Group;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.ValidationWarnings;
import ezvcard.property.RawProperty;

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
		if (c.getValueString(VCardOptions.NAME).length() > 0)
			writeVCard(vCardString, c.getValueString(VCardOptions.NAME));
		else
			writeVCard(vCardString, "Neuer Kontakt");
	}

	public static void exportVCard(Group g) {
		String vCardString = getVCardString(g.getMembers());
		writeVCard(vCardString, g.getName());
	}

	public static void writeVCard(String vCardString, String filename) {

		FileDialog dialog = new FileDialog(new Shell(), SWT.SAVE);
		dialog.setFileName(filename + ".vcf");
		String open = dialog.open();

		if (open != null) {
			try {
				File file = new File(open);
				file.createNewFile();
				FileWriter myWriter = new FileWriter(open);
				myWriter.write(vCardString);
				myWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String getVCardString(List<Contact> contacts) {
		String vCardString = "";
		for (Contact c : contacts) {
			vCardString += getVCardString(c);
		}
		return vCardString;
	}

	public static String getVCardString(Contact c) {

		VCard vcard = new VCard();

		for (String property : c.getProperties()) {
			for (String type : c.getTypesAndValues(property).keySet()) {
				RawProperty raw = vcard.addExtendedProperty(property, c.getTypesAndValues(property).get(type).getVCardString());
				if (!type.equals("")) {
					raw.setParameter("TYPE", type);
				}

				if (property.equals(VCardOptions.PHOTO))
					raw.addParameter("Encoding", "b");
			}
		}

		// Validate
		String vCardString = vcard.write();
		vcard = Ezvcard.parse(vCardString).first();
		ValidationWarnings warnings = vcard.validate(VCardVersion.V4_0);
		System.out.println(warnings.toString());

		return vCardString;
	}

}
