package br.com.atendimento.dto.squad;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.atendimento.entity.Squad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SquadDtoPost {
	
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
	
	public Squad converter() {		
		return new Squad(nome, coordenacao, po);
	}	
}
