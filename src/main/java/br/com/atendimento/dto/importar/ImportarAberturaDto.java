package br.com.atendimento.dto.importar;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportarAberturaDto {
	
	@JsonProperty("Abertura Ocorrência")
	private String abertura_ocorrencia;		
	
	@JsonProperty("Agência")
	private String agencia;	
	
	@JsonProperty("Alto Atrito")
	private String altoatrito;
	
	@JsonProperty("Analista")
	private String analista;
	
	@JsonProperty("Área Responsável")
	private String area_responsavel;
	
	@JsonProperty("Banco")
	private String banco;	
	
	@JsonProperty("Banco Central")
	private String banco_central;
	
	@JsonProperty("Bancos")
	private String bancos;
	
	@JsonProperty("Callback Obrigatório (Sim / Não)")
	private String callback;
	
	@JsonProperty("Canal de Atendimento")
	private String canal;
	
	@JsonProperty("Classificação")
	private String classificacao ;
	
	@JsonProperty("Classificação da Reclamação")
	private String classificacao_reclamacao;

	@JsonProperty("Classificação do Canal")
	private String classificacao_canal;
			
	@JsonProperty("Código da Ocorrência")
	private String codigo_correncia;
			
	@JsonProperty("Código do Expediente")
	private String codigo_expediente;
			
	@JsonProperty("Conta")
	private String conta;
			
	@JsonProperty("CPF")
	private String cpf;
			
	@JsonProperty("Data Abertura Ocorrência")
	private String dt_abertura;
			
	@JsonProperty("Data Conclusão da Ocorrência")
	private String dt_conclusao;
			
	@JsonProperty("Data Fatal Vigente")
	private String dt_fatal;
			
	@JsonProperty("Data Previsão Final da Ocorrência")
	private String dt_previsao;
			
	@JsonProperty("Data Reabertura Ocorrência")
	private String dt_reabertura;
			
	@JsonProperty("Data Recebimento")
	private String d_recebimento;
			
	@JsonProperty("Dias Para Resolução")
	private String d_resolucao;
			
	@JsonProperty("DV")
	private String dv;
			
	@JsonProperty("FCR (Sim / Não)")
	private String fcr;
			
	@JsonProperty("Forma de Contato")
	private String forma_contato;
			
	@JsonProperty("Forma de Envio do Documento")
	private String forma_envio;
			
	@JsonProperty("Forma de Manifestação")
	private String forma_manifestacao;
			
	@JsonProperty("Forma de Retorno")
	private String forma_retorno;
			
	@JsonProperty("Funcionário")
	private String funcionario;
			
	@JsonProperty("Grupo")
	private String grupo;
			
	@JsonProperty("Hora Abertura Ocorrência")
	private String h_abertura;
			
	@JsonProperty("Hora Conclusão da Ocorrência")
	private String h_conculsao;
			
	@JsonProperty("Hora Previsão Final da Ocorrência")
	private String h_previsao;
			
	@JsonProperty("Hora Recebimento")
	private String h_recebimento;
			
	@JsonProperty("Instância do Canal (Normal / 2a)")
	private String instancia_canal;
			
	@JsonProperty("Motivo")
	private String motivo;
			
	@JsonProperty("Nome")
	private String nome;
			
	@JsonProperty("Número do Protocolo")
	private String num_protocolo;
			
	@JsonProperty("Opção do Processo")
	private String opcao_processo;
			
	@JsonProperty("Procedente / Improcedente")
	private String procedente_improcedente;
			
	@JsonProperty("Produto")
	private String produto;
			
	@JsonProperty("Quantidade de Reapresentações")
	private String quantidade_representante;
			
	@JsonProperty("Representante")
	private String representante;
			
	@JsonProperty("Resultado da Ocorrência")
	private String resultado_ocorrencia;
			
	@JsonProperty("Situação")
	private String situacao;
			
	@JsonProperty("Sub-Motivo")
	private String submotivo;
			
	@JsonProperty("Sub-Motivo Principal?")
	private String submotivo_principal;
			
	@JsonProperty("Tipo de Público")
	private String tipo_publico;
			
	@JsonProperty("Usuário de Abertura da Ocorrência")
	private String user_abertura;
			
	@JsonProperty("Variedade do Produto")
	private String 	variedade_produto;
}
