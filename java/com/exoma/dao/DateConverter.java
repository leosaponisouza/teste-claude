package com.exoma.dao;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@FacesConverter("dateconverter")
public class DateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		System.out.println("arg2 em DateConverter: [" + arg2 + "]");
		if (arg2!="" && arg2!=null) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date temp = format.parse(arg2);
				Date date = new java.sql.Date(temp.getTime());
				System.out.println("retorno em DateConverter: [" + date.toString() + "]");
				return date;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
        return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return arg2.toString();
	}

}
