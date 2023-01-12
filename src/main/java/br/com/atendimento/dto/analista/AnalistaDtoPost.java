package br.com.atendimento.dto.analista;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.atendimento.entity.Analista;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalistaDtoPost {
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String nome;
	
	public Analista converter() {		
		return new Analista(nome);
	}	
}
