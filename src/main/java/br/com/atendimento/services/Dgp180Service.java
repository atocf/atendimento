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

@Service
public class Dgp180Service {
	
	@Value("${dgp180.host}")
	private String dgp180Host;
	
	@Value("${dgp180.header.authorization}")
	private String dgp180HeaderAuthorization;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(Dgp180Service.class);

	public String deleteRedis(String cpf) {
		log.info("Consultar o status do Pin Eletrônico pelo CPF: {}", cpf);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Basic "+dgp180HeaderAuthorization);
			headers.set("cpf", cpf);
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(
					dgp180Host + "/api/support/v1/account/redis", HttpMethod.DELETE, requestEntity,
					String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.OK) {
				Boolean resp = g.fromJson(response.getBody(),
						new TypeToken<Boolean>() {
						}.getType());
				return resp.toString();
			}
		} catch (HttpStatusCodeException e) {
			return null;
		}
		return null;
	}
}
