package aero.minova.test.saw.rcp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import aero.minova.test.saw.rcp.vCard.VCardOptions;

public class Database {

	private List<Contact> contacts;
	private List<Group> groups;

	private static AtomicInteger currentContacts = new AtomicInteger(0);
	private static AtomicInteger currentGroups = new AtomicInteger(1);

	private static final Database db = new Database();

	private Database() {
		this.contacts = new ArrayList<Contact>();
		this.groups = new ArrayList<Group>();

		groups.add(new Group("Alle Kontakte", 0));

		generateTestData();
	}

	public static Database getInstance() {
		return db;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public Group getGroupById(long id) {
		for (Group g : groups) {
			if (g.getGroupID() == id) {
				return g;
			}
		}
		return null;
	}

	public Contact getContactById(long id) {
		for (Contact c : contacts) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}

	public Group getGroupByPosition(int pos) {
		if (groups.size() >= pos && pos >= 0)
			return groups.get(pos);
		else
			return null;
	}

	public int getPositionOfGroup(Group g) {
		int i = 0;
		for (Group group : groups) {
			if (g.equals(group))
				return i;
			i += 1;
		}
		return -1;
	}

	public Group getByPositionOfGroup(int pos) {
		return groups.get(pos);
	}

	public Contact addContact() {
		Contact c = new Contact(currentContacts.getAndIncrement());
		contacts.add(c);
		getGroupById(0).addMember(c);
		return c;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void addGroup(List<Contact> members, String name) {
		ArrayList<Contact> mList = new ArrayList<Contact>();
		for (Contact m : members) {
			mList.add(m);
		}
		groups.add(new Group(mList, name, currentGroups.getAndIncrement()));
	}

	public void addGroup(String name) {
		groups.add(new Group(name, currentGroups.getAndIncrement()));
	}

	private void generateTestData() {
		Contact c = addContact();

		c.setProperty(VCardOptions.NAME, new NameValue("Fischer;Erik;;;"));
		c.setProperty(VCardOptions.ORG, "Minova");
		c.setProperty(VCardOptions.TEL, VCardOptions.WORK, "123345346");
		c.setProperty(VCardOptions.TEL, VCardOptions.HOME, "9876543");
		c.setProperty(VCardOptions.BDAY, new DateValue("01.01.1990"));
		c.setProperty(VCardOptions.ADR, VCardOptions.HOME, new AddressValue(";;Straße 123;Würzburg;;97070;Deutschland"));

		c = addContact();
		c.setProperty(VCardOptions.NAME, new NameValue("Mustermann;Max;;;"));
		c.setProperty(VCardOptions.ORG, "Minova");
		c.setProperty(VCardOptions.TEL, VCardOptions.HOME, "9876543");

		c = addContact();
		c.setProperty(VCardOptions.NAME, new NameValue("Vogel;Dieter;;;"));
		c.setProperty(VCardOptions.ORG, "Company");

		c = addContact();
		c.setProperty(VCardOptions.NAME, new NameValue("Schuster;Thorsten;;;"));
		c.setProperty(VCardOptions.ORG, "Company");

		c = addContact();
		c.setProperty(VCardOptions.NAME, new NameValue("Fischer;Ursula;;;"));
		c.setProperty(VCardOptions.ORG, "Company");

		c = addContact();
		c.setProperty(VCardOptions.NAME, new NameValue("Biermann;Tim;;;"));
		c.setProperty(VCardOptions.ORG, "Testfirma");
		c.setProperty(VCardOptions.TEL, VCardOptions.WORK, "5647");
		c.setProperty(VCardOptions.EMAIL, VCardOptions.HOME, "tim.biermann@gmail.com");
		c.setProperty(VCardOptions.ADR, VCardOptions.HOME, new AddressValue(";;Straße 123;Würzburg;;97070;Deutschland"));
		c.setProperty(VCardOptions.NOTE, "some notes");

		c = addContact();
		c.setProperty(VCardOptions.NAME, new NameValue("Zimmer;Andrea;;Dr.;"));
		c.setProperty(VCardOptions.ORG, "Testfirma");
		c.setProperty(VCardOptions.TEL, VCardOptions.WORK, "42424242");
		c.setProperty(VCardOptions.EMAIL, VCardOptions.HOME, "mail@gmail.com");
		c.setProperty(VCardOptions.ADR, VCardOptions.HOME, new AddressValue(";;Gasse 42;Würzburg;;97080;Deutschland"));
		c.setProperty(VCardOptions.ADR, VCardOptions.WORK, new AddressValue(";;Straße 123;Würzburg;;97070;Deutschland"));
		c.setProperty(VCardOptions.NOTE, "mehr Notizen");

		addGroup(List.of(getContactById(0), getContactById(1)), "Freunde");
		addGroup(List.of(getContactById(3), getContactById(4), getContactById(5), getContactById(6)), "Arbeit");
	}

	public void removeContact(Contact c) {
		if (contacts.contains(c)) {
			contacts.remove(c);
		}

		for (Group g : groups) {
			g.removeMember(c);
		}
	}

	public void removeGroup(Group g) {
		if (groups.contains(g) && g.getGroupID() != 0) {
			groups.remove(g);
		}
	}

	public Contact getContactByName(Value value) {
		for (Contact c : contacts) {
			if (c.getValue(VCardOptions.NAME).equals(value))
				return c;
		}
		return null;
	}
}
