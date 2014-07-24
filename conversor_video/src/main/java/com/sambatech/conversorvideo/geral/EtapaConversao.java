package com.sambatech.conversorvideo.geral;

public class EtapaConversao {
	public String acao;
	public String subtitulo;
	public Integer idEtapa;
	public String msgErro;
	public String msgSucesso;
	public String backTraceEtapas;
	public String instrucao;
	public String pathArquivoLocal;
	public String pathArquivoRemoto;
	
	public Integer getIdEtapa() {
		return idEtapa;
	}
	public void setIdEtapa(Integer idEtapa) {
		this.idEtapa = idEtapa;
	}
	public String getMsgErro() {
		return msgErro;
	}
	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}
	public String getMsgSucesso() {
		return msgSucesso;
	}
	public void setMsgSucesso(String msgSucesso) {
		this.msgSucesso = msgSucesso;
	}
	public String getInstrucao() {
		return instrucao;
	}
	public void setInstrucao(String instrucao) {
		this.instrucao = instrucao;
	}
	public String getPathArquivoLocal() {
		return pathArquivoLocal;
	}
	public void setPathArquivoLocal(String pathArquivoLocal) {
		this.pathArquivoLocal = pathArquivoLocal;
	}
	public String getPathArquivoRemoto() {
		return pathArquivoRemoto;
	}
	public void setPathArquivoRemoto(String pathArquivoRemoto) {
		this.pathArquivoRemoto = pathArquivoRemoto;
	}
	public String getSubtitulo() {
		return subtitulo;
	}
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}
	public String getBackTraceEtapas() {
		return backTraceEtapas;
	}
	public void setBackTraceEtapas(String backTraceEtapas) {
		this.backTraceEtapas = backTraceEtapas;
	}
	public String getAcao() {
		return acao;
	}
	public void setAcao(String acao) {
		this.acao = acao;
	}
}
