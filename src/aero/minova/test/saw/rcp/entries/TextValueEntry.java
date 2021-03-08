package aero.minova.test.saw.rcp.entries;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import aero.minova.test.saw.rcp.model.TextValue;
import aero.minova.test.saw.rcp.model.Value;

public class TextValueEntry extends ValueEntry {

	private Text input;
	private ModifyListener inputModifyListener;

	public TextValueEntry(Composite body, PropertyEntry contactPropertyEntry, String property, boolean editable) {

		input = new Text(body, SWT.NONE);
		input.setMessage(property);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		input.setLayoutData(gd);
		input.setEditable(editable);
		inputModifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				contactPropertyEntry.addCTE();
				input.removeModifyListener(this);
			}
		};
		input.addModifyListener(inputModifyListener);

	}

	@Override
	protected void removeModifyListener() {
		input.removeModifyListener(inputModifyListener);
	}

	@Override
	protected String getText() {
		return input.getText();
	}

	@Override
	protected void setEditable(boolean editable) {
		input.setEditable(editable);
	}

	@Override
	protected void setVisible(boolean showInput) {
		GridData gd = (GridData) input.getLayoutData();
		gd.exclude = !showInput;
		input.setVisible(showInput);
	}

	@Override
	protected boolean getEditable() {
		return input.getEditable();
	}

	@Override
	protected void moveAbove(Label seperator) {
		input.moveAbove(seperator);
	}

	@Override
	protected void dispose() {
		input.dispose();
	}

	@Override
	protected Value getValue() {
		return new TextValue(input.getText());
	}

	@Override
	protected void setText(Value value) {
		input.setText(value.getStringRepresentation());

	}
}
