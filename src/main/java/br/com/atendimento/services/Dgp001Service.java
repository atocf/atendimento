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

import br.com.atendimento.dto.dgp001.Dgp001Cliente;

@Service
public class Dgp001Service {
	
	@Value("${dgp001.host}")
	private String dgp001Host;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(Dgp001Service.class);

	public Dgp001Cliente consultaCliente(String cpf) {
		log.info("Consultar os dados cadastrais pelo CPF: {}", cpf);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("cpf", cpf);
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(dgp001Host + "/Clientes",
					HttpMethod.GET, requestEntity, String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.OK) {
				Dgp001Cliente cliente = g.fromJson(response.getBody(), new TypeToken<Dgp001Cliente>() {}.getType());
				if (cliente != null) {
					return cliente;
				}
			}
		} catch (HttpStatusCodeException e) {
			return null;
		}
		return null;
	}
}
