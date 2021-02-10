package aero.minova.test.saw.rcp.handlers;

import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;

import aero.minova.test.saw.rcp.model.Contact;

public class ContactColumnPropertyAccessor implements IColumnPropertyAccessor<Contact> {

	@Override
	public Object getDataValue(Contact c, int columnIndex) {
		switch (columnIndex) {
		case 0: return c.getFirstName();
		case 1: return c.getCompany();
		default: return "UNDEFINED";
		
		}
	}

	@Override
	public void setDataValue(Contact c, int columnIndex, Object newValue) {
		switch (columnIndex) {
			case 0: c.setFirstName(String.valueOf(newValue));
			case 1: c.setCompany(String.valueOf(newValue));
			default: throw new IllegalArgumentException("column number out of range");
		}
		
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public String getColumnProperty(int columnIndex) {
		// TODO Auto-generated method stub
		return "name?";
	}

	@Override
	public int getColumnIndex(String propertyName) {
		// TODO Auto-generated method stub
		return 0;
	}

}
