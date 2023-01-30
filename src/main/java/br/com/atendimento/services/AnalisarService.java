package br.com.atendimento.services;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.atendimento.dto.analisar.ResponseDevolverDto;
import br.com.atendimento.entity.Chamado;

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
	
	private static final Logger log = LoggerFactory.getLogger(AnalisarService.class);


	public ResponseDevolverDto devolver() {
		ResponseDevolverDto resp = new ResponseDevolverDto();
		
		log.info("Buscando lista de chamados para devolver para fila BMG Empresas");
		
		List<Chamado> devolverBmgEmpresa = chamadoService.findByStatusintergrallAndSubmotivo_EquipeAndCnpjIsNotNull("Pendente", "BACKOFFICE DÍGITAL");
		
		resp.setTotal_devolver_bmg_empresa(devolverBmgEmpresa.size());
		
		if(devolverBmgEmpresa.size() > 0) {
			for(Chamado c : devolverBmgEmpresa) {
				c.setStatus(statusService.findByNome("DEVOLVER").get());
				c.setAnalista(analistaService.findById(14L).get());
				c.setCausaraiz(causaRaizService.findById(1L).get());
				c.setDatastatus(new Date());
				chamadoService.save(c);
			}
		}		
		
		return resp;
	}
}
