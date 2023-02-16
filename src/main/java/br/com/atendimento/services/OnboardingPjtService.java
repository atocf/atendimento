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

import br.com.atendimento.dto.onboardingpjt.OnboardingPjtDto;

@Service
public class OnboardingPjtService {

	@Value("${onboarding.pjt.host}")
	private String onboardingPjtHost;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(OnboardingPjtService.class);

	public String consultaContrato(String cpf) {
		log.info("Consultar clientes originados pelo App Meu Bmg PF pelo CPF: {}", cpf);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Cpf", cpf);
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(
					onboardingPjtHost + "/api/v1/relatorios/intergrall/proposta-cpf", HttpMethod.GET, requestEntity,
					String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.OK) {
				OnboardingPjtDto resp = g.fromJson(response.getBody(), new TypeToken<OnboardingPjtDto>() {
				}.getType());
				if (resp != null && resp.getData() != null && resp.getData().getStatusTicket().equals("APROVADA")
						&& resp.getData().getDadosEmpresariais() != null) {
					return resp.getData().getDadosEmpresariais().getCnpj();
				}
			}
		} catch (HttpStatusCodeException e) {
			return null;
		}
		return null;
	}
}
