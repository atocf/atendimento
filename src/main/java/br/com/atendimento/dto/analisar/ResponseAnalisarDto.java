package br.com.atendimento.dto.analisar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseAnalisarDto {
	
	private int total_devolver_fila_errada;
	private int total_devolver_fila_errada_conta_corrente;
	private int total_devolver_fila_errada_bmg_empresa; 
	private int total_devolver_fila_errada_digital;
	private int total_devolver_fila_errada_fraude;
	private int total_devolver_fila_errada_geare;
	private int total_devolver_fila_errada_geback;
	private int total_devolver_atendimento;
	private int total_fechar;
	private int total_sincronizado;
	private int total_atualizado_dados_pjtinha;
	private int total_limpo_redis;
}

