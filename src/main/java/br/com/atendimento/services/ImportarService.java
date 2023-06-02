package br.com.atendimento.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.atendimento.dto.importar.ErrosImportDto;
import br.com.atendimento.dto.importar.ImportarBacklogDto;
import br.com.atendimento.dto.importar.ResponseImportDto;
import br.com.atendimento.dto.planilhaantiga.PlanilhaDto;
import br.com.atendimento.entity.Analista;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Squad;
import br.com.atendimento.entity.Status;
import br.com.atendimento.excel.PlanilhaExcelImport;
import br.com.atendimento.util.CnpjUtils;
import br.com.atendimento.util.CpfUtils;
import br.com.atendimento.util.CsvUtils;
import br.com.atendimento.util.DataUtils;
import io.micrometer.core.instrument.util.StringUtils;

@Service
public class ImportarService {

	@Autowired
	private AnalistaService analistaService;

	@Autowired
	private ChamadoService chamadoService;

	@Autowired
	private CausaRaizService causaRaizService;

	@Autowired
	private IntegracaoService integracaoService;

	@Autowired
	private SubMotivoService subMotivoService;

	@Autowired
	private StatusService statusService;

	@Autowired
	private SquadService squadService;

	private static final Logger logger = LoggerFactory.getLogger(ImportarService.class);
	private static final String STR_PLANILHA_PJ = "PJ - Intergrall.xlsx";
	private static final String STR_PLANILHA_PF = "PF - Intergrall.xlsx";
	private static final String STR_PLANILHA_PF_PIX = "PIX- Intergrall.xlsx";
	private static final String STR_PLANILHA_PF_PREFERENCIAL = "Priorizados PF - Intergrall.xlsx";
	private static final String STR_BACKOFFICE_DIGITAL = "BACKOFFICE DÍGITAL";
	private static final String STR_BMG_EMPRESA = "BMG EMPRESAS"; 

	public ResponseImportDto importarBacklog(List<ImportarBacklogDto> list)
			throws ParseException, KeyManagementException, NoSuchAlgorithmException {

		logger.info("Inicio da importação da lista de backlog do integrall");

		List<ErrosImportDto> listErros = new ArrayList<ErrosImportDto>();
		int total_importado = 0;

		ResponseImportDto iDto = new ResponseImportDto();
		iDto.setStatus(true);
		iDto.setTotal_enviados(list.size());

		if (!(list.size() > 0)) {
			iDto.setStatus(false);
			iDto.setMsg("Nenhum item encontrado para importação");
			return iDto;
		}

		chamadoService.updateAllFinalizado(list.get(0).getArea_responsavel());
		iDto.setMsg(list.get(0).getArea_responsavel());

		for (ImportarBacklogDto pd : list) {

			logger.info("Ocorrência lida: {}", pd.getOcorrencia());

			Chamado chamado = null;

			Optional<Chamado> chamadoExit = chamadoService.findById(Long.parseLong(pd.getOcorrencia()));
			if (chamadoExit.isPresent()) {
				chamado = chamadoExit.get();
				if (chamado.getStatus() != null && !(chamado.getStatus().getNome().equals("ENCAMINHADO"))) {
					chamado.setStatus(null);
				}
			} else {
				chamado = new Chamado();
			}

			chamado.setProtocolo(Long.parseLong(pd.getNum_protocolo()));
			chamado.setStatusintergrall(pd.getSituacao());
			chamado.setOcorrencia(Long.parseLong(pd.getOcorrencia()));
			chamado.setCanalatendimento(pd.getCanal());
			chamado.setReabertura(Integer.parseInt(pd.getQtd_reapresentaçoes()));
			chamado.setDataabertura(DataUtils.convert(pd.getDt_abertura(), DataUtils.formatoData));
			chamado.setDatavencimento(DataUtils.convert(pd.getDt_previsao(), DataUtils.formatoData));
			chamado.setDescricao(StringUtils.truncate(pd.getDescricao(), 10000));
			chamado.setNome(pd.getNome());
			chamado.setSubmotivo(subMotivoService.findSubMotivo(pd.getProduto(), pd.getTp_publico(),
					pd.getVariedade_produto(), pd.getMotivo(), pd.getSubmotivo(), pd.getArea_responsavel(), null,
					pd.getSituacao(), pd.getCanal()));

			if (chamado.getAnalista() == null) {
				chamado.setAnalista(chamado.getSubmotivo().getAnalista());
			}

			if (!pd.getDt_conclusao().equals("Não informado")) {
				chamado.setDataconclusao(DataUtils.convert(pd.getDt_conclusao(), DataUtils.formatoData));
			}

			String cpf_ou_cnpj = CpfUtils.valid(pd.getCpf());
			if (cpf_ou_cnpj != null) {
				chamado.setCpf(cpf_ou_cnpj);
			} else {
				cpf_ou_cnpj = CnpjUtils.valid(pd.getCpf());
				if (cpf_ou_cnpj != null) {
					chamado.setCnpj(cpf_ou_cnpj);
				}
			}

			integracaoService.buscarDados(chamadoService.save(chamado));

			total_importado++;

			logger.info("Ocorrência salvo: {}", pd.getOcorrencia());
		}

		iDto.setErros(listErros);
		iDto.setTotal_importados(total_importado);

		logger.info("Fim da importação da lista de backlog do integrall");
		return iDto;
	}

	public int importarPlanilhaPf(InputStream file, String sheet) throws ParseException {
		return atualizaChamados(PlanilhaExcelImport.excelToChamadoPf(file, sheet));
	}
	
	public int importarPlanilhaPj(InputStream inputStream, String sheet) throws ParseException, IOException {
		return atualizaChamados(PlanilhaExcelImport.excelToChamadoPj(inputStream, sheet));
	}

	private int atualizaChamados(List<PlanilhaDto> list) {

		if (list.size() > 0) {
			for (PlanilhaDto a : list) {
				logger.info("Ocorrência:{}", a.getOcorrencia());
				if (a.getOcorrencia() != null) {

					Optional<Chamado> chamadoExit = chamadoService.findById(a.getOcorrencia());

					if (chamadoExit.isPresent()) {

						Chamado chamado = chamadoExit.get();

						if (a.getStatus() != null) {
							Optional<Status> s = statusService.findByNome(a.getStatus());
							if (s.isPresent()) {
								chamado.setStatus(s.get());
								if (!(s.get().getNome().equals("ENCAMINHADO"))) {
									chamado.setStatusintergrall("Finalizado");
								}
							}
						}

						Optional<Analista> analista = analistaService.findByNome(a.getAnalista());
						if (analista.isPresent()) {
							chamado.setAnalista(analista.get());
						}

						if (a.getSquad() != null) {
							Optional<Squad> s = squadService.findByNome(a.getSquad());
							if (s.isPresent()) {
								chamado.setSquad(s.get());
							}
						}
						chamado.setCard(a.getCard());
						chamado.setObservacao(StringUtils.truncate(a.getObservacao(), 5000));
						chamado.setDatastatus(a.getData_status());
						chamado.setCausaraiz(causaRaizService.findCausaRaiz(a.getCausa_raiz()));

						chamadoService.save(chamado);
					}
				}
			}
		}
		
		return list.size();
	}

	public ResponseImportDto importPlanilhas(String sheet) throws ParseException, FileNotFoundException, IOException, KeyManagementException, NoSuchAlgorithmException {
		ResponseImportDto responseImportDto = new ResponseImportDto();
		
		File fPlanilhas = new File(getClass().getResource("/import/planilhas").getPath());
		File[] aPLanilhas = fPlanilhas.listFiles(); 
		
		for(File p : aPLanilhas) {
			
			if(STR_PLANILHA_PJ.equals(p.getName())) {
				logger.info("Iniciar Importação:{}", p.getName());
				int t = importarPlanilhaPj(new FileInputStream(p), sheet);
				responseImportDto.setTotal_Pj(t);				
			} else { 
				logger.info("Iniciar Importação:{}", p.getName());
				int t = importarPlanilhaPf(new FileInputStream(p), sheet);
				if(STR_PLANILHA_PF.equals(p.getName())) {
					responseImportDto.setTotal_Pf(t);
				} else if(STR_PLANILHA_PF_PIX.equals(p.getName())) {
					responseImportDto.setTotal_Pf_Pix(t);
				} else if(STR_PLANILHA_PF_PREFERENCIAL.equals(p.getName())) {
					responseImportDto.setTotal_Pf_Preferencial(t);
				}
			} 
			p.delete();
			
			logger.info("Fim Importação");
			logger.info("--------------");
			logger.info("--------------");
			logger.info("--------------");
		}
		
		File fIntergrall = new File(getClass().getResource("/import/intergrall").getPath());
		File[] aIntergrall = fIntergrall.listFiles(); 
		
		for(File p : aIntergrall) {
			logger.info("Iniciar Importação:{}", p.getName());
			ResponseImportDto rp = importarBacklog(CsvUtils.read(ImportarBacklogDto.class, new FileInputStream(p)));
			if(STR_BACKOFFICE_DIGITAL.equals(rp.getMsg())) {
				responseImportDto.setTotal_BackOffice_Digital(rp.getTotal_importados());
			} else if(STR_BMG_EMPRESA.equals(rp.getMsg())) {
				responseImportDto.setTotal_BMG_Empresas(rp.getTotal_importados());
			} 
			p.delete();
			logger.info("Fim Importação");
			logger.info("--------------");
			logger.info("--------------");
			logger.info("--------------");
		}

		return responseImportDto;
	}
}
