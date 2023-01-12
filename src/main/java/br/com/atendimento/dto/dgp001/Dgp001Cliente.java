package br.com.atendimento.dto.dgp001;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Dgp001Cliente {
	
	private String nome;
	private String email;
	private String dataUltimaAtualizacao; 
	private Dgp001Telefone telefone; 
}
