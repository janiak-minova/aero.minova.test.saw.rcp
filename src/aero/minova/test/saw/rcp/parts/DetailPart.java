package aero.minova.test.saw.rcp.parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.nebula.widgets.opal.textassist.TextAssist;
import org.eclipse.nebula.widgets.opal.textassist.TextAssistContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import aero.minova.test.saw.rcp.handlers.TraverseListenerImpl;

@SuppressWarnings("restriction")
public class DetailPart {

	private static final int COLUMN_WIDTH = 110;
	private static final int TEXT_WIDTH = COLUMN_WIDTH;
	private static final int MARGIN_HORIZONTAL = 5;
	private static final int MARGIN_VERTICAL = 0;
	private static final int SECTION_WIDTH = 4 * COLUMN_WIDTH + 5 * MARGIN_HORIZONTAL; // 4 Spalten = 5 Zwischenräume
	private static final int COLUMN_HEIGHT = 25;

	@Inject
	private IEventBroker eventBroker;

	@Inject
	Logger logger;

	@Inject
	private MPart part;
	private FormToolkit toolkit;
	private Label page2Description1;
	private Label page2Label1;
	private TextAssist page2Lookup1;
	private ArrayList<String> x = new ArrayList<>();

	public DetailPart() {
		x.add("WERT1");
		x.add("WERT2");
		x.add("ETWASANDERES");
		x.add("ETMON");
		Collections.sort(x, (x1, x2) -> x1.compareTo(x2));
	}

	@PostConstruct
	public void createComposite(Composite parent) {
		TraverseListener traverseListener = new TraverseListenerImpl(logger);
		toolkit = new FormToolkit(parent.getDisplay());
		parent.setLayout(new RowLayout(SWT.VERTICAL));

		Section head = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
		head.setText("Kopf");
		head.addTraverseListener(traverseListener);

		Composite composite = toolkit.createComposite(head);
		composite.setLayout(new FormLayout());
		toolkit.paintBordersFor(composite);
		head.setClient(composite);

		createHead(composite, traverseListener);

		////////// PAGE 2

		Section page2 = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
		page2.setText("Option Page 1");
		page2.addTraverseListener(traverseListener);

		composite = toolkit.createComposite(page2);
		composite.setLayout(new FormLayout());
		toolkit.paintBordersFor(composite);
		page2.setClient(composite);
		createPage1(composite, traverseListener);

		////////// PAGE 1

		Section page1 = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
		page1.setText("Option Page 2");
		RowData page1Data = new RowData();
		page1.setLayoutData(page1Data);
		page1.addTraverseListener(traverseListener);
		page1.setLayout(new GridLayout());

		composite = toolkit.createComposite(page1);
		composite.setLayout(new FormLayout());
		toolkit.paintBordersFor(composite);
		page1.setClient(composite);
		createPage2(composite, traverseListener);

	}

	private void createPage1(Composite composite, TraverseListener traverseListener) {
		int line = 0;
		//// ZEILE 1

//		Text headText11 = toolkit.createText(composite, "", SWT.BORDER | SWT.FILL);
		Text headText11 = toolkit.createText(composite, "", SWT.NONE);
		FormData headText11Data = new FormData();
		headText11Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		headText11Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		headText11.setMessage("01.01.2020");
		headText11.setText("01.01.2020");
		headText11.setLayoutData(headText11Data);
		headText11.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				headText11.pack();
				eventBroker.send(StatusBar.STATUSBAR, "headText11: " + headText11.getSize());
			}
		});
		headText11.addTraverseListener(traverseListener);

		Label headLabel11 = toolkit.createLabel(composite, "Label 1/1", SWT.RIGHT);
		FormData headLabel11Data = new FormData();
		headLabel11Data.width = COLUMN_WIDTH;
		headLabel11Data.top = new FormAttachment(headText11, 0, SWT.CENTER);
		headLabel11Data.right = new FormAttachment(headText11, MARGIN_HORIZONTAL * -1, SWT.LEFT);
		headLabel11.setLayoutData(headLabel11Data);
		headLabel11.addTraverseListener(traverseListener);

		//// ZEILE 2
		line++;

		Text headText21 = toolkit.createText(composite, "", SWT.BORDER);
		FormData headText21Data = new FormData();
		headText21Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		headText21Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		headText21.setMessage("23:59");
		headText21.setText("23:59");
		headText21.setLayoutData(headText21Data);
		headText21.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				headText21.pack();
				eventBroker.send(StatusBar.STATUSBAR, "headText21: " + headText21.getSize());
			}
		});
		headText21.addTraverseListener(traverseListener);

		Label headLabel21 = toolkit.createLabel(composite, "Label 2/1", SWT.RIGHT);
		FormData headLabel21Data = new FormData();
		headLabel21Data.width = COLUMN_WIDTH;
		headLabel21Data.top = new FormAttachment(headText21, 0, SWT.CENTER);
		headLabel21Data.right = new FormAttachment(headText21, MARGIN_HORIZONTAL * -1, SWT.LEFT);
		headLabel21.setLayoutData(headLabel21Data);
		headLabel21.addTraverseListener(traverseListener);

		//// ZEILE 3
		line++;

		Button button31 = toolkit.createButton(composite, "Button 3/1", SWT.CHECK);
		FormData headButton31Data = new FormData();
		headButton31Data.width = COLUMN_WIDTH;
		headButton31Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		headButton31Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		button31.setLayoutData(headButton31Data);
		button31.addTraverseListener(traverseListener);

		//// ZEILE 4
		line++;

		Text headText41 = toolkit.createText(composite, "999.999.999", SWT.BORDER | SWT.RIGHT);
		FormData headText41Data = new FormData();
		headText41Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		headText41Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		headText41.setMessage("999.999.999");
		headText41.setLayoutData(headText41Data);
		headText41.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				headText41.pack();
				eventBroker.send(StatusBar.STATUSBAR, "headText41: " + ((Control) e.widget).getSize());
			}
		});
		headText41.addTraverseListener(traverseListener);

		Label headLabel41 = toolkit.createLabel(composite, "Label 4/1", SWT.RIGHT);
		FormData headLabel41Data = new FormData();
		headLabel41Data.width = COLUMN_WIDTH;
		headLabel41Data.top = new FormAttachment(headText41, 0, SWT.CENTER);
		headLabel41Data.right = new FormAttachment(headText41, -1 * MARGIN_HORIZONTAL, SWT.LEFT);
		headLabel41.setLayoutData(headLabel41Data);
		headLabel41.addTraverseListener(traverseListener);

		//// Ende
		line++;
		Composite bottomComposite = new Composite(composite, SWT.None);
		FormData bottomCompositeData = new FormData();
		bottomCompositeData.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		bottomCompositeData.left = new FormAttachment(composite, 0);
		bottomCompositeData.width = SECTION_WIDTH;
		bottomCompositeData.height = MARGIN_VERTICAL;
		bottomComposite.setLayoutData(bottomCompositeData);
	}

	private void createHead(Composite composite, TraverseListener traverseListener) {
		int line = 0;
		int column;
		//// ZEILE 1

		Text matchcodeText = toolkit.createText(composite, "IDENTITY", SWT.BORDER | SWT.FILL);
		FormData matchcodeTextData = new FormData();
		matchcodeTextData.width = TEXT_WIDTH;
		matchcodeTextData.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		matchcodeTextData.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		matchcodeText.setLayoutData(matchcodeTextData);
		matchcodeText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				logger.debug("Matchcode: " + matchcodeText.getSize());
			}
		});

		Label matchcodeLabel = toolkit.createLabel(composite, "Matchcode", SWT.RIGHT);
		FormData matchcodeLabelData = new FormData();
		matchcodeLabelData.width = COLUMN_WIDTH;
		matchcodeLabelData.top = new FormAttachment(matchcodeText, 0, SWT.CENTER);
		matchcodeLabelData.right = new FormAttachment(matchcodeText, MARGIN_HORIZONTAL * -1, SWT.LEFT);
		matchcodeLabel.setLayoutData(matchcodeLabelData);

		//// ZEILE 2
		line++;

		Text descriptionText = toolkit.createText(composite, "Description for Lookup", SWT.BORDER);
		FormData descriptionTextData = new FormData();
		descriptionTextData.width = 3 * COLUMN_WIDTH + 2 * MARGIN_HORIZONTAL;
		descriptionTextData.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		descriptionTextData.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		descriptionText.setLayoutData(descriptionTextData);
		descriptionText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				String message = "Description: " + descriptionText.getSize();
				logger.debug(message);
				eventBroker.send(StatusBar.STATUSBAR, message);
			}
		});

		Label descriptionLabel = toolkit.createLabel(composite, "Description", SWT.RIGHT);
		FormData descriptionLabelData = new FormData();
		descriptionLabelData.width = COLUMN_WIDTH;
		descriptionLabelData.top = new FormAttachment(descriptionText, 0, SWT.CENTER);
		descriptionLabelData.right = new FormAttachment(descriptionText, MARGIN_HORIZONTAL * -1, SWT.LEFT);
		descriptionLabel.setLayoutData(descriptionLabelData);

		//// ZEILE 3
		line++;
		column = 1;

		Text headText41 = toolkit.createText(composite, "01.01.2021", SWT.BORDER);
		FormData headText41Data = new FormData();
		headText41Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		headText41Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		headText41.setLayoutData(headText41Data);
		headText41.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println(headText41.getSize());
				eventBroker.send(StatusBar.STATUSBAR, "Hallo Head Text 4	1");
			}
		});
		headText41.setMessage("01.01.2000");

		Label headLabel41 = toolkit.createLabel(composite, "Gültig von", SWT.RIGHT);
		FormData headLabel41Data = new FormData();
		headLabel41Data.width = COLUMN_WIDTH;
		headLabel41Data.top = new FormAttachment(headText41, 0, SWT.CENTER);
		headLabel41Data.right = new FormAttachment(headText41, -1 * MARGIN_HORIZONTAL, SWT.LEFT);
		headLabel41.setLayoutData(headLabel41Data);

		column++;

		Text headText42 = toolkit.createText(composite, "31.12.2021", SWT.BORDER | SWT.RIGHT);
		FormData headText42Data = new FormData();
		headText42Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		headText42Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * (column + 1) + (column + 1) * COLUMN_WIDTH);
		headText42.setLayoutData(headText42Data);
		headText42.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println(headText42.getSize());
			}
		});
		headText42.setMessage("01.01.2000");

		Label headLabel42 = toolkit.createLabel(composite, "Gültig bis", SWT.RIGHT);
		FormData headLabel42Data = new FormData();
		headLabel42Data.width = COLUMN_WIDTH;
		headLabel42Data.top = new FormAttachment(headText42, 0, SWT.CENTER);
		headLabel42Data.right = new FormAttachment(headText42, -1 * MARGIN_HORIZONTAL, SWT.LEFT);
		headLabel42.setLayoutData(headLabel42Data);

		//// Ende
		line++;
		Composite bottomComposite = new Composite(composite, SWT.None);
		FormData bottomCompositeData = new FormData();
		bottomCompositeData.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		bottomCompositeData.left = new FormAttachment(composite, 0);
		bottomCompositeData.width = SECTION_WIDTH;
		bottomCompositeData.height = MARGIN_VERTICAL;
		bottomComposite.setLayoutData(bottomCompositeData);
	}

	private void createPage2(Composite composite, TraverseListener traverseListener) {
		int line = 0;
		int column;
		//// ZEILE 1

		TextAssistContentProvider page2Lookup1ContentProvider = new TextAssistContentProvider() {

			@Override
			public List<String> getContent(String entry) {
				System.out.println("getContent(" + entry + ")");
				return x.stream().filter(new Predicate<String>() {

					@Override
					public boolean test(String t) {
						return t.toUpperCase().contains(entry.toUpperCase());
					}
				}).collect(Collectors.toList());
			}

		};
		page2Lookup1 = new TextAssist(composite, SWT.BORDER, page2Lookup1ContentProvider);
		page2Lookup1.setText("LOOKUP");
//		page2Lookup1.setUseSingleClick(true);
		FormData page2Lookup1Data = new FormData();
		page2Lookup1Data.width = COLUMN_WIDTH;
		page2Lookup1Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		page2Lookup1Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		page2Lookup1.setLayoutData(page2Lookup1Data);
		page2Lookup1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println(page2Lookup1.getSize());
			}
		});

		page2Label1 = toolkit.createLabel(composite, "Lookup 1", SWT.RIGHT);
		FormData page2Label1Data = new FormData();
		page2Label1Data.width = COLUMN_WIDTH;
		page2Label1Data.top = new FormAttachment(page2Lookup1, 0, SWT.CENTER);
		page2Label1Data.right = new FormAttachment(page2Lookup1, MARGIN_HORIZONTAL * -1, SWT.LEFT);
		page2Label1.setLayoutData(page2Label1Data);

		page2Description1 = toolkit.createLabel(composite, "Dies ist ein besonders langer Text, damit es hilft", SWT.RIGHT);
		FormData page2Description1Data = new FormData();
		page2Description1Data.width = COLUMN_WIDTH * 2 + MARGIN_HORIZONTAL * 2;
		page2Description1Data.top = new FormAttachment(page2Lookup1, 0, SWT.CENTER);
		page2Description1Data.left = new FormAttachment(page2Lookup1, 0, SWT.RIGHT);
//		headLabelDescription11Data.right = new FormAttachment(composite, MARGIN_LEFT * -1, SWT.RIGHT);
		page2Description1.setLayoutData(page2Description1Data);

		//// ZEILE 2
		line++;
		column = 0;

		Text headText21 = toolkit.createText(composite, "23:59", SWT.BORDER);
		FormData headText21Data = new FormData();
//		headText21Data.width = COLUMN_WIDTH;
		headText21Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		headText21Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		headText21.setLayoutData(headText21Data);
		headText21.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println(headText21.getSize());
			}
		});

		Label headLabel21 = toolkit.createLabel(composite, "Lookup 2", SWT.RIGHT);
		FormData headLabel21Data = new FormData();
		headLabel21Data.width = COLUMN_WIDTH;
		headLabel21Data.top = new FormAttachment(headText21, 0, SWT.CENTER);
		headLabel21Data.right = new FormAttachment(headText21, MARGIN_HORIZONTAL * -1, SWT.LEFT);
		headLabel21.setLayoutData(headLabel21Data);

		//// ZEILE 3
		line++;
		column = 0;

		Button button31 = toolkit.createButton(composite, "&Speichern", SWT.CHECK);
		FormData headButton31Data = new FormData();
		headButton31Data.width = COLUMN_WIDTH;
		headButton31Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		headButton31Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		button31.setLayoutData(headButton31Data);

		//// ZEILE 4
		line++;
		column = 1;

		Text headText41 = toolkit.createText(composite, "999.999.999", SWT.BORDER);
		FormData headText41Data = new FormData();
		headText41Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		headText41Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * 2 + COLUMN_WIDTH);
		headText41.setLayoutData(headText41Data);
		headText41.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println(headText41.getSize());
			}
		});

		Label headLabel41 = toolkit.createLabel(composite, "&Label 4 - 1", SWT.RIGHT);
		FormData headLabel41Data = new FormData();
		headLabel41Data.width = COLUMN_WIDTH;
		headLabel41Data.top = new FormAttachment(headText41, 0, SWT.CENTER);
		headLabel41Data.right = new FormAttachment(headText41, -1 * MARGIN_HORIZONTAL, SWT.LEFT);
		headLabel41.setLayoutData(headLabel41Data);

		Label headUnitLabel41 = toolkit.createLabel(composite, "kg", SWT.LEFT);
		FormData headUnitLabel41Data = new FormData();
		headUnitLabel41Data.top = new FormAttachment(headText41, 0, SWT.CENTER);
		headUnitLabel41Data.left = new FormAttachment(headText41);
		headUnitLabel41Data.right = new FormAttachment(composite, 3 * MARGIN_HORIZONTAL + 2 * COLUMN_WIDTH, SWT.LEFT);
		headUnitLabel41.setLayoutData(headUnitLabel41Data);

		column++;

		Text headText42 = toolkit.createText(composite, "999.999.999", SWT.BORDER | SWT.RIGHT);
		FormData headText42Data = new FormData();
		headText42Data.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		headText42Data.left = new FormAttachment(composite, MARGIN_HORIZONTAL * (column + 1) + (column + 1) * COLUMN_WIDTH);
		headText42.setLayoutData(headText42Data);
		headText42.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println(headText42.getSize());
			}
		});
		headText42.setMessage("9.999,99");

		Label headLabel42 = toolkit.createLabel(composite, "&Label 4 - 2", SWT.RIGHT);
		FormData headLabel42Data = new FormData();
		headLabel42Data.width = COLUMN_WIDTH;
		headLabel42Data.top = new FormAttachment(headText42, 0, SWT.CENTER);
		headLabel42Data.right = new FormAttachment(headText42, -1 * MARGIN_HORIZONTAL, SWT.LEFT);
		headLabel42.setLayoutData(headLabel42Data);

		Label headUnitLabel42 = toolkit.createLabel(composite, "cbm", SWT.LEFT);
		FormData headUnitLabel42Data = new FormData();
		headUnitLabel42Data.top = new FormAttachment(headText42, 0, SWT.CENTER);
		headUnitLabel42Data.left = new FormAttachment(headText42);
		headUnitLabel42Data.right = new FormAttachment(composite, (column * 2 + 1) * MARGIN_HORIZONTAL + column * 2 * COLUMN_WIDTH, SWT.LEFT);
		headUnitLabel42.setLayoutData(headUnitLabel42Data);

		//// Ende
		line++;
		Composite bottomComposite = new Composite(composite, SWT.None);
		FormData bottomCompositeData = new FormData();
		bottomCompositeData.top = new FormAttachment(composite, MARGIN_VERTICAL + line * COLUMN_HEIGHT);
		bottomCompositeData.left = new FormAttachment(composite, 0);
		bottomCompositeData.width = SECTION_WIDTH;
		bottomCompositeData.height = MARGIN_VERTICAL;
		bottomComposite.setLayoutData(bottomCompositeData);
	}

	@Focus
	public void setFocus() {
		ArrayList<String> test = new ArrayList<>();
		test.add("sdsd");
	}

	@Persist
	public void save() {
		part.setDirty(false);
	}
}