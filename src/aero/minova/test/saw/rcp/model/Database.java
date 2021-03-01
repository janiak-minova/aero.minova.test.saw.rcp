package aero.minova.test.saw.rcp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

	public Contact getContactById(long id) {
		for (Contact c : contacts) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}

	public Group getGroupById(long id) {
		for (Group g : groups) {
			if (g.getGroupID() == id) {
				return g;
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

	public Contact addContact(String company, String name) {
		Contact c = new Contact(company, name, currentContacts.getAndIncrement());
		contacts.add(c);
		getGroupById(0).addMember(c);
		return c;
	}

	public Contact addContact(String company, String name, String homepage, String phonenumber, String notes) {
		Contact c = new Contact(company, name, homepage, phonenumber, notes, currentContacts.getAndIncrement());
		contacts.add(c);
		getGroupById(0).addMember(c);
		return c;
	}

	public Contact addContact(String company, String name, String homepage, String phonenumber, String notes, String picLocaiton) {
		Contact c = new Contact(company, name, homepage, phonenumber, notes, picLocaiton, currentContacts.getAndIncrement());
		contacts.add(c);
		getGroupById(0).addMember(c);
		return c;
	}

	public Contact addContact(String company, String name, String homepage, String phonenumber, String mail, String notes, String picLocaiton) {
		Contact c = new Contact(company, name, homepage, phonenumber, mail, notes, picLocaiton, currentContacts.getAndIncrement());
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
		addContact("Minova", "Erik Fisher");
		addContact("Minova", "Max Mustermann");
		addContact("Minova", "Dieter Vogel");
		addContact("Test", "Thorsten Schuster");
		addContact("Test", "Ursula Fischer");

		addContact("Company", "Tim Biermann", "www.homepage.de", "12345678", "some notes", "icons/user2.png");
		addContact("Company", "Andrea Zimmer", "", "03381 15 45 05", "");

		addGroup(List.of(getContactById(0), getContactById(1)), "Freunde");
		addGroup(List.of(getContactById(3), getContactById(4), getContactById(5), getContactById(6)), "Arbeit");
	}

	public void consume(Consumer<List<Contact>> taskConsumer) {
		// always pass a new copy of the data
		taskConsumer.accept(contacts.stream().map(Contact::copy).collect(Collectors.toList()));
	}

	public void removeContact(Contact c) {
		if (contacts.contains(c)) {
			contacts.remove(c);
		}

		for (Group g : groups) {
			g.removeMember(c);
		}
		// System.out.println(c.getFirstName());
	}

	public void removeGroup(Group g) {
		if (groups.contains(g) && g.getGroupID() != 0) {
			groups.remove(g);
		}
	}
}
