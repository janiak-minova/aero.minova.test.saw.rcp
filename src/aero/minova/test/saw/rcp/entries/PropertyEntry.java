package aero.minova.test.saw.rcp.entries;

import aero.minova.test.saw.rcp.model.Contact;

public abstract class PropertyEntry {

	public abstract void setInput(Contact c);

	public abstract void setEditable(boolean editable);

	public abstract TypeEntry getTypeEntryByType(String string);

	public abstract void updateContact();

}
