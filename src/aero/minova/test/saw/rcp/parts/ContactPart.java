
package aero.minova.test.saw.rcp.parts;

import static java.lang.Math.toIntExact;
import static org.eclipse.jface.widgets.ButtonFactory.newButton;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowIdAccessor;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditBindings;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditConfiguration;
import org.eclipse.nebula.widgets.nattable.extension.e4.selection.E4SelectionListener;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultRowSelectionLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import aero.minova.test.saw.rcp.events.EventConstants;
import aero.minova.test.saw.rcp.handlers.ContactColumnPropertyAccessor;
import aero.minova.test.saw.rcp.handlers.DragAndDropSupportContacts;
import aero.minova.test.saw.rcp.handlers.DropSupportGroups;
import aero.minova.test.saw.rcp.handlers.EditorConfigurationGrouplist;
import aero.minova.test.saw.rcp.handlers.GroupColumnPropertyAccessor;
import aero.minova.test.saw.rcp.handlers.VCardExportHandler;
import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.Database;
import aero.minova.test.saw.rcp.model.Group;

public class ContactPart implements GroupListViewer {

	private Database db = Database.getInstance();

	private Composite groupList;
	private Composite contactList;
	private Composite contactDetail;
	private SashForm sashForm;

//	private int[] weights = new int[] {1, 1, 2};
//	private static final int MIN_WIDTH_GROUPLIST = 300;
//	private static final int MIN_WIDTH_CONTACTLIST = 300;
//	private static final int MIN_WIDTH_CONTACTDETAIL = 50;

	public static final String COLUMN_ONE_LABEL = "ColumnOneLabel";
	public static final String COLUMN_TWO_LABEL = "ColumnTwoLabel";

	private Button deleteGroupButton;
	private NatTable groupTable;
	private SelectionLayer selectionLayerGroup;
	private DataLayer bodyDataLayerGroup;
	private List<Group> groups;
	private List<Group> selectedGroups = new ArrayList<>();

	private NatTable contactTable;
	private SelectionLayer selectionLayerContact;
	private DataLayer bodyDataLayerContact;
	private Contact currentContact;
	private List<Contact> contacts;
	private List<Contact> selectedContacts;

	private String defaultPic = "icons/user.png";
	private Label profileLable;
	private Label nameLabel;
	private Text nameText;
	private Label companyLabel;
	private Text companyText;
	private Combo phoneCombo;
	private Text phoneText;
	private Label hpLabel;
	private Text hpText;
	private Label notesLabel;
	private Text notesText;
	private boolean editable = false;
	private LinkedHashMap<Text, Control> inputs;

	@Inject
	private MPart part;
	@Inject
	private ESelectionService service;
	@Inject
	private Shell shell;
	@Inject
	private Display display;

	@Inject
	public ContactPart() {}

	@PostConstruct
	public void postConstruct(Composite parent) {
		shell.setBounds(3500, -500, 800, 600);

		contacts = new ArrayList<Contact>(db.getContacts());
		groups = db.getGroups();
		selectedGroups.add(db.getGroupById(0));

		sashForm = new SashForm(parent, SWT.HORIZONTAL);
		sashForm.addControlListener(new ContactRezieListener(sashForm));
		sashForm.setSashWidth(1);

		groupList = new Composite(sashForm, SWT.None);
		contactList = new Composite(sashForm, SWT.None);
		contactDetail = new Composite(sashForm, SWT.None);

		createGroupList(groupList);
		createContactList(contactList);
		createContactDetail(contactDetail);
	}

	private void createGroupList(Composite groupList) {
		// groupList.setVisible(false);
		groupList.setLayout(new GridLayout(2, false));
//		
		newButton(SWT.PUSH).text("Neue Gruppe").onSelect(e -> newGroup()).create(groupList);
		deleteGroupButton = newButton(SWT.PUSH).text("Gruppe Löschen").onSelect(e -> deleteGroup()).create(groupList);
		deleteGroupButton.setEnabled(false);

		IColumnPropertyAccessor<Group> columnPropertyAccessor = new GroupColumnPropertyAccessor();
		IRowDataProvider<Group> bodyDataProvider = new ListDataProvider<Group>(groups, columnPropertyAccessor);

		bodyDataLayerGroup = new DataLayer(bodyDataProvider);

		selectionLayerGroup = new SelectionLayer(bodyDataLayerGroup);
		selectionLayerGroup.addConfiguration(new DefaultRowSelectionLayerConfiguration());
		E4SelectionListener<Group> eslGroup = new E4SelectionListener<Group>(service, selectionLayerGroup, bodyDataProvider);
		selectionLayerGroup.addLayerListener(eslGroup);

		ViewportLayer viewportLayer = new ViewportLayer(selectionLayerGroup);
		viewportLayer.setRegionName(GridRegion.BODY);

		groupTable = new NatTable(groupList, viewportLayer, false);

		DropSupportGroups dndSupport = new DropSupportGroups(groupTable);
		Transfer[] transfer = { FileTransfer.getInstance() };
		groupTable.addDropSupport(DND.DROP_COPY, transfer, dndSupport);

		// Edit support
//        groupTable.addConfiguration(new DefaultNatTableStyleConfiguration());
//        groupTable.addConfiguration(new AbstractRegistryConfiguration() {
//            @Override
//            public void configureRegistry(IConfigRegistry configRegistry) {
//                configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE);
//            }
//        });
//        groupTable.configure();
		final ColumnOverrideLabelAccumulator columnLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayerGroup);
		bodyDataLayerGroup.setConfigLabelAccumulator(columnLabelAccumulator);
		columnLabelAccumulator.registerColumnOverrides(0, COLUMN_ONE_LABEL);
		columnLabelAccumulator.registerColumnOverrides(1, COLUMN_TWO_LABEL);

		groupTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		groupTable.addConfiguration(new EditorConfigurationGrouplist());
		groupTable.configure();

		bodyDataLayerGroup.addConfiguration(new DefaultEditConfiguration());
		bodyDataLayerGroup.addConfiguration(new DefaultEditBindings());

//        groupTable.addConfiguration(new DefaultNatTableStyleConfiguration());
//        groupTable.addConfiguration(new EditorConfiguration());
		groupTable.configure();

		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(groupTable);

		selectionLayerGroup.selectRow(0, 0, false, false);
	}

	private void createContactList(Composite contactList) {

		contactList.setLayout(new GridLayout(1, false));

		Text search = new Text(contactList, SWT.SEARCH | SWT.CANCEL | SWT.ICON_SEARCH);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
		search.setLayoutData(gd);
		search.setMessage("Suche Kontakt");
		search.setSize(1000, 200);
		ModifyListener listener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String s = search.getText().toLowerCase().trim();
				filterContactTable(s);
			}
		};
		search.addModifyListener(listener);

		IColumnPropertyAccessor<Contact> columnPropertyAccessor = new ContactColumnPropertyAccessor();
		IRowDataProvider<Contact> bodyDataProvider = new ListDataProvider<Contact>(contacts, columnPropertyAccessor);
		bodyDataLayerContact = new DataLayer(bodyDataProvider);

		selectionLayerContact = new SelectionLayer(bodyDataLayerContact);
		selectionLayerContact.setSelectionModel(new RowSelectionModel<>(selectionLayerContact, bodyDataProvider, new IRowIdAccessor<Contact>() {
			@Override
			public Serializable getRowId(Contact rowObject) {
				return rowObject.getId();
			}
		}, true));

		// selectionLayerContact.addConfiguration(new DefaultRowSelectionLayerConfiguration());

		E4SelectionListener<Contact> esl = new E4SelectionListener<Contact>(service, selectionLayerContact, bodyDataProvider);
		selectionLayerContact.addLayerListener(esl);

		ViewportLayer viewportLayer = new ViewportLayer(selectionLayerContact);
		viewportLayer.setRegionName(GridRegion.BODY);

		contactTable = new NatTable(contactList, viewportLayer);

		DragAndDropSupportContacts dndContact = new DragAndDropSupportContacts(contactTable, selectionLayerContact, contacts);
		Transfer[] transferFile = { FileTransfer.getInstance() };
		contactTable.addDragSupport(DND.DROP_COPY, transferFile, dndContact);
		contactTable.addDropSupport(DND.DROP_COPY, transferFile, dndContact);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(contactTable);

		selectionLayerContact.selectRow(0, 0, false, false);
	}

	private void createContactDetail(Composite body) {

		GridData gd;
		inputs = new LinkedHashMap<Text, Control>();

		// Layout für Body definieren
		body.setLayout(new GridLayout(2, false));

		// Profile picture
		profileLable = new Label(body, SWT.RIGHT);
		gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gd.horizontalSpan = 2;
		profileLable.setLayoutData(gd);
		addProfilePic(defaultPic);
		profileLable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				editProfilePic();
			}
		});

		// Kopf Label und Feld definieren
		nameLabel = new Label(body, SWT.RIGHT);
		nameLabel.setText("Name");
		gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		nameLabel.setLayoutData(gd);
		nameText = new Text(body, SWT.NONE);
		// headText.setText("Wilfried Saak");
		nameText.setEditable(false);
		nameText.setMessage("Vorname Nachname");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		nameText.setLayoutData(gd);
		inputs.put(nameText, nameLabel);

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
		// companyText.setText("MINOVA Information Services GmbH");
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
		phoneText.setEditable(false);
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
		hpText.setEditable(false);
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
		notesText = new Text(body, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		notesText.setLayoutData(new GridData(GridData.FILL_BOTH));
		inputs.put(notesText, notesLabel);

		ModifyListener listener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String newNotes = ((Text) e.widget).getText();
				if (currentContact != null) {
					currentContact.setNotes(newNotes);
				}
			}
		};
		notesText.addModifyListener(listener);

		// drawContactDetails(inputs);

	}

	private void addProfilePic(String path) {
		if (path == null || path.equals(""))
			path = defaultPic;

		Image image;
		if (path.contains("icons")) {
			Bundle bundle = FrameworkUtil.getBundle(getClass());
			URL url = FileLocator.find(bundle, new Path(path), null);
			ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
			image = imageDesc.createImage();
		} else {
			image = new Image(null, path);
		}

		Image scaled = new Image(Display.getDefault(), 50, 50);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, 50, 50);
		gc.dispose();
		image.dispose();

		profileLable.setImage(scaled);
		profileLable.setSize(50, 50);
	}

	private void editProfilePic() {
		if (editable) {
			FileDialog dialog = new FileDialog(new Shell(), SWT.OPEN);
			dialog.setFilterExtensions(new String[] { "*.png", "*.gif", "*.bmp", "*.jpg", "*.tiff" });
			String path = dialog.open();
			currentContact.setPicLocation(path);
			addProfilePic(path);
			System.out.println(path);
		}
	}

	@Inject
	@Optional
	private void handleSelectionGroup(@Named(IServiceConstants.ACTIVE_SELECTION) List<Group> selected) {
		if (selected != null && selected.size() > 0 && selected.get(0) instanceof Group) {
			contacts.clear();
			contacts.addAll(selected.get(0).getMembers());
			contactTable.refresh();
			selectedGroups = selected;

			if (selected.get(0).getGroupID() == 0)
				deleteGroupButton.setEnabled(false);
			else
				deleteGroupButton.setEnabled(true);
		}
	}

	@Inject
	@Optional
	private void handleSelectionContact(@Named(IServiceConstants.ACTIVE_SELECTION) List<Contact> selected) {
		if (selected != null && selected.size() > 0 && selected.get(0) instanceof Contact) {
			currentContact = selected.get(0);
			updateContactDetail(selected.get(0));
			selectedContacts = selected;
			switchEditable(false);
		}
	}

	private void updateContactTable() {
		contacts.clear();
		contacts.addAll(db.getContacts());
		contactTable.refresh();
		groupTable.refresh();
	}

	private void filterContactTable(String s) {
		List<Contact> list = new ArrayList<Contact>();
		for (Contact c : selectedGroups.get(0).getMembers()) {
			if (c.getFirstName().toLowerCase().contains(s))
				list.add(c);
		}
		contacts.clear();
		contacts.addAll(list);
		contactTable.refresh();
	}

	private void updateContactDetail(Contact c) {
		addProfilePic(c.getPicLocation());
		nameText.setText(c.getFirstName());
		companyText.setText(c.getCompany());
		phoneText.setText(c.getPhonenumber());
		hpText.setText(c.getHomepage());
		notesText.setText(c.getNotes());

		updateVisibility(editable);
	}

	public void drawContactDetails(Map<Text, Control> objects) {
		GridData gd;
		Label separatorLabel = new Label(contactDetail, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 500;
		separatorLabel.setLayoutData(gd);
		separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");

		for (Text o : objects.keySet()) {
			if (editable || !o.getText().strip().equals("")) {

				System.out.println(o.getMessage());
				gd = new GridData(SWT.FILL, SWT.FILL, true, false);
				o.setLayoutData(gd);
				gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
				objects.get(o).setLayoutData(gd);
				objects.get(o).moveAbove(separatorLabel);
				o.moveAbove(separatorLabel);

				// Separator
//				separatorLabel = new Label(contactDetail, SWT.SEPARATOR | SWT.HORIZONTAL);
//				gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
//				gd.horizontalSpan = 2; // Beide Spalten verwenden
//				gd.widthHint = 500;
//				separatorLabel.setLayoutData(gd);
//				separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");

			} else {

			}

		}
		Entry<Text, Control> notes = null;
		Iterator<Entry<Text, Control>> iterator = objects.entrySet().iterator();
		while (iterator.hasNext()) {
			notes = iterator.next();
		}

		contactDetail.layout(true, true);

		gd = new GridData(SWT.RIGHT, SWT.TOP, true, false);
		notes.getValue().setLayoutData(gd);
		notes.getKey().setLayoutData(new GridData(GridData.FILL_BOTH));

		notes.getKey().moveBelow(separatorLabel);
		notes.getValue().moveBelow(separatorLabel);

	}

	@Inject
	@Optional
	private void subscribeTopicNewContact(@UIEventTopic(EventConstants.NEW_CONTACT) Contact c) {
		saveChanges();

		currentContact = c;
		updateContactTable();
		// contactTable.refresh();
		selectionLayerContact.selectRow(0, toIntExact(c.getId()), false, false);
		updateContactDetail(c);
		editable = true;
		switchEditable(editable);

		selectionLayerGroup.selectRow(0, 0, false, false);
	}

	@Inject
	@Optional
	private void subscribeTopicExistingContact(@UIEventTopic(EventConstants.CONTACT_EXISTS) Contact c) {
		MessageDialog.openInformation(shell, null, "Dieser Kontakt existiert bereits");
	}

	@Inject
	@Optional
	private void subscribeTopicEditContact(@UIEventTopic(EventConstants.TOPIC_EDIT) boolean e) {
		editable = !editable;
		switchEditable(editable);
	}

	@Override
	public void setGroupListVisible(boolean visible) {
		groupList.setVisible(visible);
		groupList.getParent().requestLayout();
	}

	@Inject
	@Optional
	private void subscribeTopicDeleteContact(@UIEventTopic(EventConstants.DELETE_CONTACT) String e) {
		if (selectedContacts != null) {
			if (selectedGroups == null || selectedGroups.get(0).getGroupID() == 0) {
				Contact c = selectedContacts.get(0);
				MessageDialog dia = new MessageDialog(shell, "Löschen", null, "Wollen Sie den Kontakt \"" + c.getFirstName() + "\" endgültig löschen?",
						MessageDialog.CONFIRM, new String[] { "Löschen", "Abbrechen" }, 0);
				int result = dia.open();
				if (result == 0) {
					db.removeContact(c);
				}
			} else {
				Group g = db.getGroupById(selectedGroups.get(0).getGroupID());
				Contact c = selectedContacts.get(0);
				MessageDialog dia = new MessageDialog(shell, "Entfernen", null,
						"Wollen Sie den Kontakt \"" + c.getFirstName() + "\" aus der Gruppe \"" + g.getName() + "\" entfernen?", MessageDialog.CONFIRM,
						new String[] { "Entfernen", "Abbrechen" }, 0);
				int result = dia.open();
				if (result == 0) {
					g.removeMember(c);
				}
			}

			updateContactTable();
			// contactTable.refresh();
			updateContactDetail(new Contact(-1));
		} else {
			MessageDialog.openInformation(shell, null, "Es ist kein Kontakt ausgewählt");
		}
	}

	@Inject
	@Optional
	private void subscribeTopicRefreshContacts(@UIEventTopic(EventConstants.EXPORT_VCARD) String e) {
		VCardExportHandler.exportVCard(currentContact);
	}

	public void switchEditable(boolean editable) {
		// drawContactDetails(inputs);

		this.editable = editable;

		// Change label
		for (MToolBarElement item : part.getToolbar().getChildren()) {
			if ("aero.minova.test.saw.rcp.handledtoolitem.bearbeiten".equals(item.getElementId())) {
				if (editable) {
					((MUILabel) item).setLabel("Fertig!");
				} else {
					((MUILabel) item).setLabel("Bearbeiten");
					contactTable.redraw();
				}

			}
		}

		nameText.setEditable(editable);
		nameText.setFocus();
		companyText.setEditable(editable);
		phoneText.setEditable(editable);
		hpText.setEditable(editable);

		updateVisibility(editable);

		// Update Contact
		if (!editable) {
			saveChanges();
		}
	}

	public void saveChanges() {
		if (currentContact != null) {
			currentContact.setFirstName(nameText.getText());
			currentContact.setCompany(companyText.getText());
			currentContact.setPhonenumber(phoneText.getText());
			currentContact.setHomepage(hpText.getText());
		}
	}

	public void updateVisibility(boolean allVisible) {

		boolean vis = allVisible || !nameText.getText().strip().equals("");
		nameLabel.setVisible(vis);
		nameText.setVisible(vis);

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

	public void newGroup() {
		db.addGroup("Neu");
		groupTable.refresh();
	}

	public void deleteGroup() {
		if (selectedGroups != null) {
			Group g = selectedGroups.get(0);
			MessageDialog dia = new MessageDialog(shell, "Löschen", null, "Wollen Sie wirklich Gruppe \"" + g.getName() + "\" löschen?", MessageDialog.CONFIRM,
					new String[] { "Löschen", "Abbrechen" }, 0);
			int result = dia.open();

			if (result == 0) {
				db.removeGroup(g);
				groupTable.refresh();
				selectionLayerGroup.selectRow(0, 0, true, true);
			}
		} else {
			MessageDialog.openInformation(shell, null, "Es ist keine Gruppe ausgewählt");
		}
	}
}