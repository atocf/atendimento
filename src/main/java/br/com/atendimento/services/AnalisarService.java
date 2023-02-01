package br.com.atendimento.services;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.atendimento.dto.analisar.ResponseAnalisarDto;
import br.com.atendimento.dto.analisar.ResponseDevolverDto;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Conta;

@Service
public class AnalisarService {

	@Autowired
	private ChamadoService chamadoService;

	@Autowired
	private StatusService statusService;

	@Autowired
	private AnalistaService analistaService;

	@Autowired
	private CausaRaizService causaRaizService;

	@Autowired
	private MsgService msgService;

	@Value("${chromedriver.path}")
	private String chromedriverPath;

	private static final Logger log = LoggerFactory.getLogger(AnalisarService.class);

	public ResponseAnalisarDto analisar() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		ResponseAnalisarDto respAberturaConta = validaAberturaConta();
		ResponseAnalisarDto respPjnoPj = validaPjnoPj();

		resp.setTotal_devolver_fila_errada_bmg_empresa(resp.getTotal_devolver_fila_errada_bmg_empresa()
				+ respPjnoPj.getTotal_devolver_fila_errada_bmg_empresa());
		resp.setTotal_devolver_atendimento_conta_existente(resp.getTotal_devolver_atendimento_conta_existente()
				+ respAberturaConta.getTotal_devolver_atendimento_conta_existente());

		return resp;
	}

	private ResponseAnalisarDto validaAberturaConta() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		int conta_existente = 0;

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Nome("Pendente",
				"BACKOFFICE DÍGITAL", "CLIENTE NÃO CONSEGUE FINALIZAR ABERTURA DE CONTA");
		if (list.size() > 0) {
			for (Chamado chamado : list) {
				if (chamado.getConta().size() > 0) {
					for (Conta conta : chamado.getConta()) {
						if (!(conta.getDescricaosituacao().equals("DESATIVADA"))) {
							chamado.setStatus(statusService.findByNome("DEVOLVER").get());
							chamado.setCausaraiz(causaRaizService.findById(49L).get());
							chamado.setDatastatus(new Date());
							chamado.setMsg(msgService.findById(1L).get());
							chamadoService.save(chamado);
							conta_existente++;
						}
					}
					if (chamado.getStatus() == null) {
						//resetOnboarding(chamado);
					}
				} else {
					//resetOnboarding(chamado);
				}
			}
		}

		return resp;
	}

	private ResponseAnalisarDto validaPjnoPj() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		log.info("Buscando lista de chamados para devolver para fila BMG Empresas");

		List<Chamado> devolverBmgEmpresa = chamadoService
				.findByStatusintergrallAndSubmotivo_EquipeAndCnpjIsNotNull("Pendente", "BACKOFFICE DÍGITAL");

		resp.setTotal_devolver_fila_errada_bmg_empresa(devolverBmgEmpresa.size());

		if (devolverBmgEmpresa.size() > 0) {
			for (Chamado c : devolverBmgEmpresa) {
				c.setStatus(statusService.findByNome("DEVOLVER").get());
				c.setAnalista(analistaService.findById(14L).get());
				c.setCausaraiz(causaRaizService.findById(1L).get());
				c.setDatastatus(new Date());
				c.setMsg(msgService.findById(2L).get());
				chamadoService.save(c);
			}
		}

		return resp;
	}
}
