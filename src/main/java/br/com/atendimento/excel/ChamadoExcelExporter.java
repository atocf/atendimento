package br.com.atendimento.excel;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.common.usermodel.HyperlinkType;
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

import br.com.atendimento.entity.Cartao;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Conta;
import br.com.atendimento.util.DataUtils;

public class ChamadoExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Chamado> list;

	private String abrirLinkInicio = "https://wwws.intergrall.com.br/callcenter/popup.php?programa=flw_pendencias_2.php%3Facao%3DRP%26mk_flag%3DMD%26mk_numero%3DYB%26grupo_acesso%3D1%26ativ_num%3D";
	private String abrirLinkFim = "%26flw_tema%3D004%26flw_tema_pai%3DFYB83%26flw_anexo_arquivo%3DN%26even_num%3D1%26contrato%3D%26ilha%3D%26deonde_prog%3Dfollow%26repre%3DBMG-OPER%26combo_area%3D226%26nivel%3DPNQ%26devolve_nivel_acesso%3D%26flag_altera_dados%3DN%26pend_nivel_acesso_hora_reserva%3D226%26tipo_popup%3DAJ2%26titulo%3DTarefa%20-%20Redirecionar%20Ocorr%EAncia&titulo=Tarefa%20-%20Redirecionar%20Ocorr%EAncia";

	private String fecharLinkInicio = "https://wwws.intergrall.com.br/callcenter/popup.php?programa=flw_pendencias_2_evento.php%3Facao%3DRP%26mk_flag%3DMD%26mk_numero%3DYB%26grupo_acesso%3D1%26ativ_num%3D";
	private String fecharLinkFim = "%26flw_tema%3D038%26flw_tema_pai%3DFYB40%26flw_anexo_arquivo%3DN%26even_num%3D1%26contrato%3D%26ilha%3D%26deonde_prog%3Dfollow%26repre%3DBMG-OPER%26combo_area%3D226%26nivel%3D%26devolve_nivel_acesso%3D%26deonde_baixa_atd%3DPENDENCIA%26func_atualiza_hist_mk%3DatualizaFrameHistoricoMK%28%29%26deonde_pgm%3DPROMOTORA%26flag_altera_dados%3DN%26pend_nivel_acesso_hora_reserva%3D226%26data_cri%3D%26bmg_fcr%3DN%26tipo_popup%3DAJ2%26titulo%3DTarefa%20-%20Redirecionar%20Ocorr%EAncia&titulo=Tarefa%20-%20Redirecionar%20Ocorr%EAncia";

	private static final Logger log = LoggerFactory.getLogger(ChamadoExcelExporter.class);

	public ChamadoExcelExporter(List<Chamado> list) {
		this.list = list;
		workbook = new XSSFWorkbook();
	}

	public void export(HttpServletResponse response, String nameSheet, String[] namesCell)
			throws IOException, ParseException {
		writeHeaderLine(nameSheet, namesCell);
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

		XSSFFont font = workbook.createFont();
		font.setFontHeight(11);
		font.setColor(IndexedColors.BLACK.index);

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);

		CellStyle styleDescricao = workbook.createCellStyle();
		styleDescricao.setFont(font);
		styleDescricao.setAlignment(HorizontalAlignment.LEFT);

		XSSFFont fontLink = workbook.createFont();
		fontLink.setFontHeight(11);
		fontLink.setColor(IndexedColors.BLUE.index);
		fontLink.setUnderline(FontUnderline.SINGLE);

		CellStyle styleLink = workbook.createCellStyle();
		styleLink.setFont(fontLink);
		styleLink.setAlignment(HorizontalAlignment.CENTER);

		XSSFCreationHelper helper = workbook.getCreationHelper();

		for (Chamado chamado : list) {
			if (tipo.equals("Atendimento")) {
				Row row = sheet.createRow(rowCount++);
				int columnCount = 0;

				createCell(row, columnCount++, chamado.getAnalista().getNome(), style, null);
				createCell(row, columnCount++, chamado.getCanalatendimento(), style, null);
				createCell(row, columnCount++, chamado.getSubmotivo().getNome(), style, null);
				createCell(row, columnCount++, chamado.getQtdresolucao(), style, null);
				createCell(row, columnCount++, chamado.getOcorrencia(), style, null);
				createCell(row, columnCount++, chamado.getProtocolo(), style, null);
				if (chamado.getCpf() != null) {
					createCell(row, columnCount++, chamado.getCpf(), style, null);
				} else if (chamado.getCnpj() != null) {
					createCell(row, columnCount++, chamado.getCnpj(), style, null);
				}
				createCell(row, columnCount++, chamado.getCard(), style, null);
				if (chamado.getSquad() != null) {
					createCell(row, columnCount++, chamado.getSquad().getNome(), style, null);
				} else {
					createCell(row, columnCount++, "", style, null);
				}
				if (chamado.getStatus() != null) {
					createCell(row, columnCount++, chamado.getStatus().getNome(), style, null);
				} else {
					createCell(row, columnCount++, "", style, null);
				}
				createCell(row, columnCount++, DataUtils.format(chamado.getDatastatus(), DataUtils.formatoData), style,
						null);
				createCell(row, columnCount++, chamado.getObservacao(), style, null);
				if (chamado.getCausaraiz() != null) {
					createCell(row, columnCount++, chamado.getCausaraiz().getNome(), style, null);
				} else {
					createCell(row, columnCount++, "", style, null);
				}
				createCell(row, columnCount++, DataUtils.format(chamado.getDataabertura(), DataUtils.formatoData),
						style, null);
				createCell(row, columnCount++, DataUtils.format(chamado.getDatavencimento(), DataUtils.formatoData),
						style, null);
				createCell(row, columnCount++, chamado.getDescricao(), styleDescricao, null);
				createCell(row, columnCount++, chamado.getNome(), style, null);
				createCell(row, columnCount++, chamado.getStatussenha(), style, null);
				createCell(row, columnCount++, chamado.getEmail(), style, null);
				createCell(row, columnCount++, chamado.getTelefone(), style, null);
				createCell(row, columnCount++, chamado.getStatussms(), style, null);
				createCell(row, columnCount++,
						DataUtils.format(chamado.getDataatualizacaocadastral(), DataUtils.formatoData), style, null);
				createCell(row, columnCount++, chamado.getEscopo(), style, null);

				if (chamado.getConta() != null && chamado.getConta().size() > 0) {
					String strConta = "";
					for (Conta conta : chamado.getConta()) {
						strConta = strConta + "Tipo:" + conta.getTipoconta() + "-" + conta.getDescricaosituacao() + "-"
								+ conta.getAgencia() + "/" + conta.getNumeroconta() + " | ";
					}
					createCell(row, columnCount++, strConta, style, null);
				} else {
					createCell(row, columnCount++, "Não encontrada", style, null);
				}

				if (chamado.getCartao() != null && chamado.getCartao().size() > 0) {
					String strCartao = "";
					for (Cartao cartao : chamado.getCartao()) {
						strCartao = strCartao + "(" + cartao.getDescricaostatuscartao() + ") "
								+ cartao.getNumerocartao().substring(cartao.getNumerocartao().length() - 4) + " "
								+ cartao.getDescricaotipocartao() + " | ";
					}
					createCell(row, columnCount++, strCartao, style, null);
				} else {
					createCell(row, columnCount++, "Não encontrada", style, null);
				}

				XSSFHyperlink linkAbrir = helper.createHyperlink(HyperlinkType.URL);
				linkAbrir.setAddress(abrirLinkInicio + chamado.getOcorrencia() + abrirLinkFim);
				createCell(row, columnCount++, "Abrir", styleLink, linkAbrir);

				if (chamado.getSubmotivo().getEquipe().equals("BACKOFFICE DÍGITAL")) {

					XSSFHyperlink linkFechar = helper.createHyperlink(HyperlinkType.URL);
					linkFechar.setAddress(fecharLinkInicio + chamado.getOcorrencia() + fecharLinkFim);
					createCell(row, columnCount++, "Fechar", styleLink, linkFechar);
				}

				log.info("Protocolo: {}", chamado.getProtocolo());

			} else if (tipo.equals("Sincronizar")) {
				if (chamado.getConta() != null && chamado.getConta().size() > 0) {
					for (Conta conta : chamado.getConta()) {
						Row row = sheet.createRow(rowCount++);
						int columnCount = 0;

						if (chamado.getCpf() != null) {
							createCell(row, columnCount++, chamado.getCpf(), style, null);
							log.info("CPF: {}", chamado.getCpf());
						} else if (chamado.getCnpj() != null) {
							createCell(row, columnCount++, chamado.getCnpj(), style, null);
							log.info("CNPJ: {}", chamado.getCnpj());
						}

						createCell(row, columnCount++, conta.getAgencia(), style, null);
						createCell(row, columnCount++, conta.getNumeroconta(), style, null);
					}
				}
			}
		}

		log.info("Fim da leitura linha a linha");
	}
}