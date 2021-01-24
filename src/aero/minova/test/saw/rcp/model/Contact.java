package aero.minova.test.saw.rcp.model;

/**
 * Jede natürliche Peron stellt einen Kontakt dar.
 * 
 * @author saak
 */
public class Contact {

	private String Company;
	private String firstName;
	private String jobDescription;
	private String lastName;
	private String title;

	/**
	 * Name der Firma, Organsisation, ... bei der diese Person arbeitet
	 * 
	 * @return Name der Firma
	 */
	public String getCompany() {
		return Company;
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
		Company = company;
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

}
