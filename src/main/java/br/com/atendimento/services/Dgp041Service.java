package br.com.atendimento.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.atendimento.dto.dgp041.Dgp041Usuario;

@Service
public class Dgp041Service {

	@Value("${dgp041.host}")
	private String dgp041Host;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(Dgp041Service.class);

	public String consultaScopo(String cpf) {
		log.info("Consultar o scopo pelo CPF: {}", cpf);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("cpf", cpf);
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(dgp041Host + "/api/Autorizacoes/usuario-escopos",
					HttpMethod.GET, requestEntity, String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.OK) {
				Dgp041Usuario usuario = g.fromJson(response.getBody(), new TypeToken<Dgp041Usuario>() {}.getType());
				if (usuario != null && usuario.getData() != null && usuario.getData().getEscoposAutorizados().size() > 0) {
					return usuario.getData().getEscoposAutorizados().get(0);
				}
			}
		} catch (HttpStatusCodeException e) {
			return null;
		}
		return null;
	}
}
