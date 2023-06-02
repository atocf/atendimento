package br.com.atendimento.excel;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.atendimento.dto.export.ExportDto;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Conta;
import br.com.atendimento.util.CpfUtils;
import br.com.atendimento.util.DataUtils;

public class ChamadoExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<ExportDto> listExportDto;
	private List<Chamado> listChamado;

	private String abrirLinkJira = "https://bancobmg.atlassian.net/browse/";

	private String abrirLinkInicio = "https://wwws.intergrall.com.br/callcenter/popup.php?programa=flw_pendencias_2.php%3Facao%3DRP%26mk_flag%3DMD%26mk_numero%3DYB%26grupo_acesso%3D1%26ativ_num%3D";
	private String abrirLinkFim = "%26flw_tema%3D004%26flw_tema_pai%3DFYB83%26flw_anexo_arquivo%3DN%26even_num%3D1%26contrato%3D%26ilha%3D%26deonde_prog%3Dfollow%26repre%3DBMG-OPER%26combo_area%3D226%26nivel%3DPNQ%26devolve_nivel_acesso%3D%26flag_altera_dados%3DN%26pend_nivel_acesso_hora_reserva%3D226%26tipo_popup%3DAJ2%26titulo%3DTarefa%20-%20Redirecionar%20Ocorr%EAncia&titulo=Tarefa%20-%20Redirecionar%20Ocorr%EAncia";

	private String direcionarLinkInicioPf = "https://wwws.intergrall.com.br/callcenter/popup.php?programa=flw_pendencias_2_evento.php%3Facao%3DRP%26mk_flag%3DMD%26mk_numero%3DYB%26grupo_acesso%3D1%26ativ_num%3D";
	private String direcionarLinkFimPf = "%26flw_tema%3D038%26flw_tema_pai%3DFYB40%26flw_anexo_arquivo%3DN%26even_num%3D1%26contrato%3D%26ilha%3D%26deonde_prog%3Dfollow%26repre%3DBMG-OPER%26combo_area%3D226%26nivel%3D%26devolve_nivel_acesso%3D%26deonde_baixa_atd%3DPENDENCIA%26func_atualiza_hist_mk%3DatualizaFrameHistoricoMK%28%29%26deonde_pgm%3DPROMOTORA%26flag_altera_dados%3DN%26pend_nivel_acesso_hora_reserva%3D226%26data_cri%3D%26bmg_fcr%3DN%26tipo_popup%3DAJ2%26titulo%3DTarefa%20-%20Redirecionar%20Ocorr%EAncia&titulo=Tarefa%20-%20Finalizar";
	
	private String direcionarLinkInicioPj = "https://wwws.intergrall.com.br/callcenter/popup.php?programa=flw_pendencias_2_evento.php%3Facao%3DRP%26mk_flag%3DMD%26mk_numero%3DYB%26grupo_acesso%3D1%26ativ_num%3D";
	private String direcionarLinkFimPj = "%26flw_tema%3D038%26flw_tema_pai%3DFYB40%26flw_anexo_arquivo%3DN%26even_num%3D1%26contrato%3D%26ilha%3D%26deonde_prog%3Dfollow%26repre%3DBMG-OPER%26combo_area%3D224%26nivel%3D%26devolve_nivel_acesso%3D%26deonde_baixa_atd%3DPENDENCIA%26func_atualiza_hist_mk%3DatualizaFrameHistoricoMK%28%29%26deonde_pgm%3DPROMOTORA%26flag_altera_dados%3DN%26pend_nivel_acesso_hora_reserva%3D224%26data_cri%3D%26bmg_fcr%3DN%26bmg_classificacao_rec%3D%26tipo_popup%3DAJ2%26titulo%3DTarefa%20-%20Redirecionar%20Ocorr%EAncia&titulo=Tarefa%20-%20Redirecionar%20Ocorr%EAncia";
	
	private String fecharLinkInicio = "https://wwws.intergrall.com.br/callcenter/popup.php?programa=flw_pendencias_2_evento.php%3Facao%3DF%26mk_flag%3DMD%26mk_numero%3DYB%26grupo_acesso%3D1%26ativ_num%3D";
	private String fecharLinkFim = "%26flw_tema%3D027%26flw_tema_pai%3DFYBA5%26flw_anexo_arquivo%3DN%26even_num%3D1%26contrato%3D%26ilha%3D%26deonde_prog%3Dfollow%26repre%3DBMG-OPER%26combo_area%3D226%26nivel%3D%26devolve_nivel_acesso%3D%26deonde_baixa_atd%3DPENDENCIA%26func_atualiza_hist_mk%3DatualizaFrameHistoricoMK%28%29%26deonde_pgm%3DPROMOTORA%26flag_altera_dados%3DN%26pend_nivel_acesso_hora_reserva%3D226%26tipo_popup%3DAJ2%26titulo%3DTarefa%20-%20Finalizar&titulo=Tarefa%20-%20Finalizar";

	private static final Logger log = LoggerFactory.getLogger(ChamadoExcelExporter.class);

	public ChamadoExcelExporter(List<Chamado> listChamado, List<ExportDto> listExportDto) {
		this.listChamado = listChamado;
		this.listExportDto = listExportDto;
		workbook = new XSSFWorkbook();
	}

	public void export(HttpServletResponse response, String nameSheet, String[] namesCell)
			throws IOException, ParseException {
		writeHeaderLine(DataUtils.format(new Date(), DataUtils.tracoformatoData), namesCell);
		writeDataLines(nameSheet);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

	private void writeHeaderLine(String nameSheet, String[] namesCell) {
		log.info("Inicio da montagem do header");

		sheet = workbook.createSheet(nameSheet);

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.index);
		style.setTopBorderColor(IndexedColors.BLACK.index);
		style.setLeftBorderColor(IndexedColors.BLACK.index);
		style.setRightBorderColor(IndexedColors.BLACK.index);

		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		font.setColor(IndexedColors.WHITE.index);
		style.setFont(font);

		int rowNumber = 0;

		for (String name : namesCell) {
			createCell(row, rowNumber, name, style, null);
			rowNumber++;
		}

		log.info("Fim da montagem do header");
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style, XSSFHyperlink link) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
		if (link != null) {
			cell.setHyperlink((XSSFHyperlink) link);
		}
	}

	private void writeDataLines(String tipo) throws ParseException {
		log.info("Inicio da leitura linha a linha");

		int rowCount = 1;

		XSSFCreationHelper helper = workbook.getCreationHelper();

		// Padrão
		XSSFFont fontNormal = workbook.createFont();
		CellStyle styleNormal = workbook.createCellStyle();
		styleNormal = getStyleNormal(fontNormal, styleNormal);

		// Descrição - Left
		XSSFFont fontDescricao = workbook.createFont();
		CellStyle styleDescricao = workbook.createCellStyle();
		styleDescricao = getStyleDescricao(fontDescricao, styleDescricao);

		// Link - Color Blue e Underline
		XSSFFont fontLink = workbook.createFont();
		CellStyle styleLink = workbook.createCellStyle();
		styleLink = getStyleLink(fontLink, styleLink);

		// Padrão Prioridade
		XSSFFont fontNormalPrioridade = workbook.createFont();
		CellStyle styleNormalPrioridade = workbook.createCellStyle();
		styleNormalPrioridade = getStyleNormal(fontNormalPrioridade, styleNormalPrioridade);
		styleNormalPrioridade.setFillForegroundColor(IndexedColors.CORAL.index);
		styleNormalPrioridade.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Descrição - Left - Prioridade
		XSSFFont fontDescricaoPrioridade = workbook.createFont();
		CellStyle styleDescricaoPrioridade = workbook.createCellStyle();
		styleDescricaoPrioridade = getStyleDescricao(fontDescricaoPrioridade, styleDescricaoPrioridade);
		styleDescricaoPrioridade.setFillForegroundColor(IndexedColors.CORAL.index);
		styleDescricaoPrioridade.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Link - Color Blue e Underline - Prioridade
		XSSFFont fontLinkPrioridade = workbook.createFont();
		CellStyle styleLinkPrioridade = workbook.createCellStyle();
		styleLinkPrioridade = getStyleLink(fontLinkPrioridade, styleLinkPrioridade);
		styleLinkPrioridade.setFillForegroundColor(IndexedColors.CORAL.index);
		styleLinkPrioridade.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Padrão Vencido
		XSSFFont fontNormalVencido = workbook.createFont();
		CellStyle styleNormalVencido = workbook.createCellStyle();
		styleNormalVencido = getStyleNormal(fontNormalVencido, styleNormalVencido);
		styleNormalVencido.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.index);
		styleNormalVencido.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Descrição - Left - Vencido
		XSSFFont fontDescricaoVencido = workbook.createFont();
		CellStyle styleDescricaoVencido = workbook.createCellStyle();
		styleDescricaoVencido = getStyleDescricao(fontDescricaoVencido, styleDescricaoVencido);
		styleDescricaoVencido.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.index);
		styleDescricaoVencido.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Link - Color Blue e Underline - Vencido
		XSSFFont fontLinkVencido = workbook.createFont();
		CellStyle styleLinkVencido = workbook.createCellStyle();
		styleLinkVencido = getStyleLink(fontLinkVencido, styleLinkVencido);
		styleLinkVencido.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.index);
		styleLinkVencido.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		if (tipo.equals("Sincronizar")) {
			for (Chamado chamado : listChamado) {
				if (chamado.getConta() != null && chamado.getConta().size() > 0) {
					for (Conta conta : chamado.getConta()) {
						if (conta.getDescricaosituacao().equals("LIBERADA")) {
							Row row = sheet.createRow(rowCount++);
							int columnCount = 0;

//							if (chamado.getCpf() != null) {
//								createCell(row, columnCount++, chamado.getCpf(), styleNormal, null);
//								log.info("CPF: {}", chamado.getCpf());
//							} else if (chamado.getCnpj() != null) {
//								createCell(row, columnCount++, chamado.getCnpj(), styleNormal, null);
//								log.info("CNPJ: {}", chamado.getCnpj());
//							}

							createCell(row, columnCount++, conta.getAgencia(), styleNormal, null);
							createCell(row, columnCount++, conta.getNumeroconta(), styleNormal, null);
						}
					}
				}
			}
		} else if (tipo.equals("Atendimento")) {

			CellStyle style = null;
			CellStyle descricaoStyle = null;
			CellStyle linkStyle = null;

			for (ExportDto e : listExportDto) {
				Row row = sheet.createRow(rowCount++);
				int columnCount = 0;

				String linha = validaPrioridadeouVencido(e.getDt_vencimento(), e.getC_atendimento());

				if (linha.equals("Prioridade")) {
					style = styleNormalPrioridade;
					descricaoStyle = styleDescricaoPrioridade;
					linkStyle = styleLinkPrioridade;
				} else if (linha.equals("Vencido")) {
					style = styleNormalVencido;
					descricaoStyle = styleDescricaoVencido;
					linkStyle = styleLinkVencido;
				} else {
					style = styleNormal;
					descricaoStyle = styleDescricao;
					linkStyle = styleLink;
				}

				createCell(row, columnCount++, e.getAnalista(), style, null);
				createCell(row, columnCount++, e.getC_atendimento(), style, null);
				createCell(row, columnCount++, e.getSub_motivo(), style, null);
				createCell(row, columnCount++, e.getReabertura(), style, null);
				createCell(row, columnCount++, e.getOcorrencia(), style, null);
				createCell(row, columnCount++, e.getProtocolo(), style, null);

				if (e.getEquipe().equals("BMG EMPRESAS")) {
					createCell(row, columnCount++, e.getCpf(), style, null);
					createCell(row, columnCount++, e.getCnpj(), style, null);
				} else {
					createCell(row, columnCount++, e.getCpf(), style, null);
				}

				XSSFHyperlink linkAbrir = helper.createHyperlink(HyperlinkType.URL);
				linkAbrir.setAddress(abrirLinkInicio + e.getOcorrencia() + abrirLinkFim);
				createCell(row, columnCount++, "Abrir", linkStyle, linkAbrir);

				XSSFHyperlink linkFechar = helper.createHyperlink(HyperlinkType.URL);
				linkFechar.setAddress(fecharLinkInicio + e.getOcorrencia() + fecharLinkFim);
				createCell(row, columnCount++, "Fechar", linkStyle, linkFechar);
				
				if (e.getEquipe().equals("BACKOFFICE DÍGITAL")) {
					XSSFHyperlink linkDevolver = helper.createHyperlink(HyperlinkType.URL);
					linkDevolver.setAddress(direcionarLinkInicioPf + e.getOcorrencia() + direcionarLinkFimPf);
					createCell(row, columnCount++, "Direcionar", linkStyle, linkDevolver);
				} else if (e.getEquipe().equals("BMG EMPRESAS")) {
					XSSFHyperlink linkDevolver = helper.createHyperlink(HyperlinkType.URL);
					linkDevolver.setAddress(direcionarLinkInicioPj + e.getOcorrencia() + direcionarLinkFimPj);
					createCell(row, columnCount++, "Direcionar", linkStyle, linkDevolver);
				}
				
				if (CpfUtils.valid(e.getCpf()) != null && e.getKibana() != null) {
					String uri = e.getKibana().replaceAll("cpf_var", e.getCpf());
					XSSFHyperlink linkKibana = helper.createHyperlink(HyperlinkType.URL);
					linkKibana.setAddress(uri);
					createCell(row, columnCount++, "Kibana", linkStyle, linkKibana);
				} else {
					createCell(row, columnCount++, "", style, null);
				}
				if (e.getCard() != null && !e.getCard().isEmpty()) {
					XSSFHyperlink linkJira = helper.createHyperlink(HyperlinkType.URL);
					linkJira.setAddress(abrirLinkJira + e.getCard().trim());
					createCell(row, columnCount++, e.getCard(), linkStyle, linkJira);
				} else {
					createCell(row, columnCount++, e.getCard(), style, null);
				}
				createCell(row, columnCount++, e.getSquad(), style, null);
				createCell(row, columnCount++, e.getStatus(), style, null);
				createCell(row, columnCount++, e.getData_status(), style, null);
				createCell(row, columnCount++, e.getObservacao(), style, null);
				createCell(row, columnCount++, e.getCausa_raiz(), style, null);
				createCell(row, columnCount++, e.getDt_aberta(), style, null);
				createCell(row, columnCount++, e.getDt_vencimento(), style, null);
				createCell(row, columnCount++, e.getDescricao(), descricaoStyle, null);
				createCell(row, columnCount++, e.getNome(), style, null);
				createCell(row, columnCount++, e.getStatus_senha(), style, null);
				createCell(row, columnCount++, e.getEmail(), style, null);
				createCell(row, columnCount++, e.getTelefone(), style, null);
				createCell(row, columnCount++, e.getTelefone_sms(), style, null);
				createCell(row, columnCount++, e.getAtualizacao_cadastral(), style, null);
				createCell(row, columnCount++, e.getEscopo(), style, null);
				createCell(row, columnCount++, e.getConta(), style, null);
				createCell(row, columnCount++, e.getCartoes(), style, null);
				createCell(row, columnCount++, e.getMsg(), style, null);

				log.info("Ocorrencia: {}", e.getOcorrencia());
			}
		}

		log.info("Fim da leitura linha a linha");
	}

	private String validaPrioridadeouVencido(String dt_vencimento, String c_atendimento) throws ParseException {

		if (c_atendimento.equals("CONSUMIDOR.GOV") || c_atendimento.equals("BACEN") || c_atendimento.equals("PROCON")
				|| c_atendimento.equals("PROCON FONE")) {
			return "Prioridade";
		}

		Date dtVencimento = DataUtils.convert(dt_vencimento, DataUtils.formatoData);
		Date dtAtual = new Date();

		if (dtAtual.equals(dtVencimento) || dtAtual.after(dtVencimento)) {
			return "Vencido";
		}

		return "";
	}

	private CellStyle getStyleNormal(XSSFFont font, CellStyle style) {

		font.setFontHeight(11);
		font.setColor(IndexedColors.BLACK.index);
		font.setUnderline(FontUnderline.NONE);

		style.setFont(font);

		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);

		style.setBottomBorderColor(IndexedColors.BLACK.index);
		style.setTopBorderColor(IndexedColors.BLACK.index);
		style.setLeftBorderColor(IndexedColors.BLACK.index);
		style.setRightBorderColor(IndexedColors.BLACK.index);

		style.setAlignment(HorizontalAlignment.CENTER);

		return style;
	}

	private CellStyle getStyleDescricao(XSSFFont font, CellStyle style) {

		font.setFontHeight(11);
		font.setColor(IndexedColors.BLACK.index);
		font.setUnderline(FontUnderline.NONE);

		style.setFont(font);

		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);

		style.setBottomBorderColor(IndexedColors.BLACK.index);
		style.setTopBorderColor(IndexedColors.BLACK.index);
		style.setLeftBorderColor(IndexedColors.BLACK.index);
		style.setRightBorderColor(IndexedColors.BLACK.index);

		style.setAlignment(HorizontalAlignment.LEFT);

		return style;
	}

	private CellStyle getStyleLink(XSSFFont font, CellStyle style) {

		font.setFontHeight(11);
		font.setColor(IndexedColors.BLUE.index);
		font.setUnderline(FontUnderline.SINGLE);

		style.setFont(font);

		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);

		style.setBottomBorderColor(IndexedColors.BLACK.index);
		style.setTopBorderColor(IndexedColors.BLACK.index);
		style.setLeftBorderColor(IndexedColors.BLACK.index);
		style.setRightBorderColor(IndexedColors.BLACK.index);

		style.setAlignment(HorizontalAlignment.CENTER);

		return style;
	}
}