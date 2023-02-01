package br.com.atendimento.services;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.atendimento.dto.analisar.ResponseAnalisarDto;
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
	private Dgp018Service dgp018Service;
	
	@Autowired
	private Dgp180Service dgp180Service;
	
	@Autowired
	private Imp013Service imp013Service;
	
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
		resp.setTotal_chamado_analisado_fechar(resp.getTotal_chamado_analisado_fechar() + respAberturaConta.getTotal_chamado_analisado_fechar());

		return resp;
	}

	private ResponseAnalisarDto validaAberturaConta() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		int conta_existente = 0;
		int total_fechar = 0;

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Nome("Pendente",
				"BACKOFFICE DÍGITAL", "CLIENTE NÃO CONSEGUE FINALIZAR ABERTURA DE CONTA");
		if (list.size() > 0) {
			for (Chamado chamado : list) {
				if (chamado.getCpf() != null) {
					if (chamado.getConta().size() > 0) {
						for (Conta conta : chamado.getConta()) {
							if (conta.getDescricaosituacao().equals("LIBERADA")) {
								String st = dgp018Service.consultaStatus(chamado.getCpf());
								if (st != null && st.equals("SENHA_TEMPORARIA_PROMOVIDA")) {
									chamado.setStatussenha("Ativar Temporaria");
									atualizarChamado(chamado, null, null, 52L, null);
									total_fechar++;
								} else {
									atualizarChamado(chamado, "DEVOLVER", null, 49L, 1L);
									conta_existente++;
								}
							} else if (conta.getDescricaosituacao().equals("BLOQUEADA")) {
								atualizarChamado(chamado, "DEVOLVER", null, 49L, 1L);
								conta_existente++;
							} else if (conta.getDescricaosituacao().equals("DESATIVADA")) {
								resetOnboarding(chamado, 51L);
								total_fechar++;
							}
						}
					} else {
						resetOnboarding(chamado, 50L);
						total_fechar++;
					}
				}
			}
		}

		resp.setTotal_devolver_atendimento_conta_existente(conta_existente);
		resp.setTotal_chamado_analisado_fechar(total_fechar);
		return resp;
	}

	private void resetOnboarding(Chamado chamado, Long cauzaRaiz) {
		dgp018Service.limpezaCadastro(chamado.getCpf());
		dgp180Service.deleteRedis(chamado.getCpf());
		imp013Service.inativarPinEletronico(chamado.getCpf());
		atualizarChamado(chamado, "FECHAR", null, cauzaRaiz, 4L);	
	}

	private void atualizarChamado(Chamado chamado, String status, Long analista, Long cauzaRaiz, Long msg) {
		if(status != null) {
			chamado.setStatus(statusService.findByNome(status).get());
		}
		if (analista != null) {
			chamado.setAnalista(analistaService.findById(analista).get());
		}
		chamado.setCausaraiz(causaRaizService.findById(cauzaRaiz).get());
		chamado.setDatastatus(new Date());
		if(msg != null) {
			chamado.setMsg(msgService.findById(msg).get());
		}
		chamadoService.save(chamado);
	}

	private ResponseAnalisarDto validaPjnoPj() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		log.info("Buscando lista de chamados para devolver para fila BMG Empresas");

		List<Chamado> devolverBmgEmpresa = chamadoService
				.findByStatusintergrallAndSubmotivo_EquipeAndCnpjIsNotNull("Pendente", "BACKOFFICE DÍGITAL");

		if (devolverBmgEmpresa.size() > 0) {
			for (Chamado c : devolverBmgEmpresa) {
				atualizarChamado(c, "DEVOLVER", 14L, 1L, 2L);
			}
		}
		resp.setTotal_devolver_fila_errada_bmg_empresa(devolverBmgEmpresa.size());
		return resp;
	}
}
