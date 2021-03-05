package aero.minova.test.saw.rcp.model;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import aero.minova.test.saw.rcp.vCard.VCardOptions;

/**
 * Jede natürliche Peron stellt einen Kontakt dar.
 * 
 * @author saak
 */
public class Contact {

	private final int id;

	private Map<String, Map<String, String>> properties;

	public Contact(int id, String name) {
		this(id);

		properties.put(VCardOptions.NAME, new LinkedHashMap<String, String>());
		properties.get(VCardOptions.NAME).put("", name);
	}

	public Contact(int id) {
		this.id = id;
		properties = new LinkedHashMap<String, Map<String, String>>();

//		for (String prop : VCardOptions.PROPERTIES) {
//			properties.put(prop, new LinkedHashMap<String, String>());
//		}
	}

	public void setProperty(String prop, String type, String val) {
		if (Arrays.asList(VCardOptions.PROPERTIES).contains(prop)
				&& (VCardOptions.TYPES.get(prop) != null && Arrays.asList(VCardOptions.TYPES.get(prop)).contains(type) || type.equals(""))) {
			if (properties.get(prop) == null) {
				properties.put(prop, new LinkedHashMap<String, String>());
			}

			properties.get(prop).put(type, val);
		} else {
			System.err.println("Property " + prop + " nicht unterstüzt oder Typ " + type + " nicht unterstüzt für Property " + prop);
		}
	}

	public void setProperty(String prop, String val) {
		if (Arrays.asList(VCardOptions.PROPERTIES).contains(prop) && VCardOptions.TYPES.get(prop) == null) {
			if (properties.get(prop) == null) {
				properties.put(prop, new LinkedHashMap<String, String>());
			}

			properties.get(prop).put("", val);
		} else {
			System.err.println("Property " + prop + " nicht unterstüzt oder muss einen Typ haben");
		}
	}

	public String getValue(String prop) {
		String val = "";
		if (properties.get(prop) != null) {
			val = properties.get(prop).entrySet().iterator().next().getValue();
		}
		return val;
	}

	public String getValue(String prop, String type) {
		String val = "";
		if (properties.get(prop) != null) {
			val = properties.get(prop).get(type);
		}
		return val;
	}

	public Map<String, String> getTypesAndValues(String prop) {
		return properties.get(prop);
	}

	public int getId() {
		return id;
	}

	public Set<String> getProperties() {
		return properties.keySet();
	}

}
