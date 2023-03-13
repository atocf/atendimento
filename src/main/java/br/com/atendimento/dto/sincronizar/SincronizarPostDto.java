package br.com.atendimento.dto.sincronizar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SincronizarPostDto {
	
	private Integer agencia;
	private Integer conta;
	private String dataSincronismo;
}
