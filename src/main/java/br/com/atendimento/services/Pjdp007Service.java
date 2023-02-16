package br.com.atendimento.services;

import java.util.List;

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

import br.com.atendimento.dto.pjdp007.Pjdp007Dto;

@Service
public class Pjdp007Service {

	@Value("${pjdp007.host}")
	private String pjdp007Host;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(Pjdp007Service.class);

	public String consultarGranito(String cpf) {
		log.info("Consultar clientes originados pela Granito pelo CPF: {}", cpf);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(pjdp007Host + "/api/v1/proposta/socio/" + cpf,
					HttpMethod.GET, requestEntity, String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.OK) {
				List<Pjdp007Dto> resp = g.fromJson(response.getBody(), new TypeToken<List<Pjdp007Dto>>() {
				}.getType());
				if (resp.size() > 0 && resp.get(0).getStatusTicket().equals("Aprovado")
						&& resp.get(0).getProposta() != null && resp.get(0).getProposta().getPessoaJuridica() != null) {
					return resp.get(0).getProposta().getPessoaJuridica().getCnpj();
				}
			}
		} catch (HttpStatusCodeException e) {
			return null;
		}
		return null;
	}
}
