
package aero.minova.test.saw.rcp.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

public class ContactPart implements GroupListViewer {
	private Label headLabel;
	private Text headText;
	private Label companyLabel;
	private Text companyText;
	private Composite groupList;
	private Composite contactList;
	private Composite contactDetail;
	private SashForm sashForm;

	@Inject
	public ContactPart() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		sashForm = new SashForm(parent, SWT.HORIZONTAL);
		sashForm.addControlListener(new ContactRezieListener(sashForm));
		sashForm.setSashWidth(1);

		groupList = new Composite(sashForm, SWT.None);
		contactList = new Composite(sashForm, SWT.None);
		contactDetail = new Composite(sashForm, SWT.None);

		createGroupList(groupList);
		createContactListDetail(contactList);
		createContactDetail(contactDetail);
	}

	private void createGroupList(Composite groupList) {
		// TODO Auto-generated method stub
		groupList.setVisible(false);
	}

	private void createContactListDetail(Composite contactList) {
		// TODO Auto-generated method stub

	}

	private void createContactDetail(Composite body) {
		GridData gd;

		// Layout für Body definieren
		body.setLayout(new GridLayout(2, false));

		// Kopf Label und Feld definieren
		headLabel = new Label(body, SWT.RIGHT);
		headLabel.setText("Matchcode");
		gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		headLabel.setLayoutData(gd);
//		headText = new Text(body, SWT.BORDER | SWT.ICON_SEARCH | SWT.ICON_CANCEL | SWT.SEARCH);
		headText = new Text(body, SWT.NONE);
		headText.setText("Wilfried Saak");
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		headText.setLayoutData(gd);

		// Separator
		Label separatorLabel = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 500;
		separatorLabel.setLayoutData(gd);
		separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");

		// Weiteres Feld definieren
		companyLabel = new Label(body, SWT.RIGHT);
		companyLabel.setText("Firma");
		gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		companyLabel.setLayoutData(gd);
		companyText = new Text(body, SWT.NONE);
		companyText.setText("MINOVA Information Services GmbH");
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.widthHint = 300;
		companyText.setLayoutData(gd);

		// Separator
		separatorLabel = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 500;
		separatorLabel.setLayoutData(gd);
		separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");

		//
		Combo w = new Combo(body, SWT.READ_ONLY | SWT.FLAT | SWT.SIMPLE);
		gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		w.setLayoutData(gd);
		w.setText("Privat ▼");
		w.setItems("Privat", "Arbeit", "Zentrale", "Eigene...");
		companyText = new Text(body, SWT.NONE);
		companyText.setText("MINOVA Information Services GmbH");
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.widthHint = 300;
		companyText.setLayoutData(gd);

		// Separator
		separatorLabel = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 500;
		separatorLabel.setLayoutData(gd);
		separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");

		Link link = new Link(body, SWT.FLAT);
		gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		link.setLayoutData(gd);
		link.setText("Privat ▼");
		// w.setItems("Privat", "Arbeit", "Zentrale", "Eigene...");
		companyText = new Text(body, SWT.NONE);
		companyText.setText("MINOVA Information Services GmbH");
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.widthHint = 300;
		companyText.setLayoutData(gd);
	}

	@Override
	public void setGroupListVisible(boolean visible) {
		groupList.setVisible(visible);
		groupList.getParent().requestLayout();
	}
}