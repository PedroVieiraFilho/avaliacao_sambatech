package br.com.sambatech.infra.java.processo;

import java.util.ArrayList;

public class Processo {
	
	public StatusProcesso status; 
	public String msgSucesso;
	public String msgErro;
	public ArrayList<Object> dadosEntrada;
	public ArrayList<Object> dadosSaida;
	public String nomeProcesso;
	public String nivelDetalhe;
	public String nomeProcessoAnterior;
	public String nomeProximoProcesso;
	public ArrayList<Processo> listaSubProcessosExecutados;
	
	public Processo(String nomeProcesso) {
		iniciaProcesso(nomeProcesso);
		this.dadosEntrada = new ArrayList<Object>();
		this.dadosSaida = new ArrayList<Object>();
	}
	
	public Processo(String nomeProcesso, ArrayList<Object> dadosEntrada) {
		iniciaProcesso(nomeProcesso);
		this.dadosEntrada = new ArrayList<Object>();
		this.dadosEntrada = dadosEntrada;
		this.dadosSaida = new ArrayList<Object>();
	}	
	
	private void iniciaProcesso(String nomeProcesso) {
		this.status = StatusProcesso.INICIADO;
		this.nomeProcesso = nomeProcesso;
		this.msgSucesso = "";
		this.msgErro = "";		
	}

	public StatusProcesso getStatus() {
		return status;
	}

	public void setStatus(StatusProcesso status) {
		this.status = status;
	}

	public String getMsgSucesso() {
		return msgSucesso;
	}

	public void setMsgSucesso(String msgSucesso) {
		this.msgSucesso = msgSucesso;
	}

	public String getMsgErro() {
		return msgErro;
	}

	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}

	public ArrayList<Object> getDadosEntrada() {
		return dadosEntrada;
	}

	public void setDadosEntrada(ArrayList<Object> dadosEntrada) {
		this.dadosEntrada = dadosEntrada;
	}

	public ArrayList<Object> getDadosSaida() {
		return dadosSaida;
	}

	public void setDadosSaida(ArrayList<Object> dadosSaida) {
		this.dadosSaida = dadosSaida;
	}

	public String getNomeProcesso() {
		return nomeProcesso;
	}

	public void setNomeProcesso(String nomeProcesso) {
		this.nomeProcesso = nomeProcesso;
	}

	public String getNivelDetalhe() {
		return nivelDetalhe;
	}

	public void setNivelDetalhe(String nivelDetalhe) {
		this.nivelDetalhe = nivelDetalhe;
	}

	public String getNomeProcessoAnterior() {
		return nomeProcessoAnterior;
	}

	public void setNomeProcessoAnterior(String nomeProcessoAnterior) {
		this.nomeProcessoAnterior = nomeProcessoAnterior;
	}

	public String getNomeProximoProcesso() {
		return nomeProximoProcesso;
	}

	public void setNomeProximoProcesso(String nomeProximoProcesso) {
		this.nomeProximoProcesso = nomeProximoProcesso;
	}

	public ArrayList<Processo> getListaSubProcessosExecutados() {
		return listaSubProcessosExecutados;
	}

	public void setListaSubProcessosExecutados(ArrayList<Processo> listaSubProcessosExecutados) {
		this.listaSubProcessosExecutados = listaSubProcessosExecutados;
	}
	
	public void adicionaSubProcesso(Processo subprocesso) {
		this.listaSubProcessosExecutados.add(subprocesso);
	}
	
	public void redefineSituacaoProcessoAtual(Processo subprocesso) {
		this.setMsgErro(subprocesso.getMsgErro());
		this.setMsgSucesso(subprocesso.getMsgSucesso());
		this.setStatus(subprocesso.getStatus());
	}
	
}
