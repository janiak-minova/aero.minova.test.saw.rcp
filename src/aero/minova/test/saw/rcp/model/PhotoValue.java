package aero.minova.test.saw.rcp.model;

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
		return "hä?";
	}

}
