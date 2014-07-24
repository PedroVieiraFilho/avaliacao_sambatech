package com.sambatech.conversorvideo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import br.com.sambatech.infra.java.processo.Processo;

import com.amazon.servicos.ArmazenamentoNuvem;
import com.sambatech.conversorvideo.geral.EtapaConversao;

/**
 * Handles requests for the application home page.
 */
@Controller
@SessionAttributes
public class EtapaConversaoController {
	public static final String AWS_DEFAULT_BUCKET_NAME = "s3-bucket-sambatech-conversor-video";
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	//Inicializa dados tela inicial 
	public String etapaConversao(Model model) {
		
		EtapaConversao etapaInicial = new EtapaConversao();
		etapaInicial.setSubtitulo("Bem vindo! Para arquivos de mídia desconhecidos, este site permite o armazenamento na Nuvem (usando Amazon S3), conversão da mídia (para o formato MP4 usando Encoding.com), e a visualização desta mídia pelo browser");				
		etapaInicial.setIdEtapa(1);
		etapaInicial.setAcao("guardarArquivoAction");
        model.addAttribute("etapaConversao", etapaInicial); 
		
		return "etapaConversao";
	}
	
	//Processa o armazenamento do arquivo
	@RequestMapping(value = "/guardarArquivoAction", method = RequestMethod.POST)
    public String guardarArquivoAction(@ModelAttribute("etapaConversao") EtapaConversao etapaAtual, @RequestParam("file") MultipartFile conteudoArquivo, Model model) {
    	
        //Recupera o nome do arquivo do upload
        String nomeArquivo = conteudoArquivo.getOriginalFilename();
        
        //Guarda arquivo na Nuvem usando (Amazon AWS S3)
        ArmazenamentoNuvem servicoArmazenamento = new ArmazenamentoNuvem();
        Processo processoEnvioArquivoNuvem = servicoArmazenamento.enviaArquivoLocal(AWS_DEFAULT_BUCKET_NAME, "", nomeArquivo, "", conteudoArquivo);
                
        //Retorna resposta para tela
        etapaAtual.setAcao("converterArquivoAction");
        etapaAtual.setSubtitulo("");
        etapaAtual.setIdEtapa(2);
        model.addAttribute("etapaConversao", etapaAtual);
         
        return "etapaConversao";
    }
	
	public String converterArquivoAction(@ModelAttribute("etapaConversao") EtapaConversao etapaAtual, Model model) {
		
		return "etapaConversao";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String visualizarArquivoAction(@ModelAttribute("etapaConversao") EtapaConversao etapaAtual, Model model) {
		
        return "visualizar";
	}
	
}
