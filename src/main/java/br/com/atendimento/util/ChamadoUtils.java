package br.com.atendimento.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.atendimento.entity.Status;

public class ChamadoUtils {
		
	private static final Logger log = LoggerFactory.getLogger(ChamadoUtils.class);
	
	public static boolean prioritario(String canalatendimento) {
		log.info("Validando se o canal {} é prioritario", canalatendimento);
		if (canalatendimento.equals("CAPTAÇÃO") || canalatendimento.equals("RECLAME AQUI") || canalatendimento.equals("CONSUMIDOR.GOV")
				|| canalatendimento.equals("BACEN") || canalatendimento.equals("OUVIDORIA CLIENTE") || canalatendimento.equals("ALTO ATRITO OUVIDORIA")
				|| canalatendimento.equals("PROCON") || canalatendimento.equals("PROCON FONE")) {
			return true;
		}
		return false;
	}

	public static boolean validStatusEncaminhado(Status status) {
		if(status != null && status.getNome().equals("ENCAMINHADO")) {
			return true;
		}
		return false;
	}
}
