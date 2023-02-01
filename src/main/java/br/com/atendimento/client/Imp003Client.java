package br.com.atendimento.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Service
public class Imp003Client extends WebServiceGatewaySupport {

	@Value("${imp003.wsdl}")
	private String imp003Wsdl;
	
	@Value("${imp003.soap.action}")
	private String imp003SoapAction;

	@Autowired
	private Jaxb2Marshaller marshallerImp001Client;
	
	@Autowired
	private WebServiceTemplate webServiceTemplate;

	private static final Logger log = LoggerFactory.getLogger(Imp003Client.class);

	
}