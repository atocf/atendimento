package br.com.atendimento.client;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import br.com.atendimento.wsdl.cdtp004.ConsultarDadosCartao;
import br.com.atendimento.wsdl.cdtp004.ConsultarDadosCartaoResponse;

@Service
public class Cdtp004Client extends WebServiceGatewaySupport {
	
	@Value("${cdtp004.wsdl}")
	private String cdtp004Wsdl;
	
	@Value("${cdtp004.soap.action}")
	private String cdtp004SoapAction;

	@Autowired
	private Jaxb2Marshaller marshallerCdtp004Client;
	
	@Autowired
	private WebServiceTemplate webServiceTemplate;

	private static final Logger log = LoggerFactory.getLogger(Cdtp004Client.class);

	public ConsultarDadosCartaoResponse consultarDadosCartao(ConsultarDadosCartao item)
			throws NoSuchAlgorithmException, KeyManagementException {
		log.info("ConsultarDadosCartao Cdtp004Client");
		webServiceTemplate = new WebServiceTemplate(marshallerCdtp004Client);
		webServiceTemplate.setMarshaller(marshallerCdtp004Client);
		webServiceTemplate.setUnmarshaller(marshallerCdtp004Client);
		return (ConsultarDadosCartaoResponse) webServiceTemplate.marshalSendAndReceive(cdtp004Wsdl, item, new SoapActionCallback(cdtp004SoapAction));
	}
}
