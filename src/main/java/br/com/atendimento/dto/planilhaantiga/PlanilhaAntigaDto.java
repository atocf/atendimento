package br.com.atendimento.dto.planilhaantiga;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlanilhaAntigaDto {
	
	private Long protocolo;
	
	private String analista;	
	
	private String card;	
	
	private String squad;	
	
	private String observcao; 	
	
	private String causa_raiz;	
	
	private String status;	
	
	private Date data_status;	
	
	private String ocorrencia;
	
	private String descricao;	
	
	private String submotivo; 
	
	private String cpf;
	
	private String canal_de_atendimento;	
	
	private Date data_abertura;	
	
	private Date data_vencimento;
	
	private String nome;	
	
	private String status_senha;	
	
	private String email;	
	
	private String telefone;	
	
	private String telefone_sms;	
	
	private String ultima_atualizacao_cadastral;	
	
	private String escopo;	
	
	private String conta;	
	
	private String cartoes;	
	
	private String reabertura;	
}
