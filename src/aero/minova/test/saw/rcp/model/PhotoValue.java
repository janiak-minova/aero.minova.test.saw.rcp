package aero.minova.test.saw.rcp.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class PhotoValue extends Value {

	private byte[] base64Encoding;
	private String filetype;

	public PhotoValue(String path, String filetype) {
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL url = FileLocator.find(bundle, new Path(path), null);
		try {
			URL fileUrl = FileLocator.toFileURL(url);
			base64Encoding = PhotoValue.encodeBase64(fileUrl.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.filetype = filetype;
	}

	public PhotoValue(byte[] bs, String filetype) {
		this.base64Encoding = bs;
		this.filetype = filetype;
	}

	@Override
	public String getStringRepresentation() {
		return Base64.getEncoder().encodeToString(base64Encoding);
	}

	@Override
	public String getVCardString() {
		return Base64.getEncoder().encodeToString(base64Encoding);
	}

	public static byte[] encodeBase64(String path) {

		File file = new File(path);
		try (FileInputStream imageInFile = new FileInputStream(file)) {
			// Reading a Image file from file system
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			// base64Image = Base64.getEncoder().encodeToString(imageData);
			return imageData;
			// return null;
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}

		// }
		return null;
	}

	public byte[] getBase64Encoding() {
		return base64Encoding;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getFiletype() {
		return filetype;
	}
}
