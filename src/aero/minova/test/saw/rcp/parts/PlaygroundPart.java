package aero.minova.test.saw.rcp.parts;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class PlaygroundPart {

	private String message;
	private static final int MARGIN = 2;
	private int verticalOffsetLines;
	private Listener invalidateListener;
	private Color textColor;

	Text text;

	private static int[] INVALIDATE_EVENTS = { SWT.Activate, SWT.Deactivate, SWT.Show, SWT.Hide };

	@PostConstruct
	public void postConstruct(Composite parent) {

		parent.setLayout(new RowLayout(SWT.VERTICAL));

		Text text = new Text(parent, SWT.BORDER | SWT.MULTI);
		text.setMessage("1");

		Text text2 = new Text(parent, SWT.BORDER | SWT.MULTI);
		text2.setMessage("1342345");

		newHintMessage(text, "this is a message");

		newHintMessage(text2, "another message");

//		FormData textFormData = new FormData();
//		textFormData.top = new FormAttachment(parent, 10);
//		textFormData.left = new FormAttachment(parent, 10);
//		textFormData.width = 500;
//		textFormData.height = 300;
//
//		text.setLayoutData(textFormData);
	}

	public void newHintMessage(Text text, String message) {
		this.text = Objects.requireNonNull(text);
		this.invalidateListener = this::handleInvalidatedEvent;
		this.textColor = getTextColor();
		this.message = message;
		initialize();
	}

	private Color getTextColor() {
		return text.getDisplay().getSystemColor(SWT.COLOR_GRAY);
	}

	private void initialize() {
		text.addListener(SWT.Paint, this::handlePaintEvent);
		text.addListener(SWT.Resize, event -> text.redraw());
		text.addListener(SWT.Dispose, this::handleDispose);

	}

	public boolean isMessageShowing() {
		return !message.isEmpty() && text.getText().length() == 0;
	}

	private void drawHint(GC gc, int x, int y) {
		int verticalOffset = verticalOffsetLines * gc.getFontMetrics().getHeight();
		gc.setForeground(textColor);
		gc.drawText(message, x + MARGIN, y + MARGIN - verticalOffset, SWT.DRAW_DELIMITER | SWT.DRAW_TRANSPARENT);
	}

	private void handlePaintEvent(Event event) {
		if (isMessageShowing()) {
			drawHint(event.gc, event.x, event.y);
		}
	}

	private void handleInvalidatedEvent(Event event) {
		text.redraw();
	}

	private void handleDispose(Event event) {
		for (int eventType : INVALIDATE_EVENTS) {
			text.getDisplay().removeFilter(eventType, invalidateListener);
		}
	}
}
