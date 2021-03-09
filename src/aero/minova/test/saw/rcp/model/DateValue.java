package aero.minova.test.saw.rcp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import aero.minova.test.saw.rcp.constants.Constants;

public class DateValue extends Value {

	private Date date;

	public DateValue(String dateString) {
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATEFORMAT);
		try {
			this.setDate(format.parse(dateString));
		} catch (ParseException e) {
			if (!dateString.equals(""))
				e.printStackTrace();
		}
	}

	public DateValue(Date date) {
		this.setDate(date);
	}

	@Override
	public String getStringRepresentation() {
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATEFORMAT);
		return format.format(getDate());
	}

	@Override
	public String getVCardString() {
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATEFORMATVCARD);
		return format.format(getDate());
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
