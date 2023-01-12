package br.com.atendimento.dto.status;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusDtoPut {
	
	@NotNull
	private Boolean ativo;
}
