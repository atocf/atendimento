package br.com.atendimento.services;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.atendimento.dto.analisar.ResponseAnalisarDto;
import br.com.atendimento.dto.analisar.ResponseAnalisarMassaDto;
import br.com.atendimento.entity.Cartao;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Conta;
import br.com.atendimento.entity.SubMotivo;
import br.com.atendimento.util.DataUtils;

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

	@Autowired
	private CartaoService cartaoService;

	@Value("${chromedriver.path}")
	private String chromedriverPath;

	private static final Logger log = LoggerFactory.getLogger(AnalisarService.class);

	public ResponseAnalisarDto analisar() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		ResponseAnalisarDto respAberturaConta = validaAberturaConta();
		ResponseAnalisarDto respPjnoPj = validaPjnoPj();
		ResponseAnalisarDto respTokenSenhaBloqqueada = validaSenhaBloqueada();
		ResponseAnalisarDto respPixComplementar = validarPixComplementar();
		ResponseAnalisarDto respSenhaEletronica = validarSenhaEletronica();

		resp.setTotal_fechar(
				resp.getTotal_fechar() + respAberturaConta.getTotal_fechar() + respSenhaEletronica.getTotal_fechar());

		resp.setTotal_devolver_fila_errada_bmg_empresa(resp.getTotal_devolver_fila_errada_bmg_empresa()
				+ respPjnoPj.getTotal_devolver_fila_errada_bmg_empresa());

		resp.setTotal_devolver_fila_errada_conta_corrente(resp.getTotal_devolver_fila_errada_conta_corrente()
				+ respPixComplementar.getTotal_devolver_fila_errada_conta_corrente());

		resp.setTotal_devolver_atendimento(
				resp.getTotal_devolver_atendimento() + respAberturaConta.getTotal_devolver_atendimento()
						+ respTokenSenhaBloqqueada.getTotal_devolver_atendimento()
						+ respSenhaEletronica.getTotal_devolver_atendimento());

		return resp;
	}

	private void atualizarChamado(Chamado chamado, String status, Long analista, Long cauzaRaiz, Long msg) {
		if (status != null) {
			chamado.setStatus(statusService.findByNome(status).get());
		}
		if (analista != null) {
			chamado.setAnalista(analistaService.findById(analista).get());
		}
		chamado.setCausaraiz(causaRaizService.findById(cauzaRaiz).get());
		chamado.setDatastatus(new Date());
		if (msg != null) {
			chamado.setMsg(msgService.findById(msg).get());
		}
		chamadoService.save(chamado);
	}

	private ResponseAnalisarDto validarSenhaEletronica() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		int devolver = 0;
		int fechar = 0;

//		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
//				"Pendente", "BACKOFFICE DÍGITAL", "CLIENTE NÃO CONSEGUE CADASTRAR A SENHA ELETRÔNICA");
//
//		if (list.size() > 0) {
//			for (Chamado c : list) {
//				if (c.getStatussenha().equals("Ativo")) {
//					if (validarTexto(c.getDescricao(), "alterar", "alteração")) {
//						atualizarChamado(c, "DEVOLVER", 14L, 20L, 8L);
//					} else {
//						atualizarChamado(c, "DEVOLVER", 14L, 60L, 7L);
//					}
//					devolver++;
//				} else if (c.getStatussenha().equals("Bloqueado")) {
//					if (validarTexto(c.getDescricao(), "alterar", "alteração")) {
//						atualizarChamado(c, "DEVOLVER", 14L, 20L, 8L);
//					} else {
//						atualizarChamado(c, "DEVOLVER", 14L, 16L, 7L);
//					}
//					devolver++;
//				} else if (c.getStatussenha().equals("Inativo")) {
//					if (c.getConta().size() > 0) {
//						for (Conta conta : c.getConta()) {
//							if (conta.getDescricaosituacao().equals("LIBERADA")) {
//								// Ativar senha -- ver para integrar
//								atualizarChamado(c, "FECHAR", null, 19L, 7L);
//								fechar++;
//							} else if (conta.getDescricaosituacao().equals("BLOQUEADA")) {
//								// Ativar senha -- ver para integrar
//								atualizarChamado(c, "FECHAR", null, 19L, 7L);
//								fechar++;
//							}
//						}
//					}
//					fechar++;
//				} else if (c.getStatussenha().equals("Temporaria")) {
//					for (Conta conta : c.getConta()) {
//						if (conta.getDescricaosituacao().equals("LIBERADA")) {
//							// Ativar senha temporaria -- ver para integrar
//							atualizarChamado(c, "FECHAR", null, 22L, 7L);
//							fechar++;
//						} else if (conta.getDescricaosituacao().equals("BLOQUEADA")) {
//							// Ativar senha temporaria -- ver para integrar
//							atualizarChamado(c, "FECHAR", null, 22L, 7L);
//							fechar++;
//						}
//					}
//					fechar++;
//				} else if (c.getStatussenha().equals("Inexistente")) {
//					if (c.getConta().size() > 0) {
//						for (Conta conta : c.getConta()) {
//							if (conta.getDescricaosituacao().equals("LIBERADA")) {
//								// Criar senha -- ver para integrar
//								atualizarChamado(c, "FECHAR", null, 23L, 7L);
//								fechar++;
//							} else if (conta.getDescricaosituacao().equals("BLOQUEADA")) {
//								// Criar senha -- ver para integrar
//								atualizarChamado(c, "FECHAR", null, 23L, 7L);
//								fechar++;
//							}
//						}
//					} else {
//						List<Cartao> cartoes = cartaoService.findByChamado_Ocorrencia(c.getOcorrencia());
//						if (cartoes.size() > 0) {
//							for (Cartao cartao : cartoes) {
//								if (cartao.getDescricaotipocartao().equals("BMG Multi")) {
//									atualizarChamado(c, "FECHAR", null, 61L, 9L);
//									fechar++;
//								} else if (cartao.getDescricaotipocartao().equals("BMG Card")) {
//									atualizarChamado(c, "FECHAR", null, 62L, 8L);
//									fechar++;
//								}
//							}
//						}
//					}
//				}
//			}
//		}

		resp.setTotal_devolver_atendimento(devolver);
		resp.setTotal_fechar(fechar);
		return resp;
	}

	private boolean validarTexto(String descricao, String str1, String str2) {
		if (descricao.contains(str1) || descricao.contains(str2)) {
			return true;
		}
		return false;
	}

	private ResponseAnalisarDto validarPixComplementar() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
				"Pendente", "BACKOFFICE DÍGITAL", "VALOR NÃO CREDITADO - PIX COMPLEMENTAR");

		if (list.size() > 0) {
			for (Chamado c : list) {
				atualizarChamado(c, "DEVOLVER", 14L, 2L, 6L);
			}
			resp.setTotal_devolver_fila_errada_conta_corrente(list.size());
		}

		return resp;
	}

	private ResponseAnalisarDto validaSenhaBloqueada() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		int senha_bloqueada = 0;

		List<Chamado> list1 = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
				"Pendente", "BACKOFFICE DÍGITAL", "CLIENTE NÃO RECEBE TOKEN");

		List<Chamado> list2 = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
				"Pendente", "BACKOFFICE DÍGITAL", "ERRO DE TOKEN");
		if (list1.size() > 0) {
			if (list2.size() > 0) {
				list1.addAll(list2);
			}
			for (Chamado c : list1) {
				if (c.getStatussenha() != null && c.getStatussenha().equals("Bloqueado")) {
					atualizarChamado(c, "DEVOLVER", null, 53L, 5L);
					senha_bloqueada++;
				}
			}
		}
		resp.setTotal_devolver_atendimento(senha_bloqueada);
		return resp;
	}

	private ResponseAnalisarDto validaAberturaConta() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		int conta_existente = 0;
		int total_fechar = 0;

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
				"Pendente", "BACKOFFICE DÍGITAL", "CLIENTE NÃO CONSEGUE FINALIZAR ABERTURA DE CONTA");
		if (list.size() > 0) {
			for (Chamado chamado : list) {
				if (chamado.getConta().size() > 0) {

					Boolean liberada = false;
					Boolean bloqueada = false;
					Boolean desativada = false;

					for (Conta conta : chamado.getConta()) {
						if (conta.getDescricaosituacao().equals("LIBERADA")) {
							liberada = true;
						} else if (conta.getDescricaosituacao().equals("BLOQUEADA")) {
							bloqueada = true;
						} else if (conta.getDescricaosituacao().equals("DESATIVADA")) {
							desativada = true;
						}
					}

					if (liberada) {
						String st = dgp018Service.consultaStatus(chamado.getCpf());
						if (st != null && st.equals("SENHA_TEMPORARIA_PROMOVIDA")) {
							// Ativar senha temporaria -- ver para integrar
							chamado.setStatussenha("Ativar Temporaria");
							atualizarChamado(chamado, null, null, 52L, null);
							total_fechar++;
						} else {
							atualizarChamado(chamado, "DEVOLVER", null, 49L, 1L);
							conta_existente++;
						}
					} else if (bloqueada) {
						atualizarChamado(chamado, "DEVOLVER", null, 49L, 1L);
						conta_existente++;
					} else if (desativada) {
						resetOnboarding(chamado, 51L);
						total_fechar++;
					}

				} else {
					resetOnboarding(chamado, 50L);
					total_fechar++;
				}
			}
		}

		resp.setTotal_devolver_atendimento(conta_existente);
		resp.setTotal_fechar(total_fechar);
		return resp;

	}

	private boolean prioritario(String canalatendimento) {
		if (canalatendimento.equals("RECLAME AQUI") || canalatendimento.equals("CONSUMIDOR.GOV")
				|| canalatendimento.equals("BACEN") || canalatendimento.equals("OUVIDORIA CLIENTE")
				|| canalatendimento.equals("PROCON") || canalatendimento.equals("PROCON FONE")) {
			return true;
		}
		return false;
	}

	private void resetOnboarding(Chamado chamado, Long cauzaRaiz) {
		dgp018Service.limpezaCadastro(chamado.getCpf());
		dgp180Service.deleteRedis(chamado.getCpf());
		imp013Service.inativarPinEletronico(chamado.getCpf());
		atualizarChamado(chamado, "FECHAR", null, cauzaRaiz, 4L);
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
			resp.setTotal_devolver_fila_errada_bmg_empresa(devolverBmgEmpresa.size());
		}
		return resp;
	}

	public ResponseAnalisarDto massa(@Valid ResponseAnalisarMassaDto massa) throws ParseException {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();
		
		Date data = DataUtils.convert(massa.getData(), DataUtils.formatoData);
		
		int fechar = 0;
		
		for(SubMotivo s : massa.getSubMotivos()) {
			List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNullAndDataabertura(
					"Pendente", s.getEquipe(), s.getNome(), data);
			if (list.size() > 0) {
				for (Chamado c : list) {
					if(!(prioritario(c.getCanalatendimento()))) {
						atualizarChamado(c, "FECHAR", massa.getAnalista(), massa.getCausa_raiz(), massa.getMsg());
						fechar++;
					}					
				}
			}	
		}
		
		resp.setTotal_fechar(fechar);
		return resp;
	}
}
