package br.com.sambatech.infra.java.processo;

public enum StatusProcesso {
		// Declaração das constantes
		INICIADO			 	(0, "Iniciado"),
		FINALIZADO_SUCESSO  	(1, "Finalizado com Sucesso"),
		FINALIZADO_FALHA 		(2, "Finalizado com Falha"),
		PARADO  				(3, "Parado"),
		EM_EXECUCAO 			(4, "Em execução");
		
		private final int idStatus;
		private final String descricao;
		
		private StatusProcesso(int idStatus, String descricao) {
			this.idStatus = idStatus;
			this.descricao = descricao;
		}

		public int getIdStatus() {
			return idStatus;
		}

		public String getDescricao() {
			return descricao;
		}		
}
