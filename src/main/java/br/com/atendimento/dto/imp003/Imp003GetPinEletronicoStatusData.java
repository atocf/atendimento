package br.com.atendimento.dto.imp003;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Imp003GetPinEletronicoStatusData {
	
	private Integer codigoStatus;
	private String descricaoStatus;
}
