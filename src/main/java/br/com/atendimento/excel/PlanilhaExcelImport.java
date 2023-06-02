package br.com.atendimento.excel;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import br.com.atendimento.dto.planilhaantiga.PlanilhaDto;

public class PlanilhaExcelImport {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<PlanilhaDto> excelToChamadoPf(InputStream is, String sheetName) throws ParseException {
		try {
			ZipSecureFile.setMinInflateRatio(0);
			Workbook workbook = new XSSFWorkbook(is);

			Sheet sheet = workbook.getSheet(sheetName);
			Iterator<Row> rows = sheet.iterator();

			List<PlanilhaDto> list = new ArrayList<PlanilhaDto>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				PlanilhaDto atendimento = new PlanilhaDto();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						atendimento.setAnalista(returnCellToString(currentCell));
						break;
						
					case 4:
						atendimento.setOcorrencia(returnCellToLong(currentCell));
						break;
					
					case 11:
						atendimento.setCard(returnCellToString(currentCell).replace(" ", ""));
						break;

					case 12:
						atendimento.setSquad(returnCellToString(currentCell));
						break;

					case 13:
						atendimento.setStatus(returnCellToString(currentCell));
						break;

					case 14:
						atendimento.setData_status(returnCellToDate(currentCell));
						break;

					case 15:
						atendimento.setObservacao(returnCellToString(currentCell));
						break;

					case 16:
						atendimento.setCausa_raiz(returnCellToString(currentCell));
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
	
	public static List<PlanilhaDto> excelToChamadoPj(InputStream is, String sheetName) throws ParseException {
		try {
			ZipSecureFile.setMinInflateRatio(0);
			Workbook workbook = new XSSFWorkbook(is);

			Sheet sheet = workbook.getSheet(sheetName);
			Iterator<Row> rows = sheet.iterator();

			List<PlanilhaDto> list = new ArrayList<PlanilhaDto>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				PlanilhaDto atendimento = new PlanilhaDto();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						atendimento.setAnalista(returnCellToString(currentCell));
						break;
						
					case 4:
						atendimento.setOcorrencia(returnCellToLong(currentCell));
						break;
					
					case 12:
						atendimento.setCard(returnCellToString(currentCell).replace(" ", ""));
						break;

					case 13:
						atendimento.setSquad(returnCellToString(currentCell));
						break;

					case 14:
						atendimento.setStatus(returnCellToString(currentCell));
						break;

					case 15:
						atendimento.setData_status(returnCellToDate(currentCell));
						break;

					case 16:
						atendimento.setObservacao(returnCellToString(currentCell));
						break;

					case 17:
						atendimento.setCausa_raiz(returnCellToString(currentCell));
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
		if (currentCell.getCellType().equals(CellType.STRING)) {
			return currentCell.getStringCellValue().trim();
		} else if (currentCell.getCellType().equals(CellType.NUMERIC)) {
			return String.valueOf((long) currentCell.getNumericCellValue());
		}
		return "";
	}
	
	private static Long returnCellToLong(Cell currentCell) {
		if(currentCell.getCellType().equals(CellType.STRING)) {
			return Long.parseLong(currentCell.getStringCellValue());
		} else if(currentCell.getCellType().equals(CellType.NUMERIC)) {
			return (long) currentCell.getNumericCellValue();
		} 
		return null;
	}
	
	private static Date returnCellToDate(Cell currentCell) throws ParseException {
		if(currentCell.getCellType().equals(CellType.STRING)) {
			return formato.parse(currentCell.getStringCellValue());
		} else if(currentCell.getCellType().equals(CellType.NUMERIC)) {
			return currentCell.getDateCellValue();
		} 
		return null;
	}
}