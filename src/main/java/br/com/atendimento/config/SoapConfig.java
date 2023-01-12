package br.com.atendimento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import br.com.atendimento.client.Cdtp004Client;
import br.com.atendimento.client.Imp001Client;
import br.com.atendimento.util.NullHostVerifierHttpsMessageSender;

@Configuration
public class SoapConfig {
	
	
	@Bean
	public SaajSoapMessageFactory messageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.setSoapVersion(SoapVersion.SOAP_12);
		return messageFactory;
	}
	
	@Bean
	public Jaxb2Marshaller marshallerImp001Client() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setPackagesToScan("br.com.bmg.atendimento.wsdl.imp001");
		return jaxb2Marshaller;
	}
	
	@Bean
	public Imp001Client customerImp001Client (Jaxb2Marshaller marshallerImp001Client) {
		Imp001Client customerImp001Client = new Imp001Client();
		customerImp001Client.setMarshaller(marshallerImp001Client);
		customerImp001Client.setUnmarshaller(marshallerImp001Client);
		return customerImp001Client;
	}
	
	@Bean
	public Jaxb2Marshaller marshallerCdtp004Client() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setPackagesToScan("br.com.bmg.atendimento.wsdl.cdtp004");
		return jaxb2Marshaller;
	}
	
	@Bean
	public Cdtp004Client customerCdtp004Client (Jaxb2Marshaller marshallerCdtp004Client) {
		Cdtp004Client customerCdtp004Client = new Cdtp004Client();
		customerCdtp004Client.setMarshaller(marshallerCdtp004Client);
		customerCdtp004Client.setUnmarshaller(marshallerCdtp004Client);
		return customerCdtp004Client;
	}

	@Bean
	public WebServiceTemplate webServiceTemplate() throws Exception {
	
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setMessageSender(new NullHostVerifierHttpsMessageSender());

		return webServiceTemplate;
	}
}