package com.atguigu.ems.struts2.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class DateConverter extends StrutsTypeConverter{

	private DateFormat dateFormat;
	
	{
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}
	
	@Override
	public Object convertFromString(Map arg0, String[] arg1, Class arg2) {
		Date date = null;

		if(arg2.equals(Date.class)){
			try {
				date = dateFormat.parse(arg1[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return date;
	}

	@Override
	public String convertToString(Map arg0, Object arg1) {
		if(arg1 instanceof Date){
			return dateFormat.format((Date)arg1);
		}
		
		return null;
	}

}
