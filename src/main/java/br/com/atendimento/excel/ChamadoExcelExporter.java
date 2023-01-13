package br.com.atendimento.excel;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Conta;
import br.com.atendimento.services.ContaService;
import br.com.atendimento.util.DataUtils;

public class ChamadoExcelExporter {

	@Autowired
	private ContaService contaService;

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Chamado> list;

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

		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(11);
		font.setColor(IndexedColors.WHITE.index);
		style.setFont(font);

		int rowNumber = 0;

		for (String name : namesCell) {
			createCell(row, rowNumber, name, style);
			rowNumber++;
		}

		log.info("Fim da montagem do header");
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
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
	}

	private void writeDataLines(String tipo) throws ParseException {
		log.info("Inicio da leitura linha a linha");

		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(11);
		font.setColor(IndexedColors.BLACK.index);
		style.setFont(font);

		for (Chamado chamado : list) {
			if (tipo.equals("Atendimento PF")) {
				Row row = sheet.createRow(rowCount++);
				int columnCount = 0;

				createCell(row, columnCount++, chamado.getAnalista().getNome(), style);
				createCell(row, columnCount++, chamado.getCanalatendimento(), style);
				createCell(row, columnCount++, chamado.getSubmotivo().getNome(), style);
				createCell(row, columnCount++, chamado.getQtdresolucao(), style);
				createCell(row, columnCount++, chamado.getOcorrencia(), style);
				createCell(row, columnCount++, chamado.getProtocolo(), style);
				createCell(row, columnCount++, chamado.getCpf(), style);
				createCell(row, columnCount++, chamado.getCard(), style);
				if (chamado.getSquad() != null) {
					createCell(row, columnCount++, chamado.getSquad().getNome(), style);
				} else {
					createCell(row, columnCount++, "", style);
				}
				if (chamado.getStatus() != null) {
					createCell(row, columnCount++, chamado.getStatus().getNome(), style);
				} else {
					createCell(row, columnCount++, "", style);
				}
				createCell(row, columnCount++, DataUtils.format(chamado.getDatastatus(), DataUtils.formatoData), style);
				createCell(row, columnCount++, chamado.getObservacao(), style);
				if (chamado.getCausaraiz() != null) {
					createCell(row, columnCount++, chamado.getCausaraiz().getNome(), style);
				} else {
					createCell(row, columnCount++, "", style);
				}
				createCell(row, columnCount++, DataUtils.format(chamado.getDataabertura(), DataUtils.formatoData),
						style);
				createCell(row, columnCount++, DataUtils.format(chamado.getDatavencimento(), DataUtils.formatoData),
						style);
				createCell(row, columnCount++, chamado.getDescricao(), style);
				createCell(row, columnCount++, chamado.getNome(), style);
				createCell(row, columnCount++, chamado.getStatussenha(), style);
				createCell(row, columnCount++, chamado.getEmail(), style);
				createCell(row, columnCount++, chamado.getTelefone(), style);
				createCell(row, columnCount++, chamado.getStatussms(), style);
				createCell(row, columnCount++,
						DataUtils.format(chamado.getDataatualizacaocadastral(), DataUtils.formatoData), style);
				createCell(row, columnCount++, chamado.getEscopo(), style);
				createCell(row, columnCount++, "CONTA", style);
				createCell(row, columnCount++, "CARTAO", style);

				log.info("Protocolo: {}", chamado.getProtocolo());

			} else if (tipo.equals("Atendimento PJ")) {
				Row row = sheet.createRow(rowCount++);
				int columnCount = 0;

				createCell(row, columnCount++, chamado.getAnalista().getNome(), style);
				createCell(row, columnCount++, chamado.getCanalatendimento(), style);
				createCell(row, columnCount++, chamado.getSubmotivo().getNome(), style);
				createCell(row, columnCount++, chamado.getQtdresolucao(), style);
				createCell(row, columnCount++, chamado.getOcorrencia(), style);
				createCell(row, columnCount++, chamado.getProtocolo(), style);
				createCell(row, columnCount++, chamado.getCnpj(), style);
				createCell(row, columnCount++, chamado.getCard(), style);
				if (chamado.getSquad() != null) {
					createCell(row, columnCount++, chamado.getSquad().getNome(), style);
				} else {
					createCell(row, columnCount++, "", style);
				}
				if (chamado.getStatus() != null) {
					createCell(row, columnCount++, chamado.getStatus().getNome(), style);
				} else {
					createCell(row, columnCount++, "", style);
				}
				createCell(row, columnCount++, DataUtils.format(chamado.getDatastatus(), DataUtils.formatoData), style);
				createCell(row, columnCount++, chamado.getObservacao(), style);
				if (chamado.getCausaraiz() != null) {
					createCell(row, columnCount++, chamado.getCausaraiz().getNome(), style);
				} else {
					createCell(row, columnCount++, "", style);
				}
				createCell(row, columnCount++, DataUtils.format(chamado.getDataabertura(), DataUtils.formatoData),
						style);
				createCell(row, columnCount++, DataUtils.format(chamado.getDatavencimento(), DataUtils.formatoData),
						style);
				createCell(row, columnCount++, chamado.getDescricao(), style);
				createCell(row, columnCount++, chamado.getNome(), style);
				createCell(row, columnCount++, chamado.getStatussenha(), style);
				createCell(row, columnCount++, chamado.getEmail(), style);
				createCell(row, columnCount++, chamado.getTelefone(), style);
				createCell(row, columnCount++, chamado.getStatussms(), style);
				createCell(row, columnCount++,
						DataUtils.format(chamado.getDataatualizacaocadastral(), DataUtils.formatoData), style);
				createCell(row, columnCount++, chamado.getEscopo(), style);
				createCell(row, columnCount++, "CONTA", style);
				createCell(row, columnCount++, "CARTAO", style);

				log.info("Protocolo: {}", chamado.getProtocolo());

			} else if (tipo.equals("Sincronizar")) {

				List<Conta> contas = contaService.findByChamado_Protocolo(chamado.getProtocolo());
				if (contas.size() > 0) {
					for (Conta conta : contas) {
						Row row = sheet.createRow(rowCount++);
						int columnCount = 0;

						if (chamado.getCpf() != null) {
							createCell(row, columnCount++, chamado.getCpf(), style);
							log.info("CPF: {}", chamado.getCpf());
						} else if (chamado.getCnpj() != null) {
							createCell(row, columnCount++, chamado.getCnpj(), style);
							log.info("CNPJ: {}", chamado.getCnpj());
						}

						createCell(row, columnCount++, conta.getAgencia(), style);
						createCell(row, columnCount++, conta.getNumeroconta(), style);
					}
				}
			}
		}

		log.info("Fim da leitura linha a linha");
	}
}