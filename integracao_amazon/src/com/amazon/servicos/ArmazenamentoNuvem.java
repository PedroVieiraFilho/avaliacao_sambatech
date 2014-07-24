package com.amazon.servicos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import br.com.sambatech.infra.java.processo.Processo;
import br.com.sambatech.infra.java.processo.StatusProcesso;

import com.amazon.classes.MetadadosArquivo;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class ArmazenamentoNuvem {
	public AmazonS3 s3;
	public AWSCredentials credenciais;

	public ArmazenamentoNuvem() {		
		this.setS3(new AmazonS3Client(new ProfileCredentialsProvider()));
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		this.getS3().setRegion(usWest2);
	}
	
	public Processo criaBucket(String nomeBucket){
    	Processo processoAtual = new Processo("criaBucket");
		
		try {
			processoAtual.setStatus(StatusProcesso.INICIADO);
			
			// Verifica se o bucket existe
			if (!this.getS3().doesBucketExist(nomeBucket)) {
				// Cria bucket
				System.out.println("Creating bucket " + nomeBucket + "\n");
				this.getS3().createBucket(nomeBucket);
				processoAtual.setStatus(StatusProcesso.FINALIZADO_SUCESSO);	
			}
		} catch (AmazonClientException exception) {
			processoAtual.setMsgErro(exception.getMessage());
			processoAtual.setStatus(StatusProcesso.FINALIZADO_FALHA);
		}
		return processoAtual;
	}
	

	public Processo enviaArquivoLocal(String nomeBucket, String nomeDiretorio, String nomeArquivo, String descricao, MultipartFile conteudoArquivo) {
		Processo processoAtual = new Processo("criaBucket");
		String msgErro = "";
		Boolean continuaProcesso = true;

		// Define identificador do arquivo no serviço (bucket)
		String key = UUID.randomUUID().toString().replaceAll("-", ""); 

		try {
			Processo subProcessoCriaBucket = this.criaBucket(nomeBucket);
			
			if (subProcessoCriaBucket.getStatus().equals(StatusProcesso.FINALIZADO_FALHA)) {
				processoAtual.redefineSituacaoProcessoAtual(subProcessoCriaBucket);
				return subProcessoCriaBucket;
			}
			
			//TODO - Adiciona para diretório
			
			//Cria metadados
			MetadadosArquivo metaArq = new MetadadosArquivo();
			metaArq.setNome(nomeArquivo);
			metaArq.setDataCriacao(new Date());
			metaArq.setDescricao(descricao);
			metaArq.setTamanho(conteudoArquivo.getSize());

			//Obtem conteúdo do arquivo
			InputStream inputStream = conteudoArquivo.getInputStream();
			
			//Referência: docs.aws.amazon.com/AmazonS3/latest/dev/IIJavaUploadFile.html
			//InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(nomeBucket, key);
			//InitiateMultipartUploadResult initResponse = s3.initiateMultipartUpload(initRequest);

			// Faz upload arquivo
			System.out.println("Uploading a new object to S3 from a file\n");
			s3.putObject(new PutObjectRequest(nomeBucket, key, inputStream, metaArq.getObjectMetadata()));

			processoAtual.setMsgSucesso("SUCESSO - BucketName " + nomeBucket + " successfully created.");
			processoAtual.setStatus(StatusProcesso.FINALIZADO_SUCESSO);

		} catch (AmazonServiceException ase) {
			msgErro = "Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason. Details: "
					+ "\r\n";
			msgErro.concat("Error Message:    " + ase.getMessage() + "\r\n");
			msgErro.concat("HTTP Status Code: " + ase.getStatusCode() + "\r\n");
			msgErro.concat("AWS Error Code:   " + ase.getErrorCode() + "\r\n");
			msgErro.concat("Error Type:       " + ase.getErrorType() + "\r\n");
			msgErro.concat("Request ID:       " + ase.getRequestId() + "\r\n");
			msgErro = "FALHA - ".concat(msgErro);
			processoAtual.setMsgErro(msgErro);
			processoAtual.setStatus(StatusProcesso.FINALIZADO_FALHA);
			System.out.println(msgErro);
		} catch (AmazonClientException ace) {
			msgErro = "Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network. Error Message: "
					+ ace.getMessage();
			msgErro = "FALHA - ".concat(msgErro);
			processoAtual.setMsgErro(msgErro);
			System.out.println(msgErro);
			processoAtual.setStatus(StatusProcesso.FINALIZADO_FALHA);
		} catch (FileNotFoundException e) {
			processoAtual.setMsgErro(e.getMessage());
			processoAtual.setStatus(StatusProcesso.FINALIZADO_FALHA);
		} catch (IOException e) {
			processoAtual.setMsgErro(e.getMessage());
			processoAtual.setStatus(StatusProcesso.FINALIZADO_FALHA);		}

		return processoAtual;
	}
	
	public Boolean verificaArquivoExiste(String nomeBucket, String nomeArquivo) {
		Boolean arquivoExiste = false;

		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
				.withBucketName(nomeBucket));
		for (S3ObjectSummary objeto : objectListing.getObjectSummaries()) {
			if (objeto.getKey() == nomeArquivo) {
				arquivoExiste = true;
				break;
			}
		}

		return arquivoExiste;
	}
	
	public List<MetadadosArquivo> listaArquivos(String nomeBucket, String prefixo, String diretorioNuvem) {
		ArrayList<MetadadosArquivo> listaArquivos = new ArrayList<MetadadosArquivo>();
		MetadadosArquivo metaArq;

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(nomeBucket);
        ObjectListing objectListing;
        
	    do {
	        objectListing = this.getS3().listObjects(listObjectsRequest);
	        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
	            System.out.println(" - " + objectSummary.getKey() + "  " +
	                    "(size = " + objectSummary.getSize() + 
	                    ")");
	        }
	        listObjectsRequest.setMarker(objectListing.getNextMarker());
	    } while (objectListing.isTruncated());

		return listaArquivos;
	}
	
	public S3ObjectInputStream baixaArquivo(String nomeBucket, String nomeArquivo) {
		String bucketName = "";
		
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(nomeBucket));
		for (S3ObjectSummary objeto : objectListing.getObjectSummaries()) {
			if (objeto.getKey() == nomeArquivo) {
				bucketName = objeto.getBucketName();
				break;
			}
		}		
		
        S3Object object = s3.getObject(new GetObjectRequest(bucketName, nomeArquivo));
        S3ObjectInputStream streamArquivo = object.getObjectContent();
        
		return streamArquivo;
	}

	public AmazonS3 getS3() {
		return s3;
	}

	public void setS3(AmazonS3 s3) {
		this.s3 = s3;
	}

	public AWSCredentials getCredenciais() {
		return credenciais;
	}

	public void setCredenciais(AWSCredentials credenciais) {
		this.credenciais = credenciais;
	}
}
