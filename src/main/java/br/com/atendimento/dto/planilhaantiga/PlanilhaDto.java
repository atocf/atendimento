package br.com.atendimento.dto.planilhaantiga;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlanilhaDto {
	
	private String analista;
	private Long ocorrencia;
	private String card;
	private String squad;
	private String status;
	private Date data_status;
	private String observacao;
	private String causa_raiz;
}
