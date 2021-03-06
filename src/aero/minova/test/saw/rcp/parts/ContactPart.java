
package aero.minova.test.saw.rcp.parts;

import static org.eclipse.jface.widgets.ButtonFactory.newButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowIdAccessor;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditBindings;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditConfiguration;
import org.eclipse.nebula.widgets.nattable.extension.e4.selection.E4SelectionListener;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import aero.minova.test.saw.rcp.constants.EventConstants;
import aero.minova.test.saw.rcp.entries.DefaultPropertyEntry;
import aero.minova.test.saw.rcp.entries.NotesPropertyEntry;
import aero.minova.test.saw.rcp.entries.PhotoPropertyEntry;
import aero.minova.test.saw.rcp.entries.PropertyEntry;
import aero.minova.test.saw.rcp.handlers.ContactColumnPropertyAccessor;
import aero.minova.test.saw.rcp.handlers.DragAndDropSupportContacts;
import aero.minova.test.saw.rcp.handlers.DragAndDropSupportGroups;
import aero.minova.test.saw.rcp.handlers.EditorConfigurationGrouplist;
import aero.minova.test.saw.rcp.handlers.GroupColumnPropertyAccessor;
import aero.minova.test.saw.rcp.handlers.SendMailHandler;
import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.Database;
import aero.minova.test.saw.rcp.model.Group;
import aero.minova.test.saw.rcp.vCard.VCardExportHandler;
import aero.minova.test.saw.rcp.vCard.VCardOptions;

public class ContactPart implements GroupListViewer {

	private Database db = Database.getInstance();

	private Composite groupList;
	private Composite contactList;
	private Composite contactDetail;
	private SashForm sashForm;

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

	private boolean editable = false;
	private Map<String, PropertyEntry> entries;

	@Inject
	private MPart part;
	@Inject
	private ESelectionService service;
	@Inject
	private Shell shell;
	@Inject
	EMenuService menuService;

	@Inject
	public ContactPart() {}

	@PostConstruct
	public void postConstruct(Composite parent) {
		shell.setBounds(-1200, -500, 800, 600);

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

		newButton(SWT.PUSH).text("Neue Gruppe").onSelect(e -> newGroup()).create(groupList);
		deleteGroupButton = newButton(SWT.PUSH).text("Gruppe Löschen").onSelect(e -> deleteGroup()).create(groupList);
		deleteGroupButton.setEnabled(false);

		IColumnPropertyAccessor<Group> columnPropertyAccessor = new GroupColumnPropertyAccessor();
		IRowDataProvider<Group> bodyDataProvider = new ListDataProvider<Group>(groups, columnPropertyAccessor);

		bodyDataLayerGroup = new DataLayer(bodyDataProvider);

		selectionLayerGroup = new SelectionLayer(bodyDataLayerGroup);
		// selectionLayerGroup.addConfiguration(new DefaultRowSelectionLayerConfiguration());
		selectionLayerGroup.setSelectionModel(new RowSelectionModel<>(selectionLayerGroup, bodyDataProvider, new IRowIdAccessor<Group>() {
			@Override
			public Serializable getRowId(Group rowObject) {
				return rowObject.getGroupID();
			}
		}, true));
		E4SelectionListener<Group> eslGroup = new E4SelectionListener<Group>(service, selectionLayerGroup, bodyDataProvider);
		selectionLayerGroup.addLayerListener(eslGroup);

		ViewportLayer viewportLayer = new ViewportLayer(selectionLayerGroup);
		viewportLayer.setRegionName(GridRegion.BODY);

		groupTable = new NatTable(groupList, viewportLayer, false);

		menuService.registerContextMenu(groupTable, "aero.minova.test.saw.rcp.popupmenu.groupMenu");
		groupTable.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {}

			@Override
			public void mouseUp(MouseEvent e) {}

			// Wähle Gruppe bei Rechtsklick
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 3) {
					selectionLayerGroup.selectRow(0, groupTable.getRowPositionByY(e.y), false, false);
				}
			}
		});

		DragAndDropSupportGroups dndSupport = new DragAndDropSupportGroups(groupTable, selectionLayerGroup);
		Transfer[] transfer = { FileTransfer.getInstance() };
		groupTable.addDragSupport(DND.DROP_COPY, transfer, dndSupport);
		groupTable.addDropSupport(DND.DROP_COPY, transfer, dndSupport);

		final ColumnOverrideLabelAccumulator columnLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayerGroup);
		bodyDataLayerGroup.setConfigLabelAccumulator(columnLabelAccumulator);

		groupTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		groupTable.addConfiguration(new EditorConfigurationGrouplist());

		// Editieren der Gruppennamen
		groupTable.addConfiguration(new AbstractRegistryConfiguration() {
			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, new IEditableRule() {

					@Override
					public boolean isEditable(int columnIndex, int rowIndex) {
						if (columnIndex == 0)
							return true;
						return false;
					}

					@Override
					public boolean isEditable(ILayerCell cell, IConfigRegistry configRegistry) {
						if (cell.getRowIndex() == 0)
							return false;
						if (cell.getColumnIndex() == 0)
							return true;
						return false;
					}
				});
			}
		});
		bodyDataLayerGroup.addConfiguration(new DefaultEditConfiguration());
		bodyDataLayerGroup.addConfiguration(new DefaultEditBindings());

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

		E4SelectionListener<Contact> esl = new E4SelectionListener<Contact>(service, selectionLayerContact, bodyDataProvider);
		selectionLayerContact.addLayerListener(esl);

		ViewportLayer viewportLayer = new ViewportLayer(selectionLayerContact);
		viewportLayer.setRegionName(GridRegion.BODY);

		contactTable = new NatTable(contactList, viewportLayer, true);

		// Rechtsklick-Menü
		menuService.registerContextMenu(contactTable, "aero.minova.test.saw.rcp.popupmenu.contactMenu");
		contactTable.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {}

			@Override
			public void mouseUp(MouseEvent e) {}

			// Wähle Kontakt bei Rechtsklick
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 3) {
					selectionLayerContact.selectRow(0, contactTable.getRowPositionByY(e.y), false, false);
				}
			}
		});

		DragAndDropSupportContacts dndContact = new DragAndDropSupportContacts(contactTable, selectionLayerContact);
		Transfer[] transferFile = { FileTransfer.getInstance() };
		contactTable.addDragSupport(DND.DROP_COPY, transferFile, dndContact);
		contactTable.addDropSupport(DND.DROP_COPY, transferFile, dndContact);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(contactTable);

		selectionLayerContact.selectRow(0, 0, false, false);
	}

	private void createContactDetail(Composite body) {

		entries = new LinkedHashMap<String, PropertyEntry>();

		// Layout für Body definieren
		body.setLayout(new GridLayout(3, false));

		// Profilbild
		entries.put(VCardOptions.PHOTO, new PhotoPropertyEntry(body));

		// Normale input Felder
		entries.put(VCardOptions.NAME, new DefaultPropertyEntry(body, VCardOptions.NAME));
		entries.put(VCardOptions.ORG, new DefaultPropertyEntry(body, VCardOptions.ORG));
		entries.put(VCardOptions.TEL, new DefaultPropertyEntry(body, VCardOptions.TEL));
		entries.put(VCardOptions.EMAIL, new DefaultPropertyEntry(body, VCardOptions.EMAIL));
		entries.put(VCardOptions.BDAY, new DefaultPropertyEntry(body, VCardOptions.BDAY));
		entries.put(VCardOptions.ADR, new DefaultPropertyEntry(body, VCardOptions.ADR));

		// Notizen
		entries.put(VCardOptions.NOTE, new NotesPropertyEntry(body));
	}

	@Inject
	@Optional
	private void handleSelectionGroup(@Named(IServiceConstants.ACTIVE_SELECTION) List<Group> selected) {
		if (selected != null && selected.size() > 0 && selected.get(0) instanceof Group) {
			contacts.clear();
			contacts.addAll(selected.get(0).getMembers());
			contactTable.refresh();
			selectedGroups = selected;
			selectionLayerContact.selectRow(0, 0, false, false);

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
			saveChanges();
			currentContact = selected.get(0);
			updateContactDetail(selected.get(0));
			selectedContacts = selected;
			switchEditable(false);
		}
	}

	private void showContacts(List<Contact> contactsToShow) {
		contacts.clear();
		contacts.addAll(contactsToShow);
		contactTable.refresh();
		groupTable.refresh();
	}

	private void filterContactTable(String s) {
		List<Contact> list = new ArrayList<Contact>();
		for (Contact c : selectedGroups.get(0).getMembers()) {
			if (c.getValueString(VCardOptions.NAME).toLowerCase().contains(s))
				list.add(c);
		}
		contacts.clear();
		contacts.addAll(list);
		contactTable.refresh();
	}

	private void updateContactDetail(Contact c) {

		for (String s : entries.keySet()) {
			entries.get(s).setInput(c);
		}

		contactDetail.requestLayout();
	}

	@Inject
	@Optional
	private void subscribeTopicNewContact(@UIEventTopic(EventConstants.NEW_CONTACT) Contact c) {
		saveChanges();

		currentContact = c;
		selectedGroups.get(0).addMember(c);
		updateContactDetail(c);
		showContacts(selectedGroups.get(0).getMembers());

		selectionLayerGroup.selectRow(0, db.getPositionOfGroup(selectedGroups.get(0)), false, false);
		selectionLayerContact.selectRow(0, selectedGroups.get(0).getPositionInList(c), false, false);

		switchEditable(true);
	}

	@Inject
	@Optional
	private void subscribeTopicNewContact(@UIEventTopic(EventConstants.SELECT_CONTACTS) List<Contact> contacts) {
		saveChanges();
		for (Contact c : contacts) {
			selectionLayerContact.selectRow(0, selectedGroups.get(0).getPositionInList(c), true, false);
		}
	}

	@Inject
	@Optional
	private void subscribeTopicExistingContact(@UIEventTopic(EventConstants.CONTACT_EXISTS) int amount) {
		if (amount > 1)
			MessageDialog.openInformation(shell, null, amount + " dieser Kontakte existieren bereits und werden aktualisiert.");
		else
			MessageDialog.openInformation(shell, null, "Dieser Kontakte existiert bereits und wird aktualisiert.");
	}

	@Inject
	@Optional
	private void subscribeTopicRefreshContacts(@UIEventTopic(EventConstants.REFRESH_CONTACTS) Contact c) {
		saveChanges();

		currentContact = c;
		selectedGroups.get(0).addMember(c);
		updateContactDetail(c);
		showContacts(selectedGroups.get(0).getMembers());

		selectionLayerGroup.selectRow(0, db.getPositionOfGroup(selectedGroups.get(0)), false, false);
		selectionLayerContact.selectRow(0, selectedGroups.get(0).getPositionInList(c), false, false);
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
				MessageDialog dia;
				if (selectedContacts.size() == 1) {
					Contact c = selectedContacts.get(0);
					dia = new MessageDialog(shell, "Löschen", null, "Wollen Sie den Kontakt \"" + c.getValueString(VCardOptions.NAME) + "\" endgültig löschen?",
							MessageDialog.CONFIRM, new String[] { "Löschen", "Abbrechen" }, 0);
				} else {
					dia = new MessageDialog(shell, "Löschen", null, "Wollen Sie " + selectedContacts.size() + " Kontakte endgültig löschen?",
							MessageDialog.CONFIRM, new String[] { "Löschen", "Abbrechen" }, 0);
				}
				int result = dia.open();
				if (result == 0) {
					for (Contact c : selectedContacts) {
						db.removeContact(c);
					}
				}
			} else {
				Group g = db.getGroupById(selectedGroups.get(0).getGroupID());
				MessageDialog dia;
				if (selectedContacts.size() == 1) {
					Contact c = selectedContacts.get(0);
					dia = new MessageDialog(shell, "Entfernen", null,
							"Wollen Sie den Kontakt \"" + c.getValueString(VCardOptions.NAME) + "\" aus der Gruppe \"" + g.getName() + "\" entfernen?",
							MessageDialog.CONFIRM, new String[] { "Entfernen", "Abbrechen" }, 0);
				} else {
					dia = new MessageDialog(shell, "Entfernen", null,
							"Wollen Sie " + selectedContacts.size() + " Kontakte aus der Gruppe \"" + g.getName() + "\" entfernen?", MessageDialog.CONFIRM,
							new String[] { "Entfernen", "Abbrechen" }, 0);
				}
				int result = dia.open();
				if (result == 0) {
					for (Contact c : selectedContacts) {
						g.removeMember(c);
					}
				}
			}

			showContacts(selectedGroups.get(0).getMembers());
			selectionLayerGroup.selectRow(0, db.getPositionOfGroup(selectedGroups.get(0)), false, false);
			selectionLayerContact.selectRow(0, 0, false, false);
			groupTable.refresh();
		} else {
			MessageDialog.openInformation(shell, null, "Es ist kein Kontakt ausgewählt");
		}
	}

	@Inject
	@Optional
	private void subscribeTopicExportVCard(@UIEventTopic(EventConstants.EXPORT_VCARD) String e) {
		VCardExportHandler.exportVCard(currentContact);
	}

	@Inject
	@Optional
	private void subscribeTopicExportGroup(@UIEventTopic(EventConstants.EXPORT_GROUP) String e) {
		VCardExportHandler.exportVCard(selectedGroups.get(0));
	}

	@Inject
	@Optional
	private void subscribeTopicSendMail(@UIEventTopic(EventConstants.SEND_MAIL) String e) {
		SendMailHandler.sendMail(selectedGroups.get(0));
	}

	@Inject
	@Optional
	private void subscribeTopicDeleteGroup(@UIEventTopic(EventConstants.DELETE_GROUP) String e) {
		deleteGroup();
	}

	private void switchEditable(boolean editable) {
		this.editable = editable;

		// Change Text on Edit button
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

		for (String s : entries.keySet()) {
			entries.get(s).setEditable(editable);
		}
		if (!editable && entries.get(VCardOptions.NAME).getTypeEntryByType("") != null)
			entries.get(VCardOptions.NAME).getTypeEntryByType("").getTypeLabel().setFocus();
		contactDetail.requestLayout();

		// Update Contact
		if (!editable) {
			saveChanges();
		}
		contactDetail.requestLayout();
	}

	private void saveChanges() {
		if (currentContact != null) {
			for (String s : entries.keySet()) {
				entries.get(s).updateContact();
			}
		}
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
				selectionLayerGroup.selectRow(0, 0, false, false);
			}
		} else {
			MessageDialog.openInformation(shell, null, "Es ist keine Gruppe ausgewählt");
		}
	}
}