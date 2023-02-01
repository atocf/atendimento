package br.com.atendimento.services;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.atendimento.client.Cdtp004Client;
import br.com.atendimento.client.Imp001Client;
import br.com.atendimento.dto.dgp001.Dgp001Cliente;
import br.com.atendimento.entity.Cartao;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Conta;
import br.com.atendimento.util.DataUtils;
import br.com.atendimento.wsdl.cdtp004.ConsultarDadosCartao;
import br.com.atendimento.wsdl.cdtp004.ConsultarDadosCartaoReq;
import br.com.atendimento.wsdl.cdtp004.ConsultarDadosCartaoResponse;
import br.com.atendimento.wsdl.imp001.DadosRetornoContaCorrente;
import br.com.atendimento.wsdl.imp001.ObterContasCorrentesPessoa;
import br.com.atendimento.wsdl.imp001.ObterContasCorrentesPessoaResponse;
import br.com.atendimento.wsdl.imp001.ParametrosObterContaCorrente;

@Service
public class IntegracaoService {

	@Autowired
	private Cdtp004Client cdtp004Client;

	@Value("${cdtp004.loginWs}")
	private String cdtp004LoginWs;

	@Value("${cdtp004.senhaWs}")
	private String cdtp004SenhaWs;

	@Autowired
	private Dgp001Service dgp001Service;

	@Autowired
	private Dgp041Service dgp041Service;

	@Autowired
	private Imp001Client imp001Client;

	@Value("${imp001.loginWs}")
	private String imp001LoginWs;

	@Value("${imp001.senhaWs}")
	private String imp001SenhaWs;

	@Autowired
	private Imp013Service imp003Service;

	@Autowired
	private ContaService contaService;

	@Autowired
	private CartaoService cartaoService;
	
	@Autowired
	private ChamadoService chamadoService;

	private static final Logger log = LoggerFactory.getLogger(IntegracaoService.class);

	public void buscarDados(Chamado chamado)
			throws KeyManagementException, NoSuchAlgorithmException, ParseException {
		
		String cpf_cnpj = null;
		
		if(chamado.getCpf() != null) {
			cpf_cnpj = chamado.getCpf();
		} else if(chamado.getCnpj() != null) {
			cpf_cnpj = chamado.getCnpj();
		}
		

		if (chamado.getSubmotivo().getCdtp004() && chamado.getCpf() != null && !(cartaoService.findByChamado_Ocorrencia(chamado.getOcorrencia()).size() > 0)) {
			log.info("Consulta dados cartão do cpf: {}", chamado.getCpf());

			ConsultarDadosCartao consultarDadosCartao = new ConsultarDadosCartao();

			ConsultarDadosCartaoReq consultarDadosCartaoReq = new ConsultarDadosCartaoReq();
			consultarDadosCartaoReq.setLoginWs(cdtp004LoginWs);
			consultarDadosCartaoReq.setSenhaWs(cdtp004SenhaWs);
			consultarDadosCartaoReq.setCPF(chamado.getCpf());

			consultarDadosCartao.setParametros(consultarDadosCartaoReq);

			ConsultarDadosCartaoResponse resp = cdtp004Client.consultarDadosCartao(consultarDadosCartao);

			if (resp != null && resp.getConsultarDadosCartaoResult() != null
					&& resp.getConsultarDadosCartaoResult().getCartoes() != null
					&& resp.getConsultarDadosCartaoResult().getCartoes().getCartao().size() > 0) {

				for (br.com.atendimento.wsdl.cdtp004.Cartao cartao : resp.getConsultarDadosCartaoResult()
						.getCartoes().getCartao()) {
					Cartao c = new Cartao();
					c.setNumerocartao(cartao.getNumeroCartao());
					c.setNomeimpresso(cartao.getNomeImpresso());
					c.setDatavalidade(cartao.getDataValidade().toGregorianCalendar().getTime());
					c.setDescricaostatuscartao(cartao.getDescricaoStatusCartao());
					c.setCartaovirtual(cartao.isCartaoVirtual());
					c.setVirtualizavel(cartao.isVirtualizavel());
					c.setDescricaotipocartao(cartao.getDescricaoTipoCartao());
					c.setNumerobin(cartao.getNumeroBin());
					c.setChamado(chamado);

					cartaoService.save(c);
				}
			}

			log.info("Fim consulta dados cartão");
		}

		if (chamado.getSubmotivo().getDgp001() && chamado.getDataatualizacaocadastral() == null) {

			log.info("Consulta dados cliente do cpf/cnpj: {}", cpf_cnpj);

			Dgp001Cliente resp = dgp001Service.consultaCliente(cpf_cnpj);
			if (resp != null) {
				chamado.setDataatualizacaocadastral(
						DataUtils.convert(resp.getDataUltimaAtualizacao(), DataUtils.usformatoDataHora));
				chamado.setEmail(resp.getEmail());
				chamado.setTelefone(resp.getTelefone().getDdd().toString() + resp.getTelefone().getNumero().toString());
				chamado.setStatussms(resp.getTelefone().getEnviaSms());
			}

			log.info("Fim consulta dados cliente");
		}

		if (chamado.getSubmotivo().getDgp041() && chamado.getCpf() != null && chamado.getEscopo() == null) {
			log.info("Consulta scopo do cpf: {}", chamado.getCpf());

			chamado.setEscopo(dgp041Service.consultaScopo(chamado.getCpf()));

			log.info("Fim consulta scopo");
		}

		if (chamado.getSubmotivo().getImp001() && !(contaService.findByChamado_Ocorrencia(chamado.getOcorrencia()).size() > 0)) {
			log.info("Consulta dados conta do cpf/cnpj: {}", cpf_cnpj);

			ObterContasCorrentesPessoa obterContasCorrentesPessoa = new ObterContasCorrentesPessoa();

			ParametrosObterContaCorrente parametrosObterContaCorrente = new ParametrosObterContaCorrente();
			parametrosObterContaCorrente.setLoginWs(imp001LoginWs);
			parametrosObterContaCorrente.setSenhaWs(imp001SenhaWs);
			parametrosObterContaCorrente.setIdOrigemIntegracao(44);
			parametrosObterContaCorrente.setInscricaoCPFCNPJ(Long.parseLong(cpf_cnpj));

			obterContasCorrentesPessoa.setParametros(parametrosObterContaCorrente);

			ObterContasCorrentesPessoaResponse resp = imp001Client
					.obterContasCorrentesPessoa(obterContasCorrentesPessoa);

			if (resp != null && resp.getObterContasCorrentesPessoaResult() != null
					&& resp.getObterContasCorrentesPessoaResult().getContas() != null
					&& resp.getObterContasCorrentesPessoaResult().getContas().getDadosRetornoContaCorrente()
							.size() > 0) {
				
				for (DadosRetornoContaCorrente conta : resp.getObterContasCorrentesPessoaResult().getContas()
						.getDadosRetornoContaCorrente()) {

					Conta c = new Conta();
					c.setAgencia(Integer.valueOf(conta.getAgencia()));
					c.setNomeagencia(conta.getNomeAgencia());
					c.setNumeroconta(conta.getNumeroConta());
					c.setTipoconta(Integer.valueOf(conta.getTipoConta()));
					c.setDescricaotipoconta(conta.getDescricaoTipoConta());
					c.setSituacao(conta.getSituacao());
					c.setDescricaosituacao(conta.getDescricaoSituacao());
					c.setAbertura(conta.getAbertura().toGregorianCalendar().getTime());
					c.setChamado(chamado);

					contaService.save(c);
				}
			}

			log.info("Fim consulta dados conta");
		}

		if (chamado.getSubmotivo().getImp013() && chamado.getCpf() != null) {
			log.info("Consulta status senha do cpf: {}", chamado.getCpf());

			chamado.setStatussenha(imp003Service.consultaStatusPin(chamado.getCpf()));

			log.info("Fim consulta status senha");
		}
		
		chamadoService.save(chamado);
	}
}
