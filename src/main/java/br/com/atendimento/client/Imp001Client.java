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

import br.com.atendimento.wsdl.imp001.ObterContasCorrentesPessoa;
import br.com.atendimento.wsdl.imp001.ObterContasCorrentesPessoaResponse;

@Service
public class Imp001Client extends WebServiceGatewaySupport {

	@Value("${imp001.wsdl}")
	private String imp001Wsdl;
	
	@Value("${imp001.soap.action}")
	private String imp001SoapAction;

	@Autowired
	private Jaxb2Marshaller marshallerImp001Client;
	
	@Autowired
	private WebServiceTemplate webServiceTemplate;

	private static final Logger log = LoggerFactory.getLogger(Imp001Client.class);

	public ObterContasCorrentesPessoaResponse obterContasCorrentesPessoa(ObterContasCorrentesPessoa item)
			throws NoSuchAlgorithmException, KeyManagementException {
		log.info("ObterContasCorrentesPessoa Imp001Client");
		webServiceTemplate = new WebServiceTemplate(marshallerImp001Client);
		webServiceTemplate.setMarshaller(marshallerImp001Client);
		webServiceTemplate.setUnmarshaller(marshallerImp001Client);
		return (ObterContasCorrentesPessoaResponse) webServiceTemplate.marshalSendAndReceive(imp001Wsdl, item, new SoapActionCallback(imp001SoapAction));
	}
}