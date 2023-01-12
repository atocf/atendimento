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

import br.com.atendimento.dto.imp003.Imp003GetPinEletronicoStatus;

@Service
public class Imp003Service {

	@Value("${imp003.host}")
	private String imp003Host;

	@Value("${imp003.header.authorization}")
	private String imp003HeaderAuthorization;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(Imp003Service.class);

	public String consultaStatusPin(String cpf) {
		log.info("Consultar o status do Pin Eletrônico pelo CPF: {}", cpf);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", imp003HeaderAuthorization);
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(
					imp003Host + "/v1/pin/eletronico/consultar-status?cpf=" + cpf, HttpMethod.GET, requestEntity,
					String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.OK) {
				Imp003GetPinEletronicoStatus pinStatus = g.fromJson(response.getBody(),
						new TypeToken<Imp003GetPinEletronicoStatus>() {
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
