package br.com.atendimento.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CnpjUtils {
	
	private static final Logger log = LoggerFactory.getLogger(CnpjUtils.class);

	public static String valid(String cnpj) {
		log.info("Validando o formato do CNPJ {}", cnpj);
		if(cnpj.length() < 14) {
			log.info("CNPJ menor que 14 caracteres: {}", cnpj);
			cnpj = StringUtils.leftPad(cnpj, 14, "0");
		} else if(cnpj.length() > 14) {
			log.info("CNPJ maior que 14 caracteres: {}", cnpj);
			return null;
		} 
		return cnpj;
	}

}