package aero.minova.test.saw.rcp.entries;

import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import aero.minova.test.saw.rcp.model.Value;
import aero.minova.test.saw.rcp.vCard.VCardOptions;

public class TypeEntry {

	private PropertyEntry parent;
	private final String property;
	private String type;

	private Label deleteIcon;

	private Label typeLabel;

	private Combo typeCombo;

	private ValueEntry input;

	Composite body;

	public TypeEntry(Composite body, PropertyEntry contactPropertyEntry, String property, String type, Boolean editable) {

		this.parent = contactPropertyEntry;
		this.property = property;
		this.body = body;

		// Eintrag löschen icon
		deleteIcon = new Label(body, SWT.RIGHT);
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.widthHint = 15;
		deleteIcon.setLayoutData(gd);
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL url = FileLocator.find(bundle, new Path("icons/delete.png"), null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
		Image image = imageDesc.createImage();
		deleteIcon.setImage(image);
		deleteIcon.setSize(5, 5);
		deleteIcon.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {}

			@Override
			public void mouseUp(MouseEvent e) {}

			@Override
			public void mouseDown(MouseEvent e) {
				deleteEntry();
			}
		});

		// Feldname Label
		typeLabel = new Label(body, SWT.RIGHT);
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.widthHint = 60;
		typeLabel.setLayoutData(gd);

		// Combo für "bearbeiten" Ansicht
		typeCombo = new Combo(body, SWT.RIGHT);
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.exclude = true;
		gd.widthHint = 60;
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
		case (VCardOptions.BDAY):
			input = new DateValueEntry(body);
			break;
		default:
			input = new TextValueEntry(body, contactPropertyEntry, property, editable);
		}

		setType(type);
	}

	protected void deleteEntry() {
		parent.deleteEntry(type, this);
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

	public void setInput(Value value) {
		input.setText(value);
	}

	public void setEditable(boolean editable) {

		input.setEditable(editable);

		boolean multipleTypesAvailable = typeCombo.getItemCount() > 1;
		boolean showDelete = editable && hasContent() && !type.equals("");
		boolean showLabel = (!editable && hasContent()) || !multipleTypesAvailable;
		boolean showCombo = editable && multipleTypesAvailable;
		boolean showInput = editable || hasContent();

		deleteIcon.setVisible(showDelete);

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
		deleteIcon.moveAbove(seperator);
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
		deleteIcon.dispose();
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
