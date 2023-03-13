package br.com.atendimento.services;

import java.text.ParseException;
import java.util.Date;

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

import br.com.atendimento.dto.sincronizar.SincronizarPostDto;
import br.com.atendimento.util.DataUtils;

@Service
public class SincronismoService {

	@Value("${sinc.host}")
	private String sincHost;
	
	@Value("${sinc.key}")
	private String sincKey;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(SincronismoService.class);

	public Boolean sincronizarConta(Integer agencia, Integer conta) throws ParseException {

		log.info(" Método para sincronisar ag {} e conta {}", agencia, conta);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("X-Api-Key", sincKey);
			
			SincronizarPostDto sinc = new SincronizarPostDto();
			sinc.setAgencia(agencia);
			sinc.setConta(conta);
			sinc.setDataSincronismo(DataUtils.format(new Date(), DataUtils.tracoformatoData));
			
			HttpEntity<SincronizarPostDto> requestEntity = new HttpEntity<SincronizarPostDto>(sinc, headers);

			ResponseEntity<String> response = restTemplate.exchange(sincHost + "/ContaCorrente/SincronizarConta", HttpMethod.POST,
					requestEntity, String.class);

			Gson g = new Gson();
			if (response.getStatusCode() == HttpStatus.CREATED) {
				Boolean resp = g.fromJson(response.getBody(),
						new TypeToken<Boolean>() {
						}.getType());
				return resp;
			}
		} catch (HttpStatusCodeException e) {
			return false;
		}
		return false;
	}
}
