package aero.minova.test.saw.rcp.model;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ContactTypeEntry {

	private ContactPropertyEntry parent;
	private final String property;
	private String type;

	private Label typeLabel;

	private Combo typeCombo;

	private Text input;
	private ModifyListener inputModifyListener;

	public ContactTypeEntry(Composite body, ContactPropertyEntry contactPropertyEntry, String property, String type, Boolean editable) {

		this.parent = contactPropertyEntry;
		this.property = property;

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
		// TODO: change to own class (with Interface?)
		input = new Text(body, SWT.NONE);
		input.setMessage(property);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		input.setLayoutData(gd);
		input.setEditable(editable);
		inputModifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				parent.addCTE();
				input.removeModifyListener(this);
			}
		};
		input.addModifyListener(inputModifyListener);

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

	public String getInput() {
		input.removeModifyListener(inputModifyListener);
		return input.getText();
	}

	public void setInput(String inputString) {
		input.setText(inputString);
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

		gd = (GridData) input.getLayoutData();
		gd.exclude = !showInput;
		input.setVisible(showInput);
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

		gd = (GridData) input.getLayoutData();
		gd.exclude = !vis;
		input.setVisible(vis);

		parent.requestLayout();
	}

	public void dispose() {
		typeLabel.dispose();
		typeCombo.dispose();
		input.dispose();
	}
}
