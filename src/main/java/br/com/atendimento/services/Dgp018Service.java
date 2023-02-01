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

import br.com.atendimento.dto.dgp018.Dgp018StatusDto;

@Service
public class Dgp018Service {

	@Value("${dgp018.host}")
	private String dgp018Host;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(Dgp018Service.class);

	public String consultaStatus(String cpf) {

		log.info(" Método para retorno do status por CPF: {}", cpf);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("cpf", cpf);
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(dgp018Host + "/Status/Cpf", HttpMethod.GET,
					requestEntity, String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.OK) {
				Dgp018StatusDto resp = g.fromJson(response.getBody(),
						new TypeToken<Dgp018StatusDto>() {
						}.getType());
				if (resp != null) {
					return resp.getStatus();
				}
			}
		} catch (HttpStatusCodeException e) {
			return null;
		}
		return null;
	}
	
	public String limpezaCadastro(String cpf) {

		log.info(" Método para retorno do status por CPF: {}", cpf);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("cpfCliente", cpf);
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(dgp018Host + "/limpeza-cadastro", HttpMethod.POST,
					requestEntity, String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.OK) {
				Dgp018StatusDto resp = g.fromJson(response.getBody(),
						new TypeToken<Dgp018StatusDto>() {
						}.getType());
				if (resp != null) {
					return resp.getStatus();
				}
			}
		} catch (HttpStatusCodeException e) {
			return null;
		}
		return null;
	}

}
