package aero.minova.test.saw.rcp.model;

public class Address extends Value {
	private String poBox;
	private String extended;
	private String street;
	private String city;
	private String region;
	private String postCode;
	private String country;

	public Address(String structuredAddress) {
		String[] splitted = structuredAddress.split(";", -1);

		poBox = splitted[0];
		extended = splitted[1];
		street = splitted[2];
		city = splitted[3];
		region = splitted[4];
		postCode = splitted[5];
		country = splitted[6];
	}

	@Override
	public String getStringRepresentation() {
		return street + "\n" + postCode + " " + city + "\n" + country;
	}

	@Override
	public String getVCardString() {
		return poBox + ";" + extended + ";" + street + ";" + city + ";" + region + ";" + postCode + ";" + country;
	}

	public String getPoBox() {
		return poBox;
	}

	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}

	public String getExtended() {
		return extended;
	}

	public void setExtended(String extended) {
		this.extended = extended;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
