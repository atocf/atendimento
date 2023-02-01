package br.com.atendimento.dto.analisar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseAnalisarDto {
	
	private int total_devolver_fila_errada;
	private int total_devolver_fila_errada_bmg_empresa;
	private int total_devolver_fila_errada_conta_corrente;
	private int total_devolver_fila_errada_fraude;
	private int total_devolver_fila_errada_geare;
	private int total_devolver_fila_errada_geback;
}