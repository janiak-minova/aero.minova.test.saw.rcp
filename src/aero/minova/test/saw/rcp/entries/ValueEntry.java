package aero.minova.test.saw.rcp.entries;

import org.eclipse.swt.widgets.Label;

import aero.minova.test.saw.rcp.model.Value;

public abstract class ValueEntry {

	protected abstract void removeModifyListener();

	protected abstract String getText();

	protected abstract void setText(Value value);

	protected abstract void setEditable(boolean editable);

	protected abstract void setVisible(boolean showInput);

	protected abstract boolean getEditable();

	protected abstract void moveAbove(Label seperator);

	protected abstract void dispose();

	protected abstract Value getValue();

}
