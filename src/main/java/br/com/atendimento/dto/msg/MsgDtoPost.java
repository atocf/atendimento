package br.com.atendimento.dto.msg;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.atendimento.entity.Msg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MsgDtoPost {
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String nome;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String descricao;
	
	public Msg converter() {		
		return new Msg(nome, descricao);
	}	
}
