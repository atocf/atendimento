package br.com.atendimento.excel;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import br.com.atendimento.dto.planilhaantiga.PlanilhaAntigaDto;

public class PlanilhaAntigaExcelImport {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "ANALISTA", "PLANILHA SERVIO", "CARD", "SQUAD", "OBSERVAÇÃO", "CAUSA RAIZ", "STATUS",
			"DATA-STATUS", "OCORRENCIA", "DESCRIÇÃO", "SUBMOTIVO", "CPF", "CANAL DE ATENDIMENTO", "DATA ABERTURA",
			"PROTOCOLO", "DATA VENCIMENTO", "NOME", "STATUS SENHA", "EMAIL", "TELEFONE", "TELEFONE_SMS",
			"ULTIMAL_ATUALIZACAO_CADASTRAL", "ESCOPO", "CONTA", "CARTOES", "REABERTURA" };
	static String SHEET = "16-09-2022";
	
	static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<PlanilhaAntigaDto> excelToChamado(InputStream is, String sheetName) throws ParseException {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			Sheet sheet = workbook.getSheet(sheetName);
			Iterator<Row> rows = sheet.iterator();

			List<PlanilhaAntigaDto> list = new ArrayList<PlanilhaAntigaDto>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				PlanilhaAntigaDto atendimento = new PlanilhaAntigaDto();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					
					switch (cellIdx) {
					case 0:
						atendimento.setAnalista(returnCellToString(currentCell));
						break;

					case 2:
						atendimento.setCard(returnCellToString(currentCell));
						break;

					case 3:
						atendimento.setSquad(returnCellToString(currentCell));
						break;

					case 4:
						atendimento.setObservcao(returnCellToString(currentCell));
						break;
						
					case 5:
						atendimento.setCausa_raiz(returnCellToString(currentCell));
						break;
						
					case 6:
						atendimento.setStatus(returnCellToString(currentCell));
						break;
						
					case 7:
						atendimento.setData_status(returnCellToDate(currentCell));
						break;
						
					case 8:
						atendimento.setOcorrencia(returnCellToLong(currentCell));
						break;

					default:
						break;
					}

					cellIdx++;
				}

				list.add(atendimento);
			}

			workbook.close();

			return list;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
	
	private static String returnCellToString(Cell currentCell) {
		if(currentCell.getCellType().equals(CellType.STRING)) {
			return currentCell.getStringCellValue();
		} else if(currentCell.getCellType().equals(CellType.NUMERIC)) {
			return String.valueOf((long) currentCell.getNumericCellValue());
		} 
		return "";
	}
	
	private static Date returnCellToDate(Cell currentCell) throws ParseException {
		if(currentCell.getCellType().equals(CellType.STRING)) {
			return formato.parse(currentCell.getStringCellValue());
		} else if(currentCell.getCellType().equals(CellType.NUMERIC)) {
			return currentCell.getDateCellValue();
		} 
		return null;
	}
	
	private static Long returnCellToLong(Cell currentCell) {
		if(currentCell.getCellType().equals(CellType.STRING)) {
			return Long.parseLong(currentCell.getStringCellValue());
		} else if(currentCell.getCellType().equals(CellType.NUMERIC)) {
			return (long) currentCell.getNumericCellValue();
		} 
		return null;
	}
}