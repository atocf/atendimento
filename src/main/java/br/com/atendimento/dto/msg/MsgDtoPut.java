package br.com.atendimento.dto.msg;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MsgDtoPut {
	
	@NotNull
	private Boolean ativo;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String descricao;
}
