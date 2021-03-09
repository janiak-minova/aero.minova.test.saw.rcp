package aero.minova.test.saw.rcp.entries;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import aero.minova.test.saw.rcp.model.StructuredName;
import aero.minova.test.saw.rcp.model.Value;

public class NameValueEntry extends ValueEntry {

	private StructuredName sName;

	private Text completeName;
	private Text prefixText;
	private Text fnText;
	private Text snText;
	private Text lnText;
	private Text suffixText;

	private List<Text> inputs;

	private Composite nameComp;

	public NameValueEntry(Composite body) {
		nameComp = new Composite(body, SWT.None);
		nameComp.setLayout(new GridLayout(5, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		nameComp.setLayoutData(gd);

		sName = new StructuredName(";;;;");

		completeName = new Text(nameComp, SWT.None);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		completeName.setEditable(false);
		completeName.setLayoutData(gd);

		prefixText = new Text(nameComp, SWT.None);
		fnText = new Text(nameComp, SWT.None);
		snText = new Text(nameComp, SWT.None);
		lnText = new Text(nameComp, SWT.None);
		suffixText = new Text(nameComp, SWT.None);

		prefixText.setMessage("Titel");
		fnText.setMessage("Vorname");
		snText.setMessage("Zweiter Name");
		lnText.setMessage("Nachname");
		suffixText.setMessage("Namenszusatz");

		inputs = new ArrayList<Text>();
		inputs.add(prefixText);
		inputs.add(fnText);
		inputs.add(snText);
		inputs.add(lnText);
		inputs.add(suffixText);

		for (Text t : inputs) {
			gd = new GridData(SWT.FILL, SWT.FILL, true, false);
			t.setEditable(false);
			t.setLayoutData(gd);
		}
	}

	@Override
	protected void removeModifyListener() {}

	@Override
	protected String getText() {
		return getValue().getStringRepresentation();
	}

	@Override
	protected void setText(Value val) {
		this.sName = (StructuredName) val;
		completeName.setText(sName.getStringRepresentation());
		prefixText.setText(sName.getPrefix());
		fnText.setText(sName.getFirstName());
		snText.setText(sName.getSecondName());
		lnText.setText(sName.getLastName());
		suffixText.setText(sName.getSuffix());
	}

	@Override
	protected void setEditable(boolean editable) {
		for (Text t : inputs) {
			t.setEditable(editable);

			GridData gd = (GridData) t.getLayoutData();
			if (editable) {
				gd.grabExcessHorizontalSpace = true;
				gd.horizontalAlignment = SWT.FILL;
			} else {
				gd.grabExcessHorizontalSpace = false;
				gd.horizontalAlignment = SWT.LEFT;
				Point size = t.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				gd.widthHint = size.x;
			}
		}
		nameComp.requestLayout();
	}

	@Override
	protected void setVisible(boolean disregard) {
		boolean vis = fnText.getEditable();

		GridData gd = (GridData) completeName.getLayoutData();
		gd.exclude = vis;
		completeName.setVisible(!vis);

		for (Text t : inputs) {
			gd = (GridData) t.getLayoutData();
			gd.exclude = !vis;
			t.setVisible(vis);
		}
		nameComp.requestLayout();
	}

	@Override
	protected boolean getEditable() {
		return fnText.getEditable();
	}

	@Override
	protected void moveAbove(Label seperator) {
		nameComp.moveAbove(seperator);
	}

	@Override
	protected void dispose() {
		for (Text t : inputs) {
			t.dispose();
		}
		nameComp.dispose();
	}

	@Override
	protected Value getValue() {
		sName.setPrefix(prefixText.getText());
		sName.setFirstName(fnText.getText());
		sName.setSecondName(snText.getText());
		sName.setLastName(lnText.getText());
		sName.setSuffix(suffixText.getText());
		completeName.setText(sName.getStringRepresentation());
		return sName;
	}
}
