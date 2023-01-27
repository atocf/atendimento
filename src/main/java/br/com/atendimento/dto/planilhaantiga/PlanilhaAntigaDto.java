package br.com.atendimento.dto.planilhaantiga;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlanilhaAntigaDto {
	
	
	private String analista;	
	
	private String card;	
	
	private String squad;	
	
	private String observcao; 	
	
	private String causa_raiz;	
	
	private String status;	
	
	private Date data_status;	
	
	private Long ocorrencia;

}
