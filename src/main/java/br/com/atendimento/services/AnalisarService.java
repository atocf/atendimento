package br.com.atendimento.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.atendimento.dto.analisar.ResponseAnalisarDto;
import br.com.atendimento.dto.analisar.ResponseAnalisarMassaDto;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Conta;
import br.com.atendimento.entity.SubMotivo;
import br.com.atendimento.services.utils.AnalisarUtilsServices;
import br.com.atendimento.util.ChamadoUtils;
import br.com.atendimento.util.DataUtils;

@Service
public class AnalisarService {

	@Autowired
	private ChamadoService chamadoService;

	@Autowired
	private AnalisarUtilsServices analisarUtilsServices;

	@Autowired
	private Dgp018Service dgp018Service;

	@Autowired
	private ContaService contaService;

	@Autowired
	private SincronismoService sincronismoService;

	@Autowired
	private OnboardingPjtService onboardingPjtService;

	@Autowired
	private Pjdp007Service pjdp007Service;

	@Autowired
	private IntegracaoService integracaoService;

	private static final Logger log = LoggerFactory.getLogger(AnalisarService.class);

	public ResponseAnalisarDto ajustarStatus() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();
		analisarUtilsServices.validarStatusChamados("DEVOLVER", "DEVOLVIDO");
		analisarUtilsServices.validarStatusChamados("ENCAMINHADO", "FECHADO");
		analisarUtilsServices.validarStatusChamados("FECHAR", "FECHADO");
		return resp;
	}

	public ResponseAnalisarDto analisar() {

		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		resp = validaPjnoPf(resp);
		resp = validarPixComplementar(resp);
//		resp = validarPj(resp);
		resp = limpezaRedisPj(resp);
		resp = limpezaRedisPf(resp);
		resp = validaAberturaConta(resp);
//		resp = validaToken(resp);
//		ResponseAnalisarDto respTokenSenhaBloqqueada = validaSenhaBloqueada();
//		ResponseAnalisarDto respSenhaEletronica = validarSenhaEletronica();

		return resp;
	}

	private ResponseAnalisarDto validaPjnoPf(ResponseAnalisarDto resp) {
		log.info("Buscando lista de chamados para devolver para fila BMG Empresas");

		List<Chamado> devolverBmgEmpresa = chamadoService
				.findByStatusintergrallAndSubmotivo_EquipeAndCnpjIsNotNull("Pendente", "BACKOFFICE DÍGITAL");

		if (devolverBmgEmpresa.size() > 0) {
			for (Chamado c : devolverBmgEmpresa) {
				analisarUtilsServices.atualizarChamado(c, "DEVOLVER", 14L, 1L, 2L, true, false);
			}
			resp.setTotal_devolver_fila_errada_bmg_empresa(resp.getTotal_devolver_fila_errada_bmg_empresa() + devolverBmgEmpresa.size());
		}
		return resp;
	}

	private ResponseAnalisarDto validarPixComplementar(ResponseAnalisarDto resp) {
		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
				"Pendente", "BACKOFFICE DÍGITAL", "VALOR NÃO CREDITADO - PIX COMPLEMENTAR");

		if (list.size() > 0) {
			for (Chamado c : list) {
				analisarUtilsServices.atualizarChamado(c, "DEVOLVER", 14L, 2L, 6L, true, false);
			}
			resp.setTotal_devolver_fila_errada_conta_corrente(resp.getTotal_devolver_fila_errada_conta_corrente() + list.size());
		}

		return resp;
	}

	private ResponseAnalisarDto validarPj(ResponseAnalisarDto resp) {
		log.info("Inicio do processo analise dos chamados pjtinha");

		// 24    CONTA PAGAMENTO PJ - Consulta pelo CNPJ
		// 30    CONTA PAGAMENTO BMG - PJ MEI - Consulta pelo CNPJ
		// 46    CONTA PAGAMENTO GRANITO PF - Consulta pelo CPF

		int filaerrada = 0;

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_Equipe("Pendente", "BMG EMPRESAS");

		if (list.size() > 0) {
			for (Chamado c : list) {
				if (!(c.getSubmotivo().getNome().equals("PROBLEMAS NO ONBOARDING"))) {
					Boolean contaValida = false;
					if (c.getConta().size() > 0) {
						for (Conta conta : c.getConta()) {
							if (conta.getTipoconta() == 24 || conta.getTipoconta() == 30
									|| conta.getTipoconta() == 46) {
								contaValida = true;
							}
						}
					}
					if (!(contaValida)) {
						analisarUtilsServices.atualizarChamado(c, "DEVOLVER", 14L, 109L, 6L, true, false);
						filaerrada++;
					}
				}
			}
		}

		resp.setTotal_devolver_fila_errada(resp.getTotal_devolver_fila_errada() + filaerrada);
		return resp;
	}

	private ResponseAnalisarDto limpezaRedisPj(ResponseAnalisarDto resp) {

		List<Chamado> list = new ArrayList<>();

		List<Chamado> list1 = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Nome("Pendente",
				"BMG EMPRESAS", "PROBLEMAS NO ONBOARDING");
		List<Chamado> list2 = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Nome("Pendente",
				"BMG EMPRESAS", "ERRO - NÃO EXISTEM CONTAS");

		list.addAll(list1);
		list.addAll(list2);

		if (list.size() > 0) {
			for (Chamado c : list) {
				if (c.getCpf() != null) {
					analisarUtilsServices.deleteRedis(c.getCpf());
				}
				if (c.getCnpj() != null) {
					analisarUtilsServices.deleteRedis(c.getCnpj());
				}
			}
			resp.setTotal_limpo_redis(resp.getTotal_limpo_redis() + list.size());
		}

		return resp;
	}
	
	private ResponseAnalisarDto limpezaRedisPf(ResponseAnalisarDto resp) {

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Nome("Pendente",
				"BACKOFFICE DÍGITAL", "PROBLEMAS PARA ATIVAR DEVICE ÚNICO");
		
		if (list.size() > 0) {
			for (Chamado c : list) {
				if (c.getCpf() != null) {
					analisarUtilsServices.deleteRedis(c.getCpf());
				}
			}
			resp.setTotal_limpo_redis(resp.getTotal_limpo_redis() + list.size());
		}

		return resp;
	}
	
	private ResponseAnalisarDto validaAberturaConta(ResponseAnalisarDto resp) {

		int conta_existente = 0;
		int total_fechar = 0;
		int limpar_redis = 0;

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
				"Pendente", "BACKOFFICE DÍGITAL", "CLIENTE NÃO CONSEGUE FINALIZAR ABERTURA DE CONTA");

		if (list.size() > 0) {
			for (Chamado chamado : list) {
				if (chamado.getConta().size() > 0 && chamado.getStatus() == null) {

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
							chamado.setStatussenha("Ativar Temporaria");
							analisarUtilsServices.atualizarChamado(chamado, null, null, 52L, null, true, false);
							total_fechar++;
						} else {
							analisarUtilsServices.atualizarChamado(chamado, "DEVOLVER", null, 49L, 1L, true, false);
							conta_existente++;
						}
						analisarUtilsServices.deleteRedis(chamado.getCpf());
						limpar_redis++;
					} else if (bloqueada) {
						analisarUtilsServices.atualizarChamado(chamado, "DEVOLVER", null, 49L, 1L, true, false);
						conta_existente++;
						analisarUtilsServices.deleteRedis(chamado.getCpf());
						limpar_redis++;
					} else if (desativada) {
						analisarUtilsServices.resetOnboarding(chamado, 51L, true, false);
						total_fechar++;
					}

				} else {
					analisarUtilsServices.resetOnboarding(chamado, 50L, true, false);
					total_fechar++;
				}
			}
		}
		
		resp.setTotal_limpo_redis(resp.getTotal_limpo_redis() + limpar_redis);
		resp.setTotal_devolver_atendimento(resp.getTotal_devolver_atendimento() + conta_existente);
		resp.setTotal_fechar(resp.getTotal_fechar() + total_fechar);
		return resp;
	}

	public ResponseAnalisarDto massa(@Valid ResponseAnalisarMassaDto massa) throws ParseException {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();

		Date data = DataUtils.convert(massa.getData(), DataUtils.formatoData);

		int fechar = 0;

		for (SubMotivo s : massa.getSubMotivos()) {
			List<Chamado> list = chamadoService
					.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNullAndDataabertura(
							"Pendente", s.getEquipe(), s.getNome(), data);
			if (list.size() > 0) {
				for (Chamado c : list) {
					if (!(ChamadoUtils.prioritario(c.getCanalatendimento()))) {
						c.setMassa(true);
						analisarUtilsServices.atualizarChamado(c, "FECHAR", massa.getAnalista(), massa.getCausa_raiz(),
								massa.getMsg(), false, true);
						fechar++;
					}
				}
			}
		}

		resp.setTotal_fechar(fechar);
		return resp;
	}

	private ResponseAnalisarDto validaToken(ResponseAnalisarDto resp) {

		List<Chamado> list = new ArrayList<>();

		List<Chamado> list1 = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
				"Pendente", "BACKOFFICE DÍGITAL", "CLIENTE NÃO RECEBE TOKEN");

		if (list1.size() > 0) {
			list.addAll(list1);
		}

		List<Chamado> list2 = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
				"Pendente", "BACKOFFICE DÍGITAL", "ERRO DE TOKEN");

		if (list2.size() > 0) {
			list.addAll(list2);
		}

		if (list.size() > 0) {
			for (Chamado c : list) {
				if (c.getStatus() == null) {
					// validar e-mail valido
					// validar celular valido
				}
			}
		}

		return resp;
	}

	public ResponseAnalisarDto sincronizar() throws ParseException {
		log.info("Inicio de sincronismo PF e PJ");

		ResponseAnalisarDto resp = new ResponseAnalisarDto();
		int countSinc = 0;

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_Sincronismo("Pendente", true);

		if (list.size() > 0) {
			for (Chamado c : list) {
				if (c.getConta() != null && c.getConta().size() > 0) {
					for (Conta conta : c.getConta()) {
						if (conta.getDescricaosituacao().equals("LIBERADA") && !(conta.getSincronizado())) {
							if (sincronismoService.sincronizarConta(conta.getAgencia(), conta.getNumeroconta())) {
								conta.setSincronizado(true);
								contaService.save(conta);
								countSinc++;
							}
						}
					}
				}
			}
		}

		resp.setTotal_sincronizado(countSinc);
		return resp;
	}

//	private ResponseAnalisarDto validarSenhaEletronica() {
//		ResponseAnalisarDto resp = new ResponseAnalisarDto();
//
//		int devolver = 0;
//		int fechar = 0;

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

//		resp.setTotal_devolver_atendimento(devolver);
//		resp.setTotal_fechar(fechar);
//		return resp;
//	}

//	private ResponseAnalisarDto validaSenhaBloqueada() {
//		ResponseAnalisarDto resp = new ResponseAnalisarDto();
//
//		int senha_bloqueada = 0;
//
//		List<Chamado> list1 = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
//				"Pendente", "BACKOFFICE DÍGITAL", "CLIENTE NÃO RECEBE TOKEN");
//
//		List<Chamado> list2 = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(
//				"Pendente", "BACKOFFICE DÍGITAL", "ERRO DE TOKEN");
//		if (list1.size() > 0) {
//			if (list2.size() > 0) {
//				list1.addAll(list2);
//			}
//			for (Chamado c : list1) {
//				if (c.getStatussenha() != null && c.getStatussenha().equals("Bloqueado")) {
//					atualizarChamado(c, "DEVOLVER", null, 53L, 5L);
//					senha_bloqueada++;
//				}
//			}
//		}
//		resp.setTotal_devolver_atendimento(senha_bloqueada);
//		return resp;
//	}

}
