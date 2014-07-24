package com.encoding.servicos;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Properties;

import br.com.sambatech.infra.java.processo.Processo;
import br.com.sambatech.infra.java.processo.StatusProcesso;

public class ConversaoVideo {
	String resultado = "";
	
	/*
	 * TODO - Trata XML de resposta conforme o tipo de requisiçao
	 */
	private Processo trataRespostaRequisicao(String nomeProcessoAtual, StringBuffer xmlResposta) {
    	Processo processoAtual = new Processo("trataRespostaRequisicao");
    	ArrayList<Object> dadosSaidaProcesso = new ArrayList<Object>();
    	
    	return processoAtual;
	}
	
	/*
	 * Conecta ao serviço de encoding
	 */
	private URL conectaServicoEncoding(Properties configuracoes) {
        URL server = null;
        try {
            String url = configuracoes.getProperty("integracao_enconding.endpoint");;
            System.out.println("Connecting to:"+url);
            server = new URL(url);

        } catch (MalformedURLException mfu) {
            mfu.printStackTrace();
            resultado = "FALHA - Conexão não realizada. Detalhes: ".concat(mfu.getMessage());
        }
        return server;
	}
	
	/*
	 * Executa Processo de Requisição no serviço de Encoding
	 */
    private Processo executaRequisicaoEncoding(Properties configuracoes, URL server, String nomeRequisicao, StringBuffer xml) {
    	Processo processoAtual = new Processo("executaRequisicaoEncoding");
    	ArrayList<Object> dadosSaidaProcesso = new ArrayList<Object>();

        try {
            String sRequest = "xml=" + URLEncoder.encode(xml.toString(), "UTF8");
           
            System.out.println("Open new connection to tunnel");
            HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
            urlConnection.setRequestMethod( "POST" );
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(Integer.valueOf(configuracoes.getProperty("integracao_enconding.connect_timeout")));
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            BufferedWriter out = new BufferedWriter( new OutputStreamWriter( urlConnection.getOutputStream() ) );
            out.write(sRequest);
            out.flush();
            out.close();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            System.out.println("Response:"+urlConnection.getResponseCode());
            System.out.println("Response:"+urlConnection.getResponseMessage());

            //Alimenta a saída do Processo
            dadosSaidaProcesso.add(urlConnection.getResponseMessage()); 
            processoAtual.setDadosSaida(dadosSaidaProcesso);
            
            StringBuffer strbuf = new StringBuffer();
            byte[] buffer = new byte[1024 * 4];
            try {
                int n = 0;
                while (-1 != (n = is.read(buffer))) {
                    strbuf.append(new String(buffer, 0, n));
                }
                is.close();
                processoAtual.setMsgSucesso("SUCESSO - Requisição para ".concat(nomeRequisicao).concat(" finalizada"));
                processoAtual.setStatus(StatusProcesso.FINALIZADO_SUCESSO);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                processoAtual.setMsgErro("FALHA - Requisição para ".concat(nomeRequisicao).concat(" não executada. Detalhes: ").concat(ioe.getMessage()));
                processoAtual.setStatus(StatusProcesso.FINALIZADO_FALHA);
            }
            System.out.println(strbuf.toString()); 
        } catch (Exception exp) {
            exp.printStackTrace();
            processoAtual.setMsgErro("FALHA - Requisição para ".concat(nomeRequisicao).concat(" não executada. Detalhes: ").concat(exp.getMessage()));
            processoAtual.setStatus(StatusProcesso.FINALIZADO_FALHA);
        }
        return processoAtual;
    }
    
	/*
	 * Implementa uso da API AddMedia (upload a partir do servidor)
	 */
	public Processo enviaArquivoParaEnconding(Properties configuracoes, String urlSourceMedia){
        String userID = configuracoes.getProperty("integracao_enconding.userid");
        String userKey = configuracoes.getProperty("integracao_enconding.userkey");
        
        StringBuffer xml = new StringBuffer();
        String nomeProcessoAtual = "enviaArquivoParaEnconding";
        Processo processoEnvio = new Processo(nomeProcessoAtual);

        xml.append("<?xml version=\"1.0\"?>");
        xml.append("<query>");
        xml.append("<userid>"+userID+"</userid>");
        xml.append("<userkey>"+userKey+"</userkey>");
        xml.append("<action>AddMedia</action>");
        xml.append("<source>"+urlSourceMedia+"</source>");
        xml.append("</query>");

        //Conecta no serviço
        URL server = conectaServicoEncoding(configuracoes);
        
        //Executa requisiçao
        Processo subprocessoExecReq = executaRequisicaoEncoding(configuracoes, server, "executaRequisicaoEncoding", xml);
        
        //Verifica se a requisicao foi executada com sucesso. Se sim, trata XML resposta
        if (subprocessoExecReq.getStatus().equals(StatusProcesso.FINALIZADO_SUCESSO)) {
        	//Trata resposta requisiçao
        	StringBuffer xmlResposta = (StringBuffer) subprocessoExecReq.getDadosSaida().get(0);
        	Processo subprocessoTrataResposta = trataRespostaRequisicao(nomeProcessoAtual, xmlResposta);
        	
            //Verifica se o tratamento do XML de resposta foi executado com sucesso. Se sim, assume a saída deste subprocesso como a saída do processo
            if (subprocessoTrataResposta.getStatus().equals(StatusProcesso.FINALIZADO_SUCESSO)) {
            	
            } else {
            	processoEnvio.setStatus(subprocessoTrataResposta.getStatus());
            	processoEnvio.setMsgErro(subprocessoTrataResposta.getMsgErro());            	
            }
        } else {
        	processoEnvio.setStatus(subprocessoExecReq.getStatus());
        	processoEnvio.setMsgErro(subprocessoExecReq.getMsgErro());
        }

        return processoEnvio;
	}    

	/*
	 * Implementa uso da API ProcessMedia
	 */		
	public Processo executaJobEncoding(Properties configuracoes, String mediaId){
        String nomeProcessoAtual = "executaJobEncoding";
        Processo processoExecutaJob = new Processo(nomeProcessoAtual);
		
        String userID = configuracoes.getProperty("integracao_enconding.userid");
        String userKey = configuracoes.getProperty("integracao_enconding.userkey");
        StringBuffer xml = new StringBuffer();
        
        Processo processoBuscaetalhesArquivo = buscaDetalheArquivo(configuracoes, mediaId);
        ArrayList<String> detalhesArquivo = (ArrayList<String>) processoBuscaetalhesArquivo.getDadosSaida().get(0);
        String nomeArquivo = detalhesArquivo.get(0);
        
        Integer posSeparadorExtensao = nomeArquivo.indexOf(".",0);
        String nomeArquivoSaida = nomeArquivo.substring(0, posSeparadorExtensao)+".mp4";
        
        xml.append("<?xml version=\"1.0\"?>");
        xml.append("<query>");
        	xml.append("<userid>"+userID+"</userid>");
        	xml.append("<userkey>"+userKey+"</userkey>");
        	xml.append("<action>ProcessMedia</action>");
        	xml.append("<mediaid>"+mediaId+"</mediaid>");
        
        	xml.append("<region>[us-east-1]</region>"); 
        	xml.append("<notify_format>[xml]</notify_format>");
        	xml.append("<notify>"+configuracoes.getProperty("integracao_encoding.notity_email")+"</notify>");
        	xml.append("<notify_encoding_errors>"+configuracoes.getProperty("integracao_encoding.notity_email_error")+"</notify_encoding_errors>");
        
        	xml.append("<format>");
        		xml.append("<output>[Output format]</output>");
        		xml.append("<video_codec>[Video Codec]</video_codec>");
                xml.append("<audio_codec>[Audio Codec]</audio_codec>");
                xml.append("<bitrate>[Video bitrate]</bitrate>");
                xml.append("<audio_bitrate>[Audio bitrate]</audio_bitrate>");
                xml.append("<audio_sample_rate>[Audio quality]</audio_sample_rate>");
                xml.append("<audio_channels_number>[Audio channels number]</audio_channels_number>");
                xml.append("<keep_aspect_ratio>[yes]</keep_aspect_ratio>");
                xml.append("<output>mp4</output>");
                xml.append("<destination>"+configuracoes.getProperty("integracao_amazon.s3.default_path")+"/"+nomeArquivoSaida+"</destination>");	
                xml.append("<audio_sync>[1]</audio_sync>"); 
                xml.append("<video_sync>auto</video_sync>");
                xml.append("<force_interlaced>no</force_interlaced>");
                xml.append("<strip_chapters>[no]</strip_chapters>");
            xml.append("</format>"); 
        xml.append("</query>");

        //Conecta no serviço
        URL server = conectaServicoEncoding(configuracoes);
        
        //Executa requisiçao
        Processo subprocessoExecReq = executaRequisicaoEncoding(configuracoes, server, "criaJobParaEncoding", xml);

        //Verifica se a requisicao foi executada com sucesso. Se sim, trata XML resposta
        if (subprocessoExecReq.getStatus().equals(StatusProcesso.FINALIZADO_SUCESSO)) {
        	//Trata resposta requisiçao
        	StringBuffer xmlResposta = (StringBuffer) subprocessoExecReq.getDadosSaida().get(0);
        	Processo subprocessoTrataResposta = trataRespostaRequisicao(nomeProcessoAtual, xmlResposta);
        	
            //Verifica se o tratamento do XML de resposta foi executado com sucesso. Se sim, assume a saída deste subprocesso como a saída do processo
            if (subprocessoTrataResposta.getStatus().equals(StatusProcesso.FINALIZADO_SUCESSO)) {
            	
            } else {
            	processoExecutaJob.setStatus(subprocessoTrataResposta.getStatus());
            	processoExecutaJob.setMsgErro(subprocessoTrataResposta.getMsgErro());            	
            }
        } else {
        	processoExecutaJob.setStatus(subprocessoExecReq.getStatus());
        	processoExecutaJob.setMsgErro(subprocessoExecReq.getMsgErro());
        }

        return processoExecutaJob;
	}
	
	/*
	 * Implementa uso da API GetMediaInfo
	 */	
	public Processo buscaDetalheArquivo(Properties configuracoes, String mediaId){
        String nomeProcessoAtual = "buscaDetalheArquivo";
        Processo processoBuscaDetalheArq = new Processo(nomeProcessoAtual);
		
        String userID = configuracoes.getProperty("integracao_enconding.userid");
        String userKey = configuracoes.getProperty("integracao_enconding.userkey");
        StringBuffer xml = new StringBuffer();

        xml.append("<?xml version=\"1.0\"?>");
        xml.append("<query>");
        xml.append("<userid>"+userID+"</userid>");
        xml.append("<userkey>"+userKey+"</userkey>");
        xml.append("<action>GetMediaInfo</action>");
        xml.append("<mediaid>"+mediaId+"</mediaid>");
        xml.append("</query>");

        //Conecta no serviço
        URL server = conectaServicoEncoding(configuracoes);
        
        //Executa requisiçao
        Processo subprocessoExecReq = executaRequisicaoEncoding(configuracoes, server, "buscaDetalheArquivo", xml);

        //Verifica se a requisicao foi executada com sucesso. Se sim, trata XML resposta
        if (subprocessoExecReq.getStatus().equals(StatusProcesso.FINALIZADO_SUCESSO)) {
        	//Trata resposta requisiçao
        	StringBuffer xmlResposta = (StringBuffer) subprocessoExecReq.getDadosSaida().get(0);
        	Processo subprocessoTrataResposta = trataRespostaRequisicao(nomeProcessoAtual, xmlResposta);
        	
            //Verifica se o tratamento do XML de resposta foi executado com sucesso. Se sim, assume a saída deste subprocesso como a saída do processo
            if (subprocessoTrataResposta.getStatus().equals(StatusProcesso.FINALIZADO_SUCESSO)) {
            	
            } else {
            	processoBuscaDetalheArq.setStatus(subprocessoTrataResposta.getStatus());
            	processoBuscaDetalheArq.setMsgErro(subprocessoTrataResposta.getMsgErro());            	
            }
        } else {
        	processoBuscaDetalheArq.setStatus(subprocessoExecReq.getStatus());
        	processoBuscaDetalheArq.setMsgErro(subprocessoExecReq.getMsgErro());
        }
        
        return processoBuscaDetalheArq;
	}

	/*
	 * TODO - Implementa uso da API GetStatus
	 */	
	

}
