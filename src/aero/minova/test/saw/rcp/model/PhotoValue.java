package aero.minova.test.saw.rcp.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import aero.minova.test.saw.rcp.constants.Constants;

public class PhotoValue extends Value {

	private String path;

	public PhotoValue(String path) {
		this.path = path;
	}

	@Override
	public String getStringRepresentation() {
		return path;
	}

	@Override
	public String getVCardString() {

		if (!path.equals(Constants.DEFAULTPIC)) {

			String base64Image = "";
			File file = new File(path);
			try (FileInputStream imageInFile = new FileInputStream(file)) {
				// Reading a Image file from file system
				byte imageData[] = new byte[(int) file.length()];
				imageInFile.read(imageData);
				base64Image = Base64.getEncoder().encodeToString(imageData);
			} catch (FileNotFoundException e) {
				System.out.println("Image not found" + e);
			} catch (IOException ioe) {
				System.out.println("Exception while reading the Image " + ioe);
			}
			return base64Image;
		}
		return null;
	}
}
