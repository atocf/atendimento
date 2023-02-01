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

import br.com.atendimento.dto.imp013.Imp013GetPinEletronicoStatusDto;

@Service
public class Imp013Service {

	@Value("${imp013.host}")
	private String imp013Host;

	@Value("${imp013.header.authorization}")
	private String imp013HeaderAuthorization;
	
	@Value("${imp013.header.authorization.inativar}")
	private String imp013HeaderAuthorizationInativar;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(Imp013Service.class);

	public String consultaStatusPin(String cpf) {
		log.info("Consultar o status do Pin Eletrônico pelo CPF: {}", cpf);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", imp013HeaderAuthorization);
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(
					imp013Host + "/v1/pin/eletronico/consultar-status?cpf=" + cpf, HttpMethod.GET, requestEntity,
					String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.OK) {
				Imp013GetPinEletronicoStatusDto pinStatus = g.fromJson(response.getBody(),
						new TypeToken<Imp013GetPinEletronicoStatusDto>() {
						}.getType());
				if (pinStatus != null && pinStatus.getData() != null) {
					return pinStatus.getData().getDescricaoStatus();
				}
			}
		} catch (HttpStatusCodeException e) {
			return null;
		}
		return null;
	}
	
	public String inativarPinEletronico(String cpf) {
		log.info("Permite inativar o Pin Eletrônico pelo CPF: {}", cpf);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", imp013HeaderAuthorizationInativar);
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(
					imp013Host + "/v1/pin/eletronico/inativar?cpf=" + cpf, HttpMethod.POST, requestEntity,
					String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.OK) {
				Imp013GetPinEletronicoStatusDto pinStatus = g.fromJson(response.getBody(),
						new TypeToken<Imp013GetPinEletronicoStatusDto>() {
						}.getType());
				if (pinStatus != null && pinStatus.getData() != null) {
					return pinStatus.getData().getDescricaoStatus();
				}
			}
		} catch (HttpStatusCodeException e) {
			return null;
		}
		return null;
	}
}
