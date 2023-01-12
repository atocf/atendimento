package br.com.atendimento.dto.squad;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SquadDtoPut {
	
	@NotNull
	private Boolean ativo;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String nome;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String coordenacao;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String po;
}
