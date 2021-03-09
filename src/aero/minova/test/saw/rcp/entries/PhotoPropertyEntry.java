package aero.minova.test.saw.rcp.entries;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.PhotoValue;
import aero.minova.test.saw.rcp.vCard.VCardOptions;

public class PhotoPropertyEntry extends PropertyEntry {

	private Contact currentContact;

	private Label label;

	private boolean editable;

	private String defaultPath = "icons/user.png";

	private String path;

	public PhotoPropertyEntry(Composite body) {
		label = new Label(body, SWT.RIGHT);
		GridData gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		addProfilePic();
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				editProfilePic();
			}
		});
	}

	private void addProfilePic() {
		if (path == null || path.equals(""))
			path = defaultPath;

		// Hole Bild (aus lokalem Ordner oder durch globalen Pfad)
		Image image;
		if (path.contains("icons")) {
			Bundle bundle = FrameworkUtil.getBundle(getClass());
			URL url = FileLocator.find(bundle, new Path(path), null);
			ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
			image = imageDesc.createImage();
		} else {
			image = new Image(null, path);
		}

		// Scaliere Bild auf 50x50px
		Image scaled = new Image(Display.getDefault(), 50, 50);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, 50, 50);
		gc.dispose();
		image.dispose();

		label.setImage(scaled);
	}

	private void editProfilePic() {
		if (editable) {
			FileDialog dialog = new FileDialog(new Shell(), SWT.OPEN);
			// Weitere Möglichkeiten: { "*.png", "*.gif", "*.bmp", "*.jpg", "*.tiff" });
			dialog.setFilterExtensions(new String[] { "*.png" });
			path = dialog.open();
			addProfilePic();
		}
	}

	@Override
	public void setInput(Contact c) {
		currentContact = c;
		if (c.getValue(VCardOptions.PHOTO) != null)
			path = c.getValue(VCardOptions.PHOTO).getStringRepresentation();
		else
			path = defaultPath;

		addProfilePic();
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public TypeEntry getTypeEntryByType(String string) {
		return null;
	}

	@Override
	public void updateContact() {
		currentContact.setProperty(VCardOptions.PHOTO, new PhotoValue(path));
	}
}
