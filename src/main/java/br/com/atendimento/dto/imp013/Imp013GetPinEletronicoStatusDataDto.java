package br.com.atendimento.dto.imp013;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Imp013GetPinEletronicoStatusDataDto {
	
	private Integer codigoStatus;
	private String descricaoStatus;
}
