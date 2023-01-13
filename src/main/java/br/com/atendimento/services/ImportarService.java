package br.com.atendimento.services;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import br.com.atendimento.dto.importar.ErrosImportDto;
import br.com.atendimento.dto.importar.ImportarAberturaDto;
import br.com.atendimento.dto.importar.ImportarBacklogDto;
import br.com.atendimento.dto.importar.ResponseImportDto;
import br.com.atendimento.dto.planilhaantiga.PlanilhaAntigaDto;
import br.com.atendimento.entity.Analista;
import br.com.atendimento.entity.Chamado;
import br.com.atendimento.entity.Squad;
import br.com.atendimento.entity.Status;
import br.com.atendimento.excel.PlanilhaAntigaExcelImport;
import br.com.atendimento.util.CnpjUtils;
import br.com.atendimento.util.CpfUtils;
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

	private static final Logger log = LoggerFactory.getLogger(ImportarService.class);

	public ResponseImportDto importarBacklog(List<ImportarBacklogDto> list)
			throws ParseException, KeyManagementException, NoSuchAlgorithmException {

		log.info("Inicio da importação da lista de backlog do integrall");

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

		log.info("Lendo a lista importada.");

		for (ImportarBacklogDto pd : list) {

			log.info("Protocolo lido: {}", pd.getNum_protocolo());

			Chamado chamado = null;

			Optional<Chamado> chamadoExit = chamadoService.findById(Long.parseLong(pd.getNum_protocolo()));
			if (chamadoExit.isPresent()) {
				chamado = chamadoExit.get();
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
			chamado.setSubmotivo(
					subMotivoService.findSubMotivo(pd.getProduto(), pd.getTp_publico(), pd.getVariedade_produto(),
							pd.getMotivo(), pd.getSubmotivo(), pd.getArea_responsavel(), null, pd.getSituacao()));
			chamado.setAnalista(chamado.getSubmotivo().getAnalista());

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

			log.info("Protocolo salvo: {}", pd.getNum_protocolo());
		}

		log.info("Fim da leitura da lista importada.");

		iDto.setErros(listErros);
		iDto.setTotal_importados(total_importado);

		log.info("Fim da importação da lista de backlog do integrall");
		return iDto;
	}

	public ResponseImportDto importarAbertura(List<ImportarAberturaDto> list) throws ParseException {

		log.info("Inicio da importação da lista de aberturas do integrall");

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

		log.info("Lendo a lista importada.");

		for (ImportarAberturaDto pd : list) {

			log.info("Protocolo lido: {}", pd.getNum_protocolo());

			Chamado chamado = null;

			Optional<Chamado> chamadoExit = chamadoService.findById(Long.parseLong(pd.getNum_protocolo()));
			if (chamadoExit.isPresent()) {
				chamado = chamadoExit.get();
			} else {
				chamado = new Chamado();
			}

			chamado.setProtocolo(Long.parseLong(pd.getNum_protocolo()));
			chamado.setStatusintergrall(pd.getSituacao());
			chamado.setOcorrencia(Long.parseLong(pd.getCodigo_correncia()));
			chamado.setCanalatendimento(pd.getCanal());
			chamado.setReabertura(Integer.parseInt(pd.getQuantidade_representante()));
			chamado.setQtdresolucao(Integer.parseInt(pd.getD_resolucao()));
			chamado.setDataabertura(DataUtils.convert(pd.getDt_abertura(), DataUtils.formatoData));
			chamado.setDatavencimento(DataUtils.convert(pd.getDt_previsao(), DataUtils.formatoData));
			chamado.setNome(pd.getNome());
			chamado.setSubmotivo(subMotivoService.findSubMotivo(pd.getProduto(), pd.getTipo_publico(),
					pd.getVariedade_produto(), pd.getMotivo(), pd.getSubmotivo(), pd.getArea_responsavel(),
					pd.getAnalista(), pd.getSituacao()));
			chamado.setAnalista(chamado.getSubmotivo().getAnalista());

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

			chamadoService.save(chamado);

			total_importado++;

			log.info("Protocolo salvo: {}", pd.getNum_protocolo());
		}

		log.info("Fim da leitura da lista importada.");

		iDto.setErros(listErros);
		iDto.setTotal_importados(total_importado);

		log.info("Fim da importação da lista de backlog do integrall");
		return iDto;
	}

	public void importarPlanilhaAntiga(MultipartFile file, String sheet) throws ParseException {
		try {
			List<PlanilhaAntigaDto> list = PlanilhaAntigaExcelImport.excelToChamado(file.getInputStream(), sheet);
			for (PlanilhaAntigaDto a : list) {
				log.info("Protocolo:{}", a.getProtocolo());
				if (a.getProtocolo() != null) {

					Optional<Chamado> chamadoExit = chamadoService.findById(a.getProtocolo());

					if (chamadoExit.isPresent()) {

						Chamado chamado = chamadoExit.get();

						if (a.getStatus() != null) {
							Optional<Status> s = statusService.findByNome(a.getStatus());
							if (s.isPresent()) {
								chamado.setStatus(s.get());
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
						chamado.setObservacao(a.getObservcao());
						chamado.setDatastatus(a.getData_status());
						chamado.setCausaraiz(causaRaizService.findCausaRaiz(a.getCausa_raiz()));

						chamadoService.save(chamado);
					}

				}
			}
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}
}
