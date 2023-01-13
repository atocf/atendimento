package br.com.atendimento.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.atendimento.entity.Cartao;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.excel.ChamadoExcelExporter;

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
	    
	    if(list.size() > 0) {
	    	ChamadoExcelExporter excelExporter = new ChamadoExcelExporter(list);
	    	String[] namesCell = new String[] {"CPF ou CNPJ", "Agência", "Conta"};	    	
			excelExporter.export(response, "Sincronizar", namesCell, null);
	    }
	}
	
	public void exportarAtendimentoPfGeral(HttpServletResponse response) throws IOException, ParseException {
		log.info("Inicio do processo de gerar excel dos chamados para atendimento PF");
		
		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Pix("Pendente", "BACKOFFICE DÍGITAL", false);
	    
		if(list.size() > 0) {
	    	ChamadoExcelExporter excelExporter = new ChamadoExcelExporter(list);
	    	String[] namesCell = new String[] {"ANALISTA", "CANAL DE ATENDIMENTO", "SUBMOTIVO", "REABERTURA", "OCORRENCIA", "PROTOCOLO", "CPF", "CARD", "SQUAD", "STATUS", "DATA-STATUS", "OBSERVAÇÃO", "CAUSA RAIZ", "DATA ABERTURA", "DATA VENCIMENTO", "DESCRIÇÃO", "NOME", "STATUS SENHA", "EMAIL", "TELEFONE", "TELEFONE_SMS", "ULTIMAL_ATUALIZACAO_CADASTRAL", "ESCOPO", "CONTA", "CARTOES", "ABRIR", "FECHAR"};	    	
			
	    	List<Cartao> cartoes = cartaoService.findByChamado_Statusintergrall("Pendente");
	    	
	    	excelExporter.export(response, "Atendimento", namesCell, cartoes);
	    }
	}
	
	public void exportarAtendimentoPfPix(HttpServletResponse response) throws IOException, ParseException {
		log.info("Inicio do processo de gerar excel dos chamados para atendimento PF");
		
		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Pix("Pendente", "BACKOFFICE DÍGITAL", true);
	    
	    if(list.size() > 0) {
	    	ChamadoExcelExporter excelExporter = new ChamadoExcelExporter(list);
	    	
	    	String[] namesCell = new String[] {"ANALISTA", "CANAL DE ATENDIMENTO", "SUBMOTIVO", "REABERTURA", "OCORRENCIA", "PROTOCOLO", "CPF", "CARD", "SQUAD", "STATUS", "DATA-STATUS", "OBSERVAÇÃO", "CAUSA RAIZ", "DATA ABERTURA", "DATA VENCIMENTO", "DESCRIÇÃO", "NOME", "STATUS SENHA", "EMAIL", "TELEFONE", "TELEFONE_SMS", "ULTIMAL_ATUALIZACAO_CADASTRAL", "ESCOPO", "CONTA", "CARTOES", "ABRIR", "FECHAR"};	    	
			excelExporter.export(response, "Atendimento", namesCell, null);
	    }
	}
	
	public void exportarAtendimentoPj(HttpServletResponse response) throws IOException, ParseException {
		log.info("Inicio do processo de gerar excel dos chamados para atendimento PJ");
		
		List<Chamado> list = chamadoService.findByStatusintergrallAndSubmotivo_Equipe("Pendente", "BMG EMPRESAS");
	    
	    if(list.size() > 0) {
	    	ChamadoExcelExporter excelExporter = new ChamadoExcelExporter(list);
	    	String[] namesCell = new String[] {"ANALISTA", "CANAL DE ATENDIMENTO", "SUBMOTIVO", "REABERTURA", "OCORRENCIA", "PROTOCOLO", "CNPJ", "CARD", "SQUAD", "STATUS", "DATA-STATUS", "OBSERVAÇÃO", "CAUSA RAIZ", "DATA ABERTURA", "DATA VENCIMENTO", "DESCRIÇÃO", "NOME", "STATUS SENHA", "EMAIL", "TELEFONE", "TELEFONE_SMS", "ULTIMAL_ATUALIZACAO_CADASTRAL", "ESCOPO", "CONTA", "CARTOES", "ABRIR"};	    	
			
	    	List<Cartao> cartoes = cartaoService.findByChamado_Statusintergrall("Pendente");
	    	
	    	excelExporter.export(response, "Atendimento", namesCell, cartoes);
	    }
	}
}
