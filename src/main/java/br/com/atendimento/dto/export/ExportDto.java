package br.com.atendimento.dto.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExportDto {

	private String analista;
	private String c_atendimento;
	private String sub_motivo;
	private String [] kibana;
	private String equipe;
	private Integer reabertura;
	private Long ocorrencia;
	private Long protocolo;
	private String cpf;
	private String cnpj;
	private String card;
	private String squad;
	private String status;
	private String data_status;
	private String observacao;
	private String causa_raiz;
	private String dt_aberta;
	private String dt_vencimento;
	private String descricao;
	private String nome;
	private String status_senha;
	private String email;
	private String telefone;
	private Boolean telefone_sms;
	private String atualizacao_cadastral;
	private String escopo;
	private String conta;
	private String cartoes;
	private String Msg;
}
