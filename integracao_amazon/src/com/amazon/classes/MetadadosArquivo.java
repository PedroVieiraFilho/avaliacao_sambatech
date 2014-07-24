package com.amazon.classes;

import java.util.Date;

import com.amazonaws.services.s3.model.ObjectMetadata;

public class MetadadosArquivo {
	public String nome;
	public String descricao;
	public Date dataCriacao;
	public Date dataAlteracao;
	public Long tamanho; //Em bytes
	public String usuarioCriador;
	public String usuarioModificador;
	public String extensao;
	
	public ObjectMetadata objectMetadata;
		
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public Date getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	public Long getTamanho() {
		return tamanho;
	}
	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}
	public String getUsuarioCriador() {
		return usuarioCriador;
	}
	public void setUsuarioCriador(String usuarioCriador) {
		this.usuarioCriador = usuarioCriador;
	}
	public String getUsuarioModificador() {
		return usuarioModificador;
	}
	public void setUsuarioModificador(String usuarioModificador) {
		this.usuarioModificador = usuarioModificador;
	}
	public String getExtensao() {
		return extensao;
	}
	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}
	
	public void defineObjectMetadata() {
		this.objectMetadata = new ObjectMetadata();
		this.objectMetadata.addUserMetadata("nome", this.getNome());
		this.objectMetadata.addUserMetadata("descricao", this.getDescricao());
		this.objectMetadata.addUserMetadata("usuarioCriador", this.getUsuarioCriador());
		this.objectMetadata.addUserMetadata("usuarioModificador", this.getUsuarioModificador());
		this.objectMetadata.addUserMetadata("dataAlteracao", this.getDataAlteracao().toString());
		this.objectMetadata.addUserMetadata("dataCriacao", this.getDataCriacao().toString());
		this.objectMetadata.addUserMetadata("tamanho", this.getTamanho().toString());
		this.objectMetadata.addUserMetadata("extensao", this.getExtensao());
	}
	
	public ObjectMetadata getObjectMetadata() {
		return this.objectMetadata;
	}
	
}
