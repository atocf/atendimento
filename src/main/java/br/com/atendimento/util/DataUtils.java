package br.com.atendimento.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {

	public static SimpleDateFormat usformatoDataHora = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatoDataHora = new SimpleDateFormat("dd/MM/yyyy hh:mm"); 
	public static SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy"); 
	public static SimpleDateFormat tracoformatoData = new SimpleDateFormat("dd-MM-yyyy");
	
	public static Date convert(String x, SimpleDateFormat format) throws ParseException {
		if(x != null) {
			Date data = format.parse(x);
			return data;
		} 
		return null;
	}	
	
	public static String format(Date x, SimpleDateFormat format) throws ParseException {
		if(x != null) {
			return format.format(x);
		} 
		return null;
	}	
}
