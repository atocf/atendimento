package br.com.atendimento.dto.analista;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalistaDtoPut {
	
	@NotNull
	private Boolean ativo;
}
