package aero.minova.test.saw.rcp.entries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.vCard.VCardOptions;

public class PropertyEntry {

	private Composite body;

	private final String property;

	private List<String> types;

	private List<TypeEntry> typeEntries;

	private Label seperator;

	private Contact currentContact;

	private boolean editable = false;

	public PropertyEntry(Composite body, String property) {

		this.body = body;
		this.property = property;
		typeEntries = new ArrayList<TypeEntry>();

		if (VCardOptions.TYPES.get(property) == null) {
			types = new ArrayList<>();
			types.add("");
		} else {
			types = Arrays.asList(VCardOptions.TYPES.get(property));
		}

		// Separator
		seperator = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 1000;
		seperator.setLayoutData(gd);
		seperator.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");
	}

	public TypeEntry addCTE(String type) {
		TypeEntry cte = new TypeEntry(body, this, property, type, editable);
		typeEntries.add(cte);
		cte.moveAbove(seperator);
		typeChanged(null);
		body.requestLayout();
		return cte;
	}

	public void addCTE() {
		if (getUnusedTypes().size() > 0)
			addCTE(getUnusedTypes().get(0));
	}

	public void setInput(Contact c) {
		currentContact = c;
		List<String> usedTypes = getUsedTypes();
		List<String> neededTypes = new ArrayList<String>();
		if (c.getTypesAndValues(property) != null) {
			for (String type : c.getTypesAndValues(property).keySet()) {
				neededTypes.add(type);
				TypeEntry cte = getTypeEntryByType(type);
				if (cte == null) {
					cte = addCTE(type);
				}
				cte.setInput(c.getTypesAndValues(property).get(type));
			}
		}
		for (String t : usedTypes) {
			if (!neededTypes.contains(t)) {
				TypeEntry cte = getTypeEntryByType(t);
				removeEntry(cte);
			}
		}
		setEditable(false);
	}

	public TypeEntry getTypeEntryByType(String type) {
		for (TypeEntry cte : typeEntries) {
			if (cte.getType().equals(type)) {
				return cte;
			}
		}
		return null;
	}

	public void updateContact() {
		for (TypeEntry cte : typeEntries) {
			if (cte.hasContent())
				currentContact.setProperty(property, cte.getType(), cte.getValue());
		}
	}

	public void setEditable(boolean editable) {
		this.editable = editable;

		if (editable && getUnusedTypes().size() > 0) {
			addCTE(getUnusedTypes().get(0));
		}

		if (!editable) {
			List<TypeEntry> remove = new ArrayList<TypeEntry>();
			for (TypeEntry cte : typeEntries) {
				if (!cte.hasContent())
					remove.add(cte);
			}
			for (TypeEntry cte : remove) {
				removeEntry(cte);
			}
		}

		boolean vis = false;
		for (TypeEntry cte : typeEntries) {
			cte.setEditable(editable);
			if (cte.hasContent())
				vis = true;
		}
		vis = vis || editable;

		GridData gd = (GridData) seperator.getLayoutData();
		gd.exclude = !vis;
		seperator.setVisible(vis);

		body.requestLayout();
	}

	public List<String> getUsedTypes() {
		List<String> usedTypes = new ArrayList<>();
		for (TypeEntry cte : typeEntries) {
			usedTypes.add(cte.getType());
		}
		return usedTypes;
	}

	public List<String> getUnusedTypes() {
		List<String> unusedTypes = new ArrayList<>(types);
		unusedTypes.removeAll(getUsedTypes());
		return unusedTypes;
	}

	public void setCombo(TypeEntry cte) {
		List<String> availableTypes = getUnusedTypes();
		if (cte.getType() != null)
			availableTypes.add(0, cte.getType());
		cte.setCombo(availableTypes);
	}

	public void typeChanged(String before) {
		if (before != null && currentContact.getTypesAndValues(property) != null)
			currentContact.getTypesAndValues(property).remove(before);
		for (TypeEntry cte : typeEntries) {
			setCombo(cte);
		}
	}

	public void requestLayout() {
		body.requestLayout();
	}

	public void removeEntry(TypeEntry cte) {
		cte.dispose();
		typeEntries.remove(cte);
	}

}
