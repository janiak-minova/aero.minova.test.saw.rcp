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
	private static AtomicInteger currentGroups = new AtomicInteger(0);
	
	private static final Database db = new Database(); 
    
    private Database() { 
        this.contacts = new ArrayList<Contact>();
        this.groups = new ArrayList<Group>();
        
        generateTestData();
    } 
         
    public static Database getInstance() { 
      return db; 
    }

	public List<Contact> getContacts() {
		return contacts;
	}
	
	public Contact getContactById(long id) {
		for (Contact c: contacts) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}
	
	public Contact addContact() {
		Contact c = new Contact(currentContacts.getAndIncrement());
		contacts.add(c);
		return c;
	}
	
	public Contact addContact(String company, String name) {
		Contact c = new Contact(company, name, currentContacts.getAndIncrement());
		contacts.add(c);
		return c;
	}
	
	public Contact addContact(String company, String name, String homepage, String phonenumber, String notes) {
		Contact c = new Contact(company, name, homepage, phonenumber, notes, currentContacts.getAndIncrement());
		contacts.add(c);
		return c;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void addGroup(List<Contact> members, String name) {
		 ArrayList<Contact> mList = new ArrayList<Contact>();
		 for (Contact m: members) {
			 mList.add(m);
		 }
		 groups.add(new Group(mList, name, currentGroups.getAndIncrement()));
	}
	
	private void generateTestData() {
		addContact("Minova", "Erik Fisher");
		addContact("Minova", "Max Mustermann");
		addContact("Minova", "Dieter Vogel");
		addContact("Test", "Thorsten Schuster");
		addContact("Test", "Ursula Fischer");
		
		addContact("Company", "Tim Biermann", "www.homepage.de", "12345678", "some notes");
		addContact("Company", "Andrea Zimmer", "", "03381 15 45 05", "");
		
		addGroup(List.of(getContactById(0), getContactById(1)), "Freunde");
	}
	
	public void consume(Consumer<List<Contact>> taskConsumer) {
		// always pass a new copy of the data
		taskConsumer.accept(contacts.stream().map(Contact::copy).collect(Collectors.toList()));
	}
	
	public void removeContact(Contact c) {
		if (contacts.contains(c)) {
			contacts.remove(c);
		}
		
		for (Group g: groups) {
			g.removeMember(c);
		}
		//System.out.println(c.getFirstName());
	}
}
