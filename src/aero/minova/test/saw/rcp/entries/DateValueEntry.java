package aero.minova.test.saw.rcp.entries;

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import aero.minova.test.saw.rcp.constants.Constants;
import aero.minova.test.saw.rcp.model.DateValue;
import aero.minova.test.saw.rcp.model.Value;

public class DateValueEntry extends ValueEntry {

	private DateValue dateValue;

	private Text input;

	private Composite dateComp;

	SimpleDateFormat format;

	public DateValueEntry(Composite body) {
		format = new SimpleDateFormat(Constants.DATEFORMAT);
		dateValue = new DateValue("");

		dateComp = new Composite(body, SWT.None);
		dateComp.setLayout(new GridLayout(5, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		dateComp.setLayoutData(gd);

		input = new Text(dateComp, SWT.NONE);
		input.setMessage(Constants.DATEFORMAT);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		input.setLayoutData(gd);
		input.setEditable(false);
	}

	@Override
	protected void removeModifyListener() {}

	@Override
	protected String getText() {
		return input.getText();
	}

	@Override
	protected void setText(Value value) {
		dateValue = (DateValue) value;
		if (dateValue.getDate() != null)
			input.setText(format.format(dateValue.getDate()));
		else
			input.setText("");
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
		dateComp.moveAbove(seperator);
	}

	@Override
	protected void dispose() {
		input.dispose();
		dateComp.dispose();
	}

	@Override
	protected Value getValue() {
		dateValue = new DateValue(input.getText());
		if (dateValue.getDate() == null)
			input.setText("");
		return dateValue;
	}
}
