package aero.minova.test.saw.rcp.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ContactDetailEntry {

	private Label name;
	private Text input;
	private Label seperator;

	public ContactDetailEntry(Composite body, String nameString, String textMessage) {

		// Feldname Label
		name = new Label(body, SWT.RIGHT);
		name.setText(nameString);
		GridData gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		gd.widthHint = 50;
		name.setLayoutData(gd);

		// Input
		input = new Text(body, SWT.NONE);
		input.setMessage(textMessage);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		input.setLayoutData(gd);

		// Separator
		seperator = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 1000;
		seperator.setLayoutData(gd);
		seperator.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");
	}

	public Label getName() {
		return name;
	}

	public Text getInput() {
		return input;
	}

	public Label getSeperator() {
		return seperator;
	}

	// TODO: Logik in Contact bewegen?
	public void setInput(Contact c) {
		String content = "";
		switch (name.getText()) {
		case "Name":
			content = c.getFirstName();
			break;
		case "Firma":
			content = c.getCompany();
			break;
		case "Privat":
			content = c.getPhonenumber();
			break;
		case "Homepage":
			content = c.getHomepage();
			break;
		case "E-Mail":
			content = c.getMail();
			break;
		default:
			System.err.println("Input not defined for Label " + name.getText());
		}

		input.setText(content);
	}

	// TODO: Logik in Contact bewegen?
	public void updateContact(Contact c) {
		switch (name.getText()) {
		case "Name":
			c.setFirstName(input.getText());
			break;
		case "Firma":
			c.setCompany(input.getText());
			break;
		case "Privat":
			c.setPhonenumber(input.getText());
			break;
		case "Homepage":
			c.setHomepage(input.getText());
			break;
		case "E-Mail":
			c.setMail(input.getText());
			break;
		default:
			System.err.println("Input not defined for Label " + name.getText());
		}

	}

}
