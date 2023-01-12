package br.com.atendimento.dto.causaraiz;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CausaRaizDtoPut {
	
	@NotNull
	private Boolean ativo;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String descricao;
}
