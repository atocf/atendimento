package br.com.atendimento.dto.kibana;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KibanaDtoPut {
	
	@NotNull
	private Boolean ativo;
	
	@NotNull
	@NotBlank
	@NotEmpty
	String descricao;
	
	@NotNull
	String[] link;
}
