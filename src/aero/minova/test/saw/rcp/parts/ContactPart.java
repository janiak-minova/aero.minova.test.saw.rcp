
package aero.minova.test.saw.rcp.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.Control;

import aero.minova.test.saw.rcp.events.EditEventConstants;

public class ContactPart implements GroupListViewer {
	private Label headLabel;
	private Text headText;
	private Label companyLabel;
	private Text companyText;
	private Combo phoneCombo;
	private Text phoneText;
	private Composite groupList;
	private Composite contactList;
	private Composite contactDetail;
	private SashForm sashForm;
	private Text notesText;
	private Label notesLabel;
	private Text hpText;
	private Label hpLabel;
	
	private HashMap<Text, Control> inputs;
	
	@Inject
	private EModelService m_ModelService;
	@Inject
	private MWindow m_Window;
	@Inject
	private MApplication app;

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
		inputs = new HashMap<Text, Control>();

		// Layout für Body definieren
		body.setLayout(new GridLayout(2, false));

		// Kopf Label und Feld definieren
		headLabel = new Label(body, SWT.RIGHT);
		headLabel.setText("Name");
//		gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
//		headLabel.setLayoutData(gd);
		headText = new Text(body, SWT.NONE);
		headText.setText("Wilfried Saak");
		headText.setEditable(false);
		headText.setMessage("Vorname Nachname");
//		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
//		headText.setLayoutData(gd);
		inputs.put(headText, headLabel);

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
		companyText.setEditable(false);
		companyText.setMessage("Konzern");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 300;
		companyText.setLayoutData(gd);
		inputs.put(companyText, companyLabel);

		// Separator
		separatorLabel = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 500;
		separatorLabel.setLayoutData(gd);
		separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");

//		//
		phoneCombo = new Combo(body, SWT.READ_ONLY | SWT.FLAT | SWT.SIMPLE);
		gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		phoneCombo.setLayoutData(gd);
		phoneCombo.setText("Privat ▼");
		phoneCombo.setItems("Privat", "Arbeit", "Zentrale", "Eigene...");
		phoneCombo.select(0);
		phoneCombo.setVisible(false);
		phoneText = new Text(body, SWT.NONE);
		phoneText.setText("");
		phoneText.setMessage("Telefon Nummer");
		phoneText.setVisible(false);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 300;
		phoneText.setLayoutData(gd);
		inputs.put(phoneText, phoneCombo);

		// Separator
		separatorLabel = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 500;
		separatorLabel.setLayoutData(gd);
		separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");
		
		hpLabel = new Label(body, SWT.FLAT);
		gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		hpLabel.setLayoutData(gd);
		hpLabel.setText("Homepage");
		hpLabel.setVisible(false);
		hpText = new Text(body, SWT.NONE);
		hpText.setText("");
		hpText.setMessage("URL");
		hpText.setVisible(false);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 300;
		hpText.setLayoutData(gd);
		inputs.put(hpText, hpLabel);
		
		// Separator
		separatorLabel = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 500;
		separatorLabel.setLayoutData(gd);
		separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");

		notesLabel = new Label(body, SWT.RIGHT | SWT.TOP);
		gd = new GridData(SWT.RIGHT, SWT.TOP, true, false);
		notesLabel.setLayoutData(gd);
		notesLabel.setText("Notizen");
		notesText = new Text(body, SWT.MULTI |  SWT.WRAP | SWT.V_SCROLL);
		notesText.setLayoutData(new GridData(GridData.FILL_BOTH));
		//inputs.put(notesText, notesLabel);
		
		drawContactDetails(inputs, true, body);

	}
	
	public void drawContactDetails(Map<Text, Control> objects, boolean edit, Composite body) {
		GridData gd;
		Label separatorLabel;
		for (Text o: objects.keySet()) {
			if (edit) {
				System.out.println(o.getMessage());
				gd = new GridData(SWT.FILL, SWT.FILL, true, false);
				o.setLayoutData(gd);
				gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
				objects.get(o).setLayoutData(gd);
				
				// Separator
				separatorLabel = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
				gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
				gd.horizontalSpan = 2; // Beide Spalten verwenden
				gd.widthHint = 500;
				separatorLabel.setLayoutData(gd);
				separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");
			}
			
		}
	}
	
	@Inject
	@Optional
	private void subscribeTopicEditAllTopics(@UIEventTopic(EditEventConstants.TOPIC_EDIT) boolean editable) {
		
		MApplication app = (MApplication)((EObject)m_Window).eContainer();
		MToolBar mainToolbar = (MToolBar)m_ModelService.find("aero.minova.test.saw.rcp.toolbar.0", app);
		
		headText.setEditable(editable);
		headText.setFocus();
		companyText.setEditable(editable);
		phoneText.setEditable(editable);
		
		if (!editable) {
			if (headText.getText().strip().equals("")) {
				headLabel.setVisible(editable);
				headText.setVisible(editable);
			} 
			if (companyText.getText().strip().equals("")) {
				companyLabel.setVisible(editable);
				companyText.setVisible(editable);
			} 
			if (phoneText.getText().strip().equals("")) {
				phoneText.setVisible(editable);
				phoneCombo.setVisible(editable);
			} 
			if (hpText.getText().strip().equals("")) {
				hpText.setVisible(editable);
				hpLabel.setVisible(editable);
			} 
		} else {
			headLabel.setVisible(editable);
			headText.setVisible(editable);
			companyLabel.setVisible(editable);
			companyText.setVisible(editable);
			phoneText.setVisible(editable);
			phoneCombo.setVisible(editable);
			hpLabel.setVisible(editable);
			hpText.setVisible(editable);
		}
	}

	@Override
	public void setGroupListVisible(boolean visible) {
		
		groupList.setVisible(visible);
		groupList.getParent().requestLayout();
	}
}