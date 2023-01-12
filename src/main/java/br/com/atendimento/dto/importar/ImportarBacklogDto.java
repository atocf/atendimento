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
public class ImportarBacklogDto {
	
	@JsonProperty("1ª DESCRIÇÃO")
	private String descricao;
	
	@JsonProperty("Agência")
	private String agencia;	
	
	@JsonProperty("Área Responsável")
	private String area_responsavel;
	
	@JsonProperty("Banco")
	private String banco;	
	
	@JsonProperty("Bancos")
	private String bancos;
	
	@JsonProperty("Canal de Atendimento")
	private String canal;
	
	@JsonProperty("Classificação do Canal")
	private String classificacao_do_canal;	
	
	@JsonProperty("Código da Ocorrência")
	private String ocorrencia;		
	
	@JsonProperty("Conta")
	private String conta;
	
	@JsonProperty("CPF")
	private String cpf;
	
	@JsonProperty("Data Abertura Ocorrência")
	private String dt_abertura;
	
	@JsonProperty("Data da Conclusão do Protocolo")
	private String dt_conclusao;
	
	@JsonProperty("Data Previsão Final da Ocorrência")
	private String dt_previsao;
	
	@JsonProperty("Data Recebimento")
	private String dt_recebimento;
	
	@JsonProperty("Forma de Contato")
	private String forma_contato;
	
	@JsonProperty("Forma de Envio do Documento")
	private String forma_envio;
	
	@JsonProperty("Forma de Manifestação")
	private String forma_manifestacao;
	
	@JsonProperty("Forma de Retorno")
	private String forma_retorno;
	
	@JsonProperty("Hora Abertura Ocorrência")
	private String h_abertura;

	@JsonProperty("Hora Recebimento")
	private String h_recebimento;
	
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
	private String qtd_reapresentaçoes;
	
	@JsonProperty("Resultado da Ocorrência")
	private String resultado_ocorrencia;
	
	@JsonProperty("Situação")
	private String situacao;
	
	@JsonProperty("Sub-Motivo")
	private String submotivo;
	
	@JsonProperty("Tipo de Público")
	private String tp_publico;
	
	@JsonProperty("Usuário de Abertura da Ocorrência")
	private String user_abertura;
	
	@JsonProperty("Variedade do Produto")
	private String variedade_produto;	
}
