package com.exoma.dao;


import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@Named
public class CalendarView {
	
    private Date date;
    
    public Date getDate() {
    	return date;
    }
    public void setDate(Date date1) {
    	this.date = date1;
    }
    
	public CalendarView() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		this.date = cal.getTime();
	}
	
    public void init() {
		Date parsesdf0=null;
		String df0 =GregorianCalendar.getInstance().getTime().toString();
		SimpleDateFormat sdf0 = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		try {
			parsesdf0 = sdf0.parse(df0);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		setDate(parsesdf0);
	}

    public void onDateSelect(SelectEvent event) {
	FacesContext facesContext = FacesContext.getCurrentInstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	facesContext.addMessage(null, new  FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected",     format.format(event.getObject())));
    }

	public void click() {
		PrimeFaces.current().ajax().update("form:display");
		PrimeFaces.current().executeScript("PF('dlg').show()");
	}
    
}

