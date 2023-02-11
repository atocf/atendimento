package br.com.atendimento.dto.kibana;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.atendimento.entity.Kibana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KibanaDtoPost {
	
	@NotNull
	@NotBlank
	@NotEmpty
	String nome;
	
	@NotNull
	@NotBlank
	@NotEmpty
	String descricao;
	
	@NotNull
	String[] link;
	
	public Kibana converter() {		
		return new Kibana(nome, descricao, link);
	}	
}
