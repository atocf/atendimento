package br.com.atendimento.dto.dgp001;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Dgp001Telefone {
	
	private Integer ddd;
	private Integer numero;
	private Boolean enviaSms;	
}
