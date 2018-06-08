package br.com.caelum.financas.modelo;

import java.math.BigDecimal;

public class ValorPorMesEAno {
	
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	
	public ValorPorMesEAno(Integer mes, Integer ano, BigDecimal valor) {
		super();
		this.mes = mes;
		this.ano = ano;
		this.valor = valor;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	

}
