package aero.minova.test.saw.rcp.entries;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.TextValue;
import aero.minova.test.saw.rcp.vCard.VCardOptions;

public class NotesPropertyEntry extends PropertyEntry {

	private Contact currentContact;

	private Label label;
	private Text input;

	public NotesPropertyEntry(Composite body) {
		new Label(body, SWT.NONE); // Leeres Label um Platz zu lassen
		label = new Label(body, SWT.RIGHT | SWT.TOP);
		GridData gd = new GridData(SWT.RIGHT, SWT.TOP, true, false);
		label.setLayoutData(gd);
		label.setText(VCardOptions.NOTE);

		input = new Text(body, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		input.setLayoutData(new GridData(GridData.FILL_BOTH));

		input.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateContact();
			}
		});
	}

	@Override
	public void setInput(Contact c) {
		currentContact = c;
		if (c.getValue(VCardOptions.NOTE) != null)
			input.setText(c.getValue(VCardOptions.NOTE).getStringRepresentation());
		else
			input.setText("");
	}

	@Override
	public void setEditable(boolean editable) {}

	@Override
	public TypeEntry getTypeEntryByType(String string) {
		return null;
	}

	@Override
	public void updateContact() {
		currentContact.setProperty(VCardOptions.NOTE, new TextValue(input.getText()));
	}

}
