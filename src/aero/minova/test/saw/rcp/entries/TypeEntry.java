package aero.minova.test.saw.rcp.entries;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import aero.minova.test.saw.rcp.model.Value;
import aero.minova.test.saw.rcp.vCard.VCardOptions;

public class TypeEntry {

	private PropertyEntry parent;
	private final String property;
	private String type;

	private Label typeLabel;

	private Combo typeCombo;

	private ValueEntry input;

	Composite body;

	public TypeEntry(Composite body, PropertyEntry contactPropertyEntry, String property, String type, Boolean editable) {

		this.parent = contactPropertyEntry;
		this.property = property;
		this.body = body;

		// Feldname Label
		typeLabel = new Label(body, SWT.RIGHT);
		GridData gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		gd.widthHint = 50;
		typeLabel.setLayoutData(gd);

		// Combo für "bearbeiten" Ansicht
		typeCombo = new Combo(body, SWT.RIGHT);
		gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		gd.widthHint = 50;
		gd.exclude = true;
		typeCombo.setLayoutData(gd);
		typeCombo.setVisible(false);
		typeCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setType(typeCombo.getText());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

		// Input
		switch (property) {
		case (VCardOptions.NAME):
			input = new NameValueEntry(body);
			break;
		case (VCardOptions.ADR):
			input = new AddressValueEntry(body, contactPropertyEntry, editable);
			break;
		default:
			input = new TextValueEntry(body, contactPropertyEntry, property, editable);
		}

		setType(type);
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		String before = this.type;
		this.type = type;
		typeLabel.setText(type);
		if (type.equals(""))
			typeLabel.setText(property);

		parent.typeChanged(before);
	}

	public void setCombo(List<String> types) {
		typeCombo.removeAll();
		for (String t : types) {
			typeCombo.add(t);
		}
		typeCombo.select(types.indexOf(type));
		setEditable(input.getEditable());
	}

//	public String getInput() {
//		input.removeModifyListener();
//		return input.getText();
//	}

	public void setInput(Value value) {
		input.setText(value);
	}

	public void setEditable(boolean editable) {

		input.setEditable(editable);

		boolean multipleTypesAvailable = typeCombo.getItemCount() > 1;
		boolean showLabel = (!editable && hasContent()) || !multipleTypesAvailable;
		boolean showCombo = editable && multipleTypesAvailable;
		boolean showInput = editable || hasContent();

		GridData gd = (GridData) typeLabel.getLayoutData();
		gd.exclude = !showLabel;
		typeLabel.setVisible(showLabel);

		gd = (GridData) typeCombo.getLayoutData();
		gd.exclude = !showCombo;
		typeCombo.setVisible(showCombo);

		input.setVisible(showInput);

		parent.requestLayout();
	}

	public boolean hasContent() {
		return !input.getText().strip().equals("");
	}

	public void moveAbove(Label seperator) {
		typeLabel.moveAbove(seperator);
		typeCombo.moveAbove(seperator);
		input.moveAbove(seperator);
	}

	public void setVisible(Boolean vis) {
		GridData gd = (GridData) typeLabel.getLayoutData();
		gd.exclude = !vis;
		typeLabel.setVisible(vis);

		gd = (GridData) typeCombo.getLayoutData();
		gd.exclude = !vis;
		typeCombo.setVisible(vis);

		input.setVisible(vis);

		parent.requestLayout();
	}

	public void dispose() {
		typeLabel.dispose();
		typeCombo.dispose();
		input.dispose();
	}

	public Value getValue() {
		return input.getValue();
	}

	public Label getTypeLabel() {
		return typeLabel;
	}
}
