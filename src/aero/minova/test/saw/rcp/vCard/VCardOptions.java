package aero.minova.test.saw.rcp.vCard;

import java.util.AbstractMap;
import java.util.Map;

public class VCardOptions {

	public static final String PHOTO = "PHOTO";
	public static final String NAME = "N";
	public static final String FNAME = "FN";

	public static final String ORG = "ORG";
	public static final String TEL = "TEL";
	public static final String EMAIL = "EMAIL";
	public static final String ADR = "ADR";
	public static final String NOTE = "NOTE";
	public static final String BDAY = "BDAY";

	public static final String HOME = "HOME";
	public static final String WORK = "WORK";
	public static final String CELL = "CELL";
	public static final String INTERNET = "INTERNET";

	public static final String PNG = "PNG";
	public static final String JPG = "JPG";
	public static final String GIF = "GIF";

	/**
	 * Die unterstützten vCard properties
	 * <ul>
	 * <li>PHOTO: Profilbild</li>
	 * <li>N: Formatierter Name (Nachname; Vorname; zweiter Name; Titel; Suffix)</li>
	 * <li>ORG: Firma/Organisation</li>
	 * <li>TEL: Telefonnummer</li>
	 * <li>EMAIL: E-Mail Adresse</li>
	 * <li>ADR: Formatierte Adresse (PO box; erweiterte Adresse (Apartment, ...); Straße und Nummer; Stadt; Region; PLZ; Land)</li>
	 * <li>Note: Notizen</li>
	 * </ul>
	 **/
	public static final String[] PROPERTIES = { "PHOTO", "N", "FN", "ORG", "TEL", "EMAIL", "ADR", "NOTE", "BDAY" };

	public static final String[] TELTYPES = { "HOME", "WORK", "CELL" };
	public static final String[] EMAILTYPES = { "HOME", "WORK", "INTERNET" };
	public static final String[] ADRTYPES = { "HOME", "WORK" };
	public static final String[] PHOTOTYPES = { "JPG", "PNG", "GIF" };

	/**
	 * Die unterstützen Typen der verschiedenen Properties <br>
	 * Ist kein Eintrag vorhanden wird nur ein Eintrag unterstüzt <br>
	 * Muss zu den verfügbaren Typen in ezvcard passen
	 * <ul>
	 * <li>TEL:"HOME", "WORK", "CELL"</li>
	 * <li>EMAIL: "HOME", "WORK"</li>
	 * <li>ADR: "HOME", "WORK"</li>
	 * </ul>
	 **/
	public static final Map<String, String[]> TYPES = Map.ofEntries(new AbstractMap.SimpleEntry<String, String[]>("TEL", TELTYPES),
			new AbstractMap.SimpleEntry<String, String[]>("EMAIL", EMAILTYPES), new AbstractMap.SimpleEntry<String, String[]>("ADR", ADRTYPES),
			new AbstractMap.SimpleEntry<String, String[]>("PHOTO", PHOTOTYPES));

}
