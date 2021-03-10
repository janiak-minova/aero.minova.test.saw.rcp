package aero.minova.test.saw.rcp.entries;

import static org.eclipse.jface.widgets.ButtonFactory.newButton;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import aero.minova.test.saw.rcp.constants.Constants;
import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.PhotoValue;
import aero.minova.test.saw.rcp.vCard.VCardOptions;

public class PhotoPropertyEntry extends PropertyEntry {

	private Contact currentContact;

	private Label label;

	private Button editButton;
	private Button deleteButton;

	private boolean editable;

	private String defaultPath = Constants.DEFAULTPIC;
	int scalePx = 70;

	private byte[] data;

	private String filetype;

	private boolean usingDefault;

	public PhotoPropertyEntry(Composite body) {
		// Label für Bild
		label = new Label(body, SWT.RIGHT);
		GridData gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
		addProfilePic();
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				editProfilePic();
			}
		});

		// Buttons
		Composite comp = new Composite(body, SWT.None);
		comp.setLayout(new GridLayout(1, false));
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		comp.setLayoutData(gd);

		editButton = newButton(SWT.PUSH).text("Bild bearbeiten").onSelect(e -> editProfilePic()).create(comp);
		editButton.setVisible(false);
		deleteButton = newButton(SWT.PUSH).text("Bild löschen").onSelect(e -> deleteProfilePic()).create(comp);
		deleteButton.setVisible(false);
	}

	private void addProfilePic() {
		if (data == null)
			setDefaultPicData();

		try {
			Image image = new Image(null, new ByteArrayInputStream(data));

			// Scaliere Bild
			Image scaled = new Image(Display.getDefault(), scalePx, scalePx);
			GC gc = new GC(scaled);
			gc.setAntialias(SWT.ON);
			gc.setInterpolation(SWT.HIGH);
			gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, scalePx, scalePx);
			gc.dispose();
			image.dispose();

			label.setImage(scaled);
		} catch (Exception e) {}
	}

	private void editProfilePic() {
		if (editable) {
			FileDialog dialog = new FileDialog(new Shell(), SWT.OPEN);
			String filterExtensionsString = "";
			for (String s : VCardOptions.PHOTOTYPES) {
				filterExtensionsString += "*." + s + ";";
			}
			dialog.setFilterExtensions(new String[] { filterExtensionsString });
			String path = dialog.open();
			if (path != null) {
				setData(path);
				addProfilePic();
			}
		}
	}

	private void deleteProfilePic() {
		setDefaultPicData();
		addProfilePic();
	}

	private void setData(String path) {
		data = PhotoValue.encodeBase64(path);
		usingDefault = false;
		String[] split = path.split("\\.");
		filetype = split[split.length - 1];
	}

	private void setDefaultPicData() {
		String[] split = defaultPath.split("\\.");
		filetype = split[split.length - 1];
		usingDefault = true;
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL url = FileLocator.find(bundle, new Path(defaultPath), null);
		try {
			URL fileUrl = FileLocator.toFileURL(url);
			data = PhotoValue.encodeBase64(fileUrl.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setInput(Contact c) {
		currentContact = c;
		if (c.getValue(VCardOptions.PHOTO) != null) {
			data = ((PhotoValue) c.getValue(VCardOptions.PHOTO)).getBase64Encoding();
			usingDefault = false;
			filetype = ((PhotoValue) c.getValue(VCardOptions.PHOTO)).getFiletype();
		} else
			setDefaultPicData();

		addProfilePic();
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
		editButton.setVisible(editable);
		deleteButton.setVisible(editable);
	}

	@Override
	public TypeEntry getTypeEntryByType(String string) {
		return null;
	}

	@Override
	public void updateContact() {
		if (!usingDefault)
			currentContact.setProperty(VCardOptions.PHOTO, filetype, new PhotoValue(data, filetype));
		else
			currentContact.removeProperty(VCardOptions.PHOTO);
	}
}
