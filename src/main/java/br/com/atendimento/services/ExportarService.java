package br.com.atendimento.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.atendimento.dto.export.ExportDto;
import br.com.atendimento.entity.Cartao;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Conta;
import br.com.atendimento.excel.ChamadoExcelExporter;
import br.com.atendimento.util.DataUtils;

@Service
public class ExportarService {

	@Autowired
	private ChamadoService chamadoService;

	@Autowired
	private CartaoService cartaoService;

	private static final Logger log = LoggerFactory.getLogger(ExportarService.class);

	public void exportarSincronismo(HttpServletResponse response) throws IOException, ParseException {
		log.info("Inicio do processo de gerar excel dos chamados para sincronismo PF e PJ");

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_Sincronismo("Pendente", true);

		if (list.size() > 0) {
			ChamadoExcelExporter excelExporter = new ChamadoExcelExporter(list, null);
			String[] namesCell = new String[] { "CPF ou CNPJ", "Agência", "Conta" };
			excelExporter.export(response, "Sincronizar", namesCell);
		}
	}

	public void exportarAtendimentoPj(HttpServletResponse response) throws IOException, ParseException {
		log.info("Inicio do processo de gerar excel dos chamados para atendimento PJ");

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_Equipe("Pendente", "BMG EMPRESAS");

		if (list.size() > 0) {
			ChamadoExcelExporter excelExporter = new ChamadoExcelExporter(null, returnListExportDto(list));
			String[] namesCell = new String[] { "ANALISTA", "CANAL DE ATENDIMENTO", "SUBMOTIVO", "REABERTURA",
					"OCORRENCIA", "PROTOCOLO", "CPF", "CNPJ", "ABRIR", "FECHAR", "DEVOLVER", "KIBANA", "CARD", "SQUAD",
					"STATUS", "DATA-STATUS", "OBSERVAÇÃO", "CAUSA RAIZ", "DATA ABERTURA", "DATA VENCIMENTO",
					"DESCRIÇÃO", "NOME", "STATUS SENHA", "EMAIL", "TELEFONE", "TELEFONE_SMS",
					"ULTIMAL_ATUALIZACAO_CADASTRAL", "ESCOPO", "CONTA", "CARTOES", "MSG" };
			excelExporter.export(response, "Atendimento", namesCell);
		}
	}

	public void exportarAtendimentoPfDuplicado(HttpServletResponse response) throws IOException, ParseException {
		log.info("Inicio do processo de gerar excel dos chamados duplicados para atendimento PF");

		List<Chamado> list = chamadoService.buscaListaOcorrenciaCpf("Pendente", "BACKOFFICE DÍGITAL");

		if (list.size() > 0) {
			ChamadoExcelExporter excelExporter = new ChamadoExcelExporter(null, returnListExportDto(list));
			String[] namesCell = new String[] { "ANALISTA", "CANAL DE ATENDIMENTO", "SUBMOTIVO", "REABERTURA",
					"OCORRENCIA", "PROTOCOLO", "CPF", "ABRIR", "FECHAR", "DEVOLVER", "KIBANA", "CARD", "SQUAD",
					"STATUS", "DATA-STATUS", "OBSERVAÇÃO", "CAUSA RAIZ", "DATA ABERTURA", "DATA VENCIMENTO",
					"DESCRIÇÃO", "NOME", "STATUS SENHA", "EMAIL", "TELEFONE", "TELEFONE_SMS",
					"ULTIMAL_ATUALIZACAO_CADASTRAL", "ESCOPO", "CONTA", "CARTOES", "MSG" };
			excelExporter.export(response, "Atendimento", namesCell);
		}
	}

	public void exportarAtendimentoPfPriorizado(HttpServletResponse response) throws IOException, ParseException {
		log.info("Inicio do processo de gerar excel dos chamados priorizados para atendimento PF");

		List<Chamado> list = chamadoService.buscaListaOcorrenciaPrioritarias("Pendente", "BACKOFFICE DÍGITAL");

		if (list.size() > 0) {
			ChamadoExcelExporter excelExporter = new ChamadoExcelExporter(null,
					returnListExportDtoLimpoDuplicados(list));
			String[] namesCell = new String[] { "ANALISTA", "CANAL DE ATENDIMENTO", "SUBMOTIVO", "REABERTURA",
					"OCORRENCIA", "PROTOCOLO", "CPF", "ABRIR", "FECHAR", "DEVOLVER", "KIBANA", "CARD", "SQUAD",
					"STATUS", "DATA-STATUS", "OBSERVAÇÃO", "CAUSA RAIZ", "DATA ABERTURA", "DATA VENCIMENTO",
					"DESCRIÇÃO", "NOME", "STATUS SENHA", "EMAIL", "TELEFONE", "TELEFONE_SMS",
					"ULTIMAL_ATUALIZACAO_CADASTRAL", "ESCOPO", "CONTA", "CARTOES", "MSG" };
			excelExporter.export(response, "Atendimento", namesCell);
		}
	}

	public void exportarAtendimentoPfGeral(HttpServletResponse response) throws IOException, ParseException {
		log.info("Inicio do processo de gerar excel dos chamados para atendimento PF");

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Pix("Pendente",
				"BACKOFFICE DÍGITAL", false);

		if (list.size() > 0) {
			ChamadoExcelExporter excelExporter = new ChamadoExcelExporter(null,
					returnListExportDtoLimpoDuplicados(list));
			String[] namesCell = new String[] { "ANALISTA", "CANAL DE ATENDIMENTO", "SUBMOTIVO", "REABERTURA",
					"OCORRENCIA", "PROTOCOLO", "CPF", "ABRIR", "FECHAR", "DEVOLVER", "KIBANA", "CARD", "SQUAD",
					"STATUS", "DATA-STATUS", "OBSERVAÇÃO", "CAUSA RAIZ", "DATA ABERTURA", "DATA VENCIMENTO",
					"DESCRIÇÃO", "NOME", "STATUS SENHA", "EMAIL", "TELEFONE", "TELEFONE_SMS",
					"ULTIMAL_ATUALIZACAO_CADASTRAL", "ESCOPO", "CONTA", "CARTOES", "MSG" };
			excelExporter.export(response, "Atendimento", namesCell);
		}
	}

	public void exportarAtendimentoPfPix(HttpServletResponse response) throws IOException, ParseException {
		log.info("Inicio do processo de gerar excel dos chamados de pix para atendimento PF");

		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Pix("Pendente",
				"BACKOFFICE DÍGITAL", true);

		if (list.size() > 0) {
			ChamadoExcelExporter excelExporter = new ChamadoExcelExporter(null,
					returnListExportDtoLimpoDuplicados(list));
			String[] namesCell = new String[] { "ANALISTA", "CANAL DE ATENDIMENTO", "SUBMOTIVO", "REABERTURA",
					"OCORRENCIA", "PROTOCOLO", "CPF", "ABRIR", "FECHAR", "DEVOLVER", "KIBANA", "CARD", "SQUAD",
					"STATUS", "DATA-STATUS", "OBSERVAÇÃO", "CAUSA RAIZ", "DATA ABERTURA", "DATA VENCIMENTO",
					"DESCRIÇÃO", "NOME", "STATUS SENHA", "EMAIL", "TELEFONE", "TELEFONE_SMS",
					"ULTIMAL_ATUALIZACAO_CADASTRAL", "ESCOPO", "CONTA", "CARTOES", "MSG" };
			excelExporter.export(response, "Atendimento", namesCell);
		}
	}

	private List<ExportDto> returnListExportDtoLimpoDuplicados(List<Chamado> list) throws ParseException {

		List<Chamado> listDuplicados = chamadoService.buscaListaOcorrenciaCpf("Pendente", "BACKOFFICE DÍGITAL");
		List<ExportDto> exportDtoDuplicados = returnListExportDto(listDuplicados);

		List<ExportDto> exportDtoList = returnListExportDto(list);

		exportDtoList.removeAll(exportDtoDuplicados);

		return exportDtoList;
	}

	private List<ExportDto> returnListExportDto(List<Chamado> list) throws ParseException {
		ArrayList<ExportDto> listExportDto = new ArrayList<>();
		for (Chamado chamado : list) {
			ExportDto e = new ExportDto();
			e.setAnalista(chamado.getAnalista().getNome());
			e.setC_atendimento(chamado.getCanalatendimento());
			e.setSub_motivo(chamado.getSubmotivo().getNome());
			if (chamado.getSubmotivo().getKibana() != null) {
				e.setKibana(chamado.getSubmotivo().getKibana().getLink());
			}
			e.setEquipe(chamado.getSubmotivo().getEquipe());
			e.setReabertura(chamado.getReabertura());
			e.setOcorrencia(chamado.getOcorrencia());
			e.setProtocolo(chamado.getProtocolo());
			e.setCpf(chamado.getCpf());
			e.setCnpj(chamado.getCnpj());
			e.setCard(chamado.getCard());
			if (chamado.getSquad() != null) {
				e.setSquad(chamado.getSquad().getNome());
			} else {
				e.setSquad("");
			}
			if (chamado.getStatus() != null) {
				e.setStatus(chamado.getStatus().getNome());
			} else {
				e.setStatus("");
			}
			e.setData_status(DataUtils.format(chamado.getDatastatus(), DataUtils.formatoData));
			e.setObservacao(chamado.getObservacao());
			if (chamado.getCausaraiz() != null) {
				e.setCausa_raiz(chamado.getCausaraiz().getNome());
			} else {
				e.setCausa_raiz("");
			}
			e.setDt_aberta(DataUtils.format(chamado.getDataabertura(), DataUtils.formatoData));
			e.setDt_vencimento(DataUtils.format(chamado.getDatavencimento(), DataUtils.formatoData));
			e.setDescricao(chamado.getDescricao());
			e.setNome(chamado.getNome());
			e.setStatus_senha(chamado.getStatussenha());
			e.setEmail(chamado.getEmail());
			e.setTelefone(chamado.getTelefone());
			e.setTelefone_sms(chamado.getStatussms());
			e.setAtualizacao_cadastral(DataUtils.format(chamado.getDataatualizacaocadastral(), DataUtils.formatoData));
			e.setEscopo(chamado.getEscopo());
			if (chamado.getConta().size() > 0) {
				String strConta = "";
				for (Conta conta : chamado.getConta()) {
					strConta = strConta + "Tipo:" + conta.getTipoconta() + "-" + conta.getDescricaosituacao() + "-"
							+ conta.getAgencia() + "/" + conta.getNumeroconta() + " | ";
				}
				e.setConta(strConta);
			} else {
				e.setCartoes("Não encontrada");
			}

			List<Cartao> cartoes = cartaoService.findByChamado_Ocorrencia(chamado.getOcorrencia());
			if (cartoes.size() > 0) {
				String strCartao = "";
				for (Cartao cartao : cartoes) {
					strCartao = strCartao + "(" + cartao.getDescricaostatuscartao() + ") "
							+ cartao.getNumerocartao().substring(cartao.getNumerocartao().length() - 4) + " "
							+ cartao.getDescricaotipocartao() + " | ";
				}
				e.setCartoes(strCartao);
			} else {
				e.setCartoes("Não encontrada");
			}
			if (chamado.getMsg() != null) {
				e.setMsg(chamado.getMsg().getDescricao());
			}

			listExportDto.add(e);
		}
		return listExportDto;
	}
}
