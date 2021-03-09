package aero.minova.test.saw.rcp.entries;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import aero.minova.test.saw.rcp.model.TextValue;
import aero.minova.test.saw.rcp.model.Value;

public class TextValueEntry extends ValueEntry {

	private Composite inputComp;
	private Text input;
	private ModifyListener inputModifyListener;

	public TextValueEntry(Composite body, DefaultPropertyEntry contactPropertyEntry, String property, boolean editable) {

		inputComp = new Composite(body, SWT.None);
		inputComp.setLayout(new GridLayout(5, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 10000;
		inputComp.setLayoutData(gd);

		input = new Text(inputComp, SWT.NONE);
		input.setMessage(property);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		input.setLayoutData(gd);
		input.setEditable(editable);
		inputModifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				contactPropertyEntry.addCTE();
				// input.setFocus();
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
		inputComp.moveAbove(seperator);
	}

	@Override
	protected void dispose() {
		input.dispose();
		inputComp.dispose();
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
