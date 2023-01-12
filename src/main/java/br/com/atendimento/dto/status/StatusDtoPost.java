package br.com.atendimento.dto.status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.atendimento.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusDtoPost {
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String nome;
	
	public Status converter() {		
		return new Status(nome);
	}	
}
