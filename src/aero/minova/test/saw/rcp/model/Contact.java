package aero.minova.test.saw.rcp.model;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import aero.minova.test.saw.rcp.vCard.VCardOptions;

/**
 * Jede natürliche Peron stellt einen Kontakt dar.
 * 
 * @author janiak
 */
public class Contact {

	private final int id;

	private Map<String, Map<String, Value>> properties;

	public Contact(int id) {
		this.id = id;
		properties = new LinkedHashMap<String, Map<String, Value>>();

//		for (String prop : VCardOptions.PROPERTIES) {
//			properties.put(prop, new LinkedHashMap<String, String>());
//		}
	}

	public void setProperty(String prop, String type, Value val) {
		if (Arrays.asList(VCardOptions.PROPERTIES).contains(prop)
				&& (VCardOptions.TYPES.get(prop) != null && Arrays.asList(VCardOptions.TYPES.get(prop)).contains(type) || type.equals(""))) {
			if (properties.get(prop) == null) {
				properties.put(prop, new LinkedHashMap<String, Value>());
			}

			properties.get(prop).put(type, val);
		} else {
			System.err.println("Property " + prop + " nicht unterstüzt oder Typ " + type + " nicht unterstüzt für Property " + prop);
		}
	}

	public void setProperty(String prop, Value val) {
		if (Arrays.asList(VCardOptions.PROPERTIES).contains(prop) && VCardOptions.TYPES.get(prop) == null) {
			if (properties.get(prop) == null) {
				properties.put(prop, new LinkedHashMap<String, Value>());
			}

			properties.get(prop).put("", val);
		} else {
			System.err.println("Property " + prop + " nicht unterstüzt oder muss einen Typ haben");
		}
	}

	public void setProperty(String prop, String val) {
		switch (prop) {
		case (VCardOptions.NAME):
			setProperty(prop, new StructuredName(val));
			break;
		case (VCardOptions.ADR):
			setProperty(prop, new Address(val));
			break;
		default:
			setProperty(prop, new TextValue(val));
		}
	}

	public void setProperty(String prop, String type, String val) {
		switch (prop) {
		case (VCardOptions.NAME):
			setProperty(prop, type, new StructuredName(val));
			break;
		case (VCardOptions.ADR):
			setProperty(prop, type, new Address(val));
			break;
		default:
			setProperty(prop, type, new TextValue(val));
		}
	}

	public String getValueString(String prop) {
		String val = "";
		if (properties.get(prop) != null && properties.get(prop).size() > 0) {
			val = properties.get(prop).entrySet().iterator().next().getValue().getStringRepresentation();
		}
		return val;
	}

	public String getValueString(String prop, String type) {
		String val = "";
		if (properties.get(prop) != null) {
			val = properties.get(prop).get(type).getStringRepresentation();
		}
		return val;
	}

	public Value getValue(String prop) {
		if (properties.get(prop) != null && properties.get(prop).size() > 0) {
			return properties.get(prop).entrySet().iterator().next().getValue();
		}
		return null;
	}

	public Value getValue(String prop, String type) {
		if (properties.get(prop) != null) {
			return properties.get(prop).get(type);
		}
		return null;
	}

	public Map<String, Value> getTypesAndValues(String prop) {
		return properties.get(prop);
	}

	public int getId() {
		return id;
	}

	public Set<String> getProperties() {
		return properties.keySet();
	}

}
