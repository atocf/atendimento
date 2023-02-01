package br.com.atendimento.services;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.atendimento.dto.analisar.ResponseAnalisarDto;
import br.com.atendimento.dto.analisar.ResponseDevolverDto;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Conta;

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

	@Value("${chromedriver.path}")
	private String chromedriverPath;

	private static final Logger log = LoggerFactory.getLogger(AnalisarService.class);

	

	public ResponseAnalisarDto analisar() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();
		
		ResponseAnalisarDto respAberturaConta = validaAberturaConta();
		
		resp.setTotal_devolver_fila_errada(resp.getTotal_devolver_fila_errada() + respAberturaConta.getTotal_devolver_fila_errada());
		resp.setTotal_devolver_fila_errada_bmg_empresa(resp.getTotal_devolver_fila_errada_bmg_empresa() + respAberturaConta.getTotal_devolver_fila_errada_bmg_empresa());
		resp.setTotal_devolver_fila_errada_conta_corrente(resp.getTotal_devolver_fila_errada_conta_corrente() + respAberturaConta.getTotal_devolver_fila_errada_conta_corrente());
		resp.setTotal_devolver_fila_errada_fraude(resp.getTotal_devolver_fila_errada_fraude() + respAberturaConta.getTotal_devolver_fila_errada_fraude());
		resp.setTotal_devolver_fila_errada_geare(resp.getTotal_devolver_fila_errada_geare() + respAberturaConta.getTotal_devolver_fila_errada_geare());
		resp.setTotal_devolver_fila_errada_geback(resp.getTotal_devolver_fila_errada_geback() + respAberturaConta.getTotal_devolver_fila_errada_geback());
		
		return resp;
	}
	
	
	private ResponseAnalisarDto validaAberturaConta() {
		ResponseAnalisarDto resp = new ResponseAnalisarDto();
		
		List<Chamado> list = chamadoService
				.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Nome("Pendente", "BACKOFFICE DÍGITAL", "CLIENTE NÃO CONSEGUE FINALIZAR ABERTURA DE CONTA");
		if(list.size() > 0) {
			for(Chamado chamado : list) {
				if(chamado.getConta().size() > 0) {
					for(Conta conta : chamado.getConta()) {
						if(!(conta.getDescricaosituacao().equals("DESATIVADA"))) {
							chamado.setStatus(statusService.findByNome("DEVOLVER").get());
							chamado.setCausaraiz(causaRaizService.findById(1L).get()); //definir causa raiz preciso do texto para isso 
							chamado.setDatastatus(new Date());
							chamadoService.save(chamado);
						}
					}
				}
				if(chamado.getStatus() == null) {
					
				}
			}
		}
		
		
		
		return resp;
	}











	public ResponseDevolverDto devolver() {
		ResponseDevolverDto resp = new ResponseDevolverDto();

		log.info("Buscando lista de chamados para devolver para fila BMG Empresas");

		List<Chamado> devolverBmgEmpresa = chamadoService
				.findByStatusintergrallAndSubmotivo_EquipeAndCnpjIsNotNull("Pendente", "BACKOFFICE DÍGITAL");

		resp.setTotal_devolver_bmg_empresa(devolverBmgEmpresa.size());

		if (devolverBmgEmpresa.size() > 0) {
			for (Chamado c : devolverBmgEmpresa) {
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
