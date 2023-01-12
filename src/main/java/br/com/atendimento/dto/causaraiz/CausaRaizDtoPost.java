package br.com.atendimento.dto.causaraiz;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.atendimento.entity.CausaRaiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CausaRaizDtoPost {
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String nome;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String descricao;
	
	public CausaRaiz converter() {		
		return new CausaRaiz(nome, descricao);
	}	
}
