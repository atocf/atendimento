package br.com.atendimento.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CpfUtils {
	
	private static final Logger log = LoggerFactory.getLogger(CpfUtils.class);

	public static String valid(String cpf) {
		log.info("Validando o formato do CPF {}", cpf);
		if(cpf != null && cpf.length() < 11) {
			log.info("CPF menor que 11 caracteres: {}", cpf);
			cpf = StringUtils.leftPad(cpf, 11, "0");
		} else if(cpf != null && cpf.length() > 11) {
			log.info("CPF maior que 11 caracteres: {}", cpf);
			return null;
		} 
		return cpf;
	}
}