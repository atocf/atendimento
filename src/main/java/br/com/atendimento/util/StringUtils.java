package br.com.atendimento.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {
	
	private static final Logger log = LoggerFactory.getLogger(StringUtils.class);
	
	public static String valid(String str) {
		log.info("Validando string is null {}", str);
		if(str != null) {
			return str;
		} else {
			return "";
		} 
	}
}
