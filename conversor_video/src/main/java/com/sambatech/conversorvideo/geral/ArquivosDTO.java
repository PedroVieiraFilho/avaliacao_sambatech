package com.sambatech.conversorvideo.geral;

import java.util.Date;

public class ArquivosDTO {
	String nomeOriginal;
	String nomeConvertido;
	Date dataInclusao;
	Date dataConversao;
	Long tamanho;
	String usuarioCriacao;
	StatusArquivo status;
	
	public String getNomeOriginal() {
		return nomeOriginal;
	}
	public void setNomeOriginal(String nomeOriginal) {
		this.nomeOriginal = nomeOriginal;
	}
	public String getNomeConvertido() {
		return nomeConvertido;
	}
	public void setNomeConvertido(String nomeConvertido) {
		this.nomeConvertido = nomeConvertido;
	}
	public Date getDataInclusao() {
		return dataInclusao;
	}
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	public Date getDataConversao() {
		return dataConversao;
	}
	public void setDataConversao(Date dataConversao) {
		this.dataConversao = dataConversao;
	}
	public Long getTamanho() {
		return tamanho;
	}
	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}
	public String getUsuarioCriacao() {
		return usuarioCriacao;
	}
	public void setUsuarioCriacao(String usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}
	public StatusArquivo getStatus() {
		return status;
	}
	public void setStatus(StatusArquivo status) {
		this.status = status;
	}


}
