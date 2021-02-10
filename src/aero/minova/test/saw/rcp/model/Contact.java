package aero.minova.test.saw.rcp.model;

/**
 * Jede natürliche Peron stellt einen Kontakt dar.
 * 
 * @author saak
 */
public class Contact {
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "Name";
	public static final String FIELD_COMPANY = "Firma";

	private String company;
	private String firstName;
	private String jobDescription;
	private String lastName;
	private String title;
	private String homepage;
	private String phonenumber;
	private String notes;
	private final long id;
	
	public Contact(String company, String firstName, long id) {
		super();
		this.company = company;
		this.firstName = firstName;
		this.id = id;
		this.jobDescription = "";
		this.lastName = "";
		this.title = "";
		this.homepage = "";
		this.phonenumber = "";
		this.notes = "";
	}

	public Contact(long id) {
		super();
		this.id = id;
		this.company = "";
		this.firstName = "";
		this.jobDescription = "";
		this.lastName = "";
		this.title = "";
		this.homepage = "";
		this.phonenumber = "";
		this.notes = "";
	}
	
	public Contact(String company, String firstName, String homepage, String phonenumber, String notes, long id) {
		super();
		this.company = company;
		this.firstName = firstName;
		this.jobDescription = "";
		this.lastName = "";
		this.title = "";
		this.homepage = homepage;
		this.phonenumber = phonenumber;
		this.notes = notes;
		this.id = id;
	}

	public Contact(String company, String firstName, String jobDescription, String lastName, String title,
			String homepage, String phonenumber, String notes, long id) {
		super();
		this.company = company;
		this.firstName = firstName;
		this.jobDescription = jobDescription;
		this.lastName = lastName;
		this.title = title;
		this.homepage = homepage;
		this.phonenumber = phonenumber;
		this.notes = notes;
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Name der Firma, Organsisation, ... bei der diese Person arbeitet
	 * 
	 * @return Name der Firma
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * Dieser Wert enthält alle Vornamen. Die einzelnen Namen werden in der Regel mit Leerzeichen getrennt.
	 * 
	 * @return Vorname
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Aktuelle Berufsbezeichnung für die Tätigkeit, die von dieser Person ausgefüht wird.
	 * 
	 * @return aktuelle Berufsbezeichnung
	 */
	public String getJobDescription() {
		return jobDescription;
	}

	/**
	 * Nachname der Person.
	 * 
	 * @return Nachname
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Anrede der Person. In der Regel Herr, Frau, Dr., Prof., ... In anderen Ländern sich auch die Akademischen Grade noch als Titel zu sehen (z.B. Herr
	 * Dipl.-Ing.)
	 * 
	 * @return Anrede
	 */
	public String getTitle() {
		return title;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Contact copy() {
		return new Contact(this.company, this.firstName, this.jobDescription, this.lastName, this.title,
			this.homepage, this.phonenumber, this.notes, this.id);
	}

}
