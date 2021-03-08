package aero.minova.test.saw.rcp.vCard;

import java.util.List;

import aero.minova.test.saw.rcp.model.TextValue;
import aero.minova.test.saw.rcp.model.Value;
import ezvcard.property.Address;
import ezvcard.property.ImageProperty;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import ezvcard.property.TextListProperty;
import ezvcard.property.TextProperty;
import ezvcard.property.VCardProperty;

public class VCardMapping {

	public static String getPropertyString(VCardProperty vCardProp) {
		switch (vCardProp.getClass().getName()) {
		case ("ezvcard.property.Photo"):
			return VCardOptions.PHOTO;
		case ("ezvcard.property.StructuredName"):
			return VCardOptions.NAME;
		case ("ezvcard.property.Organization"):
			return VCardOptions.ORG;
		case ("ezvcard.property.Telephone"):
			return VCardOptions.TEL;
		case ("ezvcard.property.Email"):
			return VCardOptions.EMAIL;
		case ("ezvcard.property.Address"):
			return VCardOptions.ADR;
		case ("ezvcard.property.Note"):
			return VCardOptions.NOTE;
		case ("ezvcard.property.ProductId"):
			return null;
		default:
			System.err.println("Unbekannte Property: " + vCardProp.getClass().getName());
			return null;
		}
	}

	public static Value getValue(VCardProperty prop) {
		if (prop instanceof TextListProperty) { // Categories, Nickname, Organization
			return new TextValue(((TextListProperty) prop).getValues().get(0));
		} else if (prop instanceof TextProperty) { // Classification, Email, Expertise, FormattedName, Hobby, Interest, Kind, Label, Language, Mailer, Note,
													// ProductId, Profile, RawPropertie, Role, SortString, SourceDisplayText, Title, UriProperty
			return new TextValue(((TextProperty) prop).getValue());
		} else if (prop instanceof ImageProperty) { // Photo, Logo
			return new TextValue(((ImageProperty) prop).getUrl());
		} else if (prop instanceof StructuredName) {
			StructuredName sName = (StructuredName) prop;
			String val = "";
			val += ((sName.getFamily() == null) ? "" : sName.getFamily()) + ";";
			val += ((sName.getGiven() == null) ? "" : sName.getGiven()) + ";";
			val += getListAsString(sName.getAdditionalNames()) + ";";
			val += getListAsString(sName.getPrefixes()) + ";";
			val += getListAsString(sName.getSuffixes());
			return new aero.minova.test.saw.rcp.model.StructuredName(val);
		} else if (prop instanceof Telephone) {
			return new TextValue(((Telephone) prop).getText());
		} else if (prop instanceof Address) {
			Address addr = (Address) prop;
			String val = "";
			val += ((addr.getPoBox() == null) ? "" : addr.getPoBox()) + ";";
			val += ((addr.getExtendedAddress() == null) ? "" : addr.getExtendedAddress()) + ";";
			val += ((addr.getStreetAddress() == null) ? "" : addr.getStreetAddress()) + ";";
			val += ((addr.getLocality() == null) ? "" : addr.getLocality()) + ";";
			val += ((addr.getRegion() == null) ? "" : addr.getRegion()) + ";";
			val += ((addr.getPostalCode() == null) ? "" : addr.getPostalCode()) + ";";
			val += ((addr.getCountry() == null) ? "" : addr.getCountry());

			return new aero.minova.test.saw.rcp.model.Address(val);
		}

		return null;
	}

	public static String getListAsString(List<String> list) {
		String res = "";
		for (String s : list) {
			res += s + " ";
		}
		return res.trim();
	}
}
