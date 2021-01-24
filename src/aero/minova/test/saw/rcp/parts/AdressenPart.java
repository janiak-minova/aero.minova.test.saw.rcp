
package aero.minova.test.saw.rcp.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class AdressenPart {
	private Label headLabel;
	private Text headText;
	private Label companyLabel;
	private Text companyText;

	@Inject
	public AdressenPart() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		GridData gd;
		Composite body = new Composite(parent, SWT.None);

		// Layout für Body definieren
		body.setLayout(new GridLayout(2, false));

		// Kopf Label und Feld definieren
		headLabel = new Label(body, SWT.RIGHT);
		headLabel.setText("Matchcode");
		headText = new Text(body, SWT.BORDER | SWT.ICON_SEARCH | SWT.ICON_CANCEL | SWT.SEARCH);
		headText.setText("Wilfried Saak");
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		headText.setLayoutData(gd);

		// Separator
		Label separatorLabel = new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.horizontalSpan = 2; // Beide Spalten verwenden
		gd.widthHint = 500;
		separatorLabel.setLayoutData(gd);
		separatorLabel.setText("lk");
		separatorLabel.setData("org.eclipse.e4.ui.css.CssClassName", "hrule");

		// Weiteres Feld definieren
		companyLabel = new Label(body, SWT.RIGHT);
		companyLabel.setText("Matchcode");
		companyText = new Text(body, SWT.BORDER | SWT.ICON_SEARCH | SWT.ICON_CANCEL | SWT.SEARCH);
		companyText.setText("MINOVA Information Srvices GmbH");
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd.widthHint = 300;
		companyText.setLayoutData(gd);

	}

}