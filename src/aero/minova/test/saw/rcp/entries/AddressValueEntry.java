package aero.minova.test.saw.rcp.entries;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import aero.minova.test.saw.rcp.model.Address;
import aero.minova.test.saw.rcp.model.Value;

public class AddressValueEntry extends ValueEntry {

	private Address address;

	private Text street;
	private Text postCode;
	private Text city;
	private Text country;

	private List<Text> inputs;

	private Composite addrComp;

	private ModifyListener inputModifyListener;

	public AddressValueEntry(Composite body, PropertyEntry contactPropertyEntry, Boolean editable) {
		addrComp = new Composite(body, SWT.None);
		addrComp.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		addrComp.setLayoutData(gd);

		address = new Address(";;;;;;");

		street = new Text(addrComp, SWT.None);
		postCode = new Text(addrComp, SWT.None);
		city = new Text(addrComp, SWT.None);
		country = new Text(addrComp, SWT.None);

		street.setMessage("Straße und Hausnummer");
		postCode.setMessage("PLZ");
		city.setMessage("Stadt");
		country.setMessage("Land");

		inputs = new ArrayList<Text>();
		inputs.add(street);
		inputs.add(postCode);
		inputs.add(city);
		inputs.add(country);

		inputModifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (getEditable()) {
					contactPropertyEntry.addCTE();
					for (Text t : inputs) {
						t.removeModifyListener(this);
					}
				}
			}
		};

		for (Text t : inputs) {
			t.setEditable(editable);
			t.addModifyListener(inputModifyListener);
		}

		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 2;
		street.setLayoutData(gd);

		gd = new GridData(SWT.FILL, SWT.FILL, false, false);
		postCode.setLayoutData(gd);

		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		city.setLayoutData(gd);

		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 2;
		country.setLayoutData(gd);
	}

	@Override
	protected void removeModifyListener() {
		for (Text t : inputs) {
			t.removeModifyListener(inputModifyListener);
		}
	}

	@Override
	protected String getText() {
		return getValue().getStringRepresentation();
	}

	@Override
	protected void setText(Value value) {
		this.address = (Address) value;
		street.setText(address.getStreet());
		postCode.setText(address.getPostCode());
		city.setText(address.getCity());
		country.setText(address.getCountry());
	}

	@Override
	protected void setEditable(boolean editable) {
		for (Text t : inputs) {
			t.setEditable(editable);
		}
	}

	@Override
	protected void setVisible(boolean showInput) {
		for (Text t : inputs) {
			boolean vis = t.getEditable() || !t.getText().strip().equals("");

			GridData gd = (GridData) t.getLayoutData();
			gd.exclude = !vis;
			t.setVisible(vis);
		}
		addrComp.requestLayout();
	}

	@Override
	protected boolean getEditable() {
		return street.getEditable();
	}

	@Override
	protected void moveAbove(Label seperator) {
		addrComp.moveAbove(seperator);
	}

	@Override
	protected void dispose() {
		for (Text t : inputs) {
			t.dispose();
		}
		addrComp.dispose();
	}

	@Override
	protected Value getValue() {
		address.setStreet(street.getText());
		address.setPostCode(postCode.getText());
		address.setCity(city.getText());
		address.setCountry(country.getText());
		return address;
	}

}
