package com.sambatech.conversorvideo.geral;

public enum StatusArquivo {
	CARREGANDO(1), CARREGADO(2), CONVERTENDO(3), CONVERTIDO(4);
	
	private final int etapaProcesso; 
	
	StatusArquivo(int etapa) { 
		etapaProcesso = etapa; 
	}

	public int getEtapaProcesso() {
		return etapaProcesso;
	}
}
