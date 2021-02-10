
package aero.minova.test.saw.rcp.parts;

import static java.lang.Math.toIntExact;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.ExtendedReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.e4.selection.E4SelectionListener;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultRowSelectionLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.tooltip.NatTableContentTooltip;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Control;

import aero.minova.test.saw.rcp.dataset.person.PersonService;
import aero.minova.test.saw.rcp.events.EventConstants;
import aero.minova.test.saw.rcp.handlers.ContactColumnPropertyAccessor;
import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.Database;
public class ContactPart implements GroupListViewer {
	
	private boolean editable = false;
	
	private Contact currentContact;
	private List<Contact> selectedContacts;
	private NatTable natTable;
	SelectionLayer selectionLayer;
	DataLayer bodyDataLayer;
	
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
	
	private LinkedHashMap<Text, Control> inputs;	

	@Inject
	private MPart part;
	@Inject
    ESelectionService service;
	@Inject 
	IStylingEngine engine;
	
	private Database db = Database.getInstance();

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

		groupList.setVisible(false);
	}

	private void createContactListDetail(Composite contactList) {
		
		contactList.setLayout(new GridLayout(2, false));
				
		IColumnPropertyAccessor<Contact> columnPropertyAccessor = new ContactColumnPropertyAccessor();
		IRowDataProvider<Contact> bodyDataProvider = new ListDataProvider<Contact>(db.getContacts(), columnPropertyAccessor);

		bodyDataLayer = new DataLayer(bodyDataProvider);
		
		selectionLayer = new SelectionLayer(bodyDataLayer);
		selectionLayer.addConfiguration(new DefaultRowSelectionLayerConfiguration());
		
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
        viewportLayer.setRegionName(GridRegion.BODY);
                
        E4SelectionListener<Contact> esl = new E4SelectionListener<Contact>(service, selectionLayer, bodyDataProvider);
        selectionLayer.addLayerListener(esl);
        
		natTable = new NatTable(contactList, viewportLayer);
				
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);

	}
	
	@Inject
    @Optional
    void handleSelection(@Named(IServiceConstants.ACTIVE_SELECTION) List<Contact> selected) {
        if (selected != null && selected.size() > 0) {
        	currentContact = selected.get(0);
        	updateContactDetail(selected.get(0));
        	selectedContacts = selected;
        }
    }
	
	private void updateContactDetail(Contact c) {
		headText.setText(c.getFirstName());
		companyText.setText(c.getCompany());
		phoneText.setText(c.getPhonenumber());
		hpText.setText(c.getHomepage());
		notesText.setText(c.getNotes());
		
		updateVisibility(editable);
	}

	private void createContactDetail(Composite body) {
		
		//Database db = Database.getInstance();
		
		GridData gd;
		inputs = new LinkedHashMap<Text, Control>();

		// Layout für Body definieren
		body.setLayout(new GridLayout(2, false));

		// Kopf Label und Feld definieren
		headLabel = new Label(body, SWT.RIGHT);
		headLabel.setText("Name");
		gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		headLabel.setLayoutData(gd);
		headText = new Text(body, SWT.NONE);
		//headText.setText("Wilfried Saak");
		headText.setEditable(false);
		headText.setMessage("Vorname Nachname");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		headText.setLayoutData(gd);
		inputs.put(headText, headLabel);

//		// Separator
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
		//companyText.setText("MINOVA Information Services GmbH");
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
		inputs.put(notesText, notesLabel);
		
		ModifyListener listener = new ModifyListener() {
		    public void modifyText(ModifyEvent e) {
		    	String newNotes = ((Text) e.widget).getText();
		    	if (currentContact != null) {
		    		currentContact.setNotes(newNotes);
		    	}
		    }
		};
		notesText.addModifyListener(listener);
		
		
		//drawContactDetails(inputs, false, body);

	}
	
	public void drawContactDetails(Map<Text, Control> objects, boolean edit, Composite body) {
		GridData gd;
		Label separatorLabel = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 500;
		separatorLabel.setLayoutData(gd);
		separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");
		
		for (Text o: objects.keySet()) {
			if (edit) {
				
				System.out.println(o.getMessage());
				gd = new GridData(SWT.FILL, SWT.FILL, true, false);
				o.setLayoutData(gd);
				gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
				objects.get(o).setLayoutData(gd);
				objects.get(o).moveAbove(separatorLabel);
				o.moveAbove(separatorLabel);
				
				// Separator
				separatorLabel = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
				gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
				gd.horizontalSpan = 2; // Beide Spalten verwenden
				gd.widthHint = 500;
				separatorLabel.setLayoutData(gd);
				separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");	
				
			} else {
				if (!o.getText().strip().equals("")) {
					System.out.println(o.getMessage());
					gd = new GridData(SWT.FILL, SWT.FILL, true, false);
					o.setLayoutData(gd);
					gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
					objects.get(o).setLayoutData(gd);
					objects.get(o).moveAbove(separatorLabel);
					o.moveAbove(separatorLabel);
					
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
		Entry<Text, Control> notes = null;	
		Iterator<Entry<Text, Control>> iterator = objects.entrySet().iterator(); 
		while (iterator.hasNext()) { notes = (Entry<Text, Control>) iterator.next(); }
				
		gd = new GridData(SWT.RIGHT, SWT.TOP, true, false);
		notes.getValue().setLayoutData(gd);
		notes.getKey().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		notes.getKey().moveBelow(separatorLabel);
		notes.getValue().moveBelow(separatorLabel);
		
	}
	
	@Inject
	@Optional
	private void subscribeTopicEditContact(@UIEventTopic(EventConstants.TOPIC_EDIT) boolean e) {
		editable = !editable;
		switchEditable(editable);
	}
	
	@Inject
	@Optional
	private void subscribeTopicDeleteContact(@UIEventTopic(EventConstants.DELETE_CONTACT) String e) {
		if (selectedContacts != null) {
			for (Contact c: selectedContacts) {
				db.removeContact(c);
			}
			natTable.refresh();
			updateContactDetail(new Contact(-1));
		}
	}
	
	@Inject
	@Optional
	private void subscribeTopicNewContact(@UIEventTopic(EventConstants.NEW_CONTACT) Contact c) {
		saveChanges();
		
		currentContact = c;
		natTable.refresh();
		selectionLayer.selectRow(0, toIntExact(c.getId()), false, false);
		updateContactDetail(c);
		editable = true;
		switchEditable(editable);
	}
	
	public void switchEditable(boolean editable) {
		//drawContactDetails(inputs, editable, body);
		
		//Change label
		for (MToolBarElement item : part.getToolbar().getChildren()) {
			if ("aero.minova.test.saw.rcp.handledtoolitem.bearbeiten".equals(item.getElementId())) {
				if (editable) {
					((MUILabel) item).setLabel("Fertig!");
					//engine.setClassname(item, "MyCSSTagForLabel");
				} else {
					((MUILabel) item).setLabel("Bearbeiten");
					natTable.redraw();
				}
				
			}
		}
				
		headText.setEditable(editable);
		headText.setFocus();
		companyText.setEditable(editable);
		phoneText.setEditable(editable);
		hpText.setEditable(editable);
		
		updateVisibility(editable);
		
		//Update Contact
		if (!editable) {
			saveChanges();
		}
	}
	
	public void saveChanges() {
		if (currentContact != null) {
			currentContact.setFirstName(headText.getText());
			currentContact.setCompany(companyText.getText());
			currentContact.setPhonenumber(phoneText.getText());
			currentContact.setHomepage(hpText.getText());
		}
	}
	
	public void updateVisibility(boolean allVisible) {
		
		boolean vis = allVisible || !headText.getText().strip().equals("");
		headLabel.setVisible(vis);
		headText.setVisible(vis);
		 
		vis = allVisible || !companyText.getText().strip().equals("");
		companyLabel.setVisible(vis);
		companyText.setVisible(vis);
			
		vis = allVisible || !phoneText.getText().strip().equals("");
		phoneText.setVisible(vis);
		phoneCombo.setVisible(vis);
			
		vis = allVisible || !hpText.getText().strip().equals("");
		hpText.setVisible(vis);
		hpLabel.setVisible(vis);
	}

	@Override
	public void setGroupListVisible(boolean visible) {
		
		groupList.setVisible(visible);
		groupList.getParent().requestLayout();
	}
}