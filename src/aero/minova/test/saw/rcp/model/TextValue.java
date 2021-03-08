package aero.minova.test.saw.rcp.model;

public class TextValue extends Value {

	private String value;

	public TextValue(String val) {
		this.value = val;
	}

	@Override
	public String getStringRepresentation() {
		return value;
	}

	@Override
	public String getVCardString() {
		return value;
	}

}
