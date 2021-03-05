package aero.minova.test.saw.rcp.vCard;

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

	public static String getValue(VCardProperty prop) {
		if (prop instanceof TextListProperty) { // Categories, Nickname, Organization
			return ((TextListProperty) prop).getValues().get(0);
		} else if (prop instanceof TextProperty) { // Classification, Email, Expertise, FormattedName, Hobby, Interest, Kind, Label, Language, Mailer, Note,
													// ProductId, Profile, RawPropertie, Role, SortString, SourceDisplayText, Title, UriProperty
			return ((TextProperty) prop).getValue();
		} else if (prop instanceof ImageProperty) { // Photo, Logo
			return ((ImageProperty) prop).getUrl();
		} else if (prop instanceof StructuredName) {
			return ((StructuredName) prop).getGiven();
		} else if (prop instanceof Telephone) {
			return ((Telephone) prop).getText();
		} else if (prop instanceof Address) {
			return ((Address) prop).getStreetAddress();
		}

		return null;
	}
}
