package com.amazon.servicos;

import java.io.File;
import java.util.ArrayList;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
// Define identificador do arquivo no serviço (bucket)
// Cria bucket
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sambatech.conversorvideo.geral.ArquivosDTO;

public class ArmazenamentoNuvem {
	String bucketNamePrefix = "s3-bucket-sambatech-conversor-video-";

	public AmazonS3 s3;

	public ArmazenamentoNuvem() {
		s3 = new AmazonS3Client();
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		s3.setRegion(usWest2);
	}

	public String enviaArquivo(String nomeArquivo, String mediaId, File streamArquivo) {
		String resultadoProcesso = "";
		String msgErro = "";

		// Define identificador do arquivo no serviço (bucket)
		String bucketName = bucketNamePrefix + mediaId;
		String key = nomeArquivo;

		try {
			// Cria bucket
			System.out.println("Creating bucket " + bucketName + "\n");
			this.s3.createBucket(bucketName);

			// Faz upload arquivo
			System.out.println("Uploading a new object to S3 from a file\n");
			s3.putObject(new PutObjectRequest(bucketName, key, streamArquivo));

			resultadoProcesso = "SUCESSO - BucketName " + bucketName
					+ " successfully created.";

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
			resultadoProcesso = msgErro;
			System.out.println(msgErro);
		} catch (AmazonClientException ace) {
			msgErro = "Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network. Error Message: "
					+ ace.getMessage();
			msgErro = "FALHA - ".concat(msgErro);
			resultadoProcesso = msgErro;
			System.out.println(msgErro);
		}

		return resultadoProcesso;
	}

	public Boolean verificaArquivoExiste(String nomeArquivo) {
		Boolean arquivoExiste = false;

		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
				.withBucketName(bucketNamePrefix));
		for (S3ObjectSummary objeto : objectListing.getObjectSummaries()) {
			if (objeto.getKey() == nomeArquivo) {
				arquivoExiste = true;
				break;
			}
		}

		return arquivoExiste;
	}

	public ArrayList<ArquivosDTO> listaArquivos(String diretorioNuvem) {
		ArrayList<ArquivosDTO> listaArquivos = new ArrayList<ArquivosDTO>();
		ArquivosDTO registroArquivo;

		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
				.withBucketName(bucketNamePrefix));
		for (S3ObjectSummary objeto : objectListing.getObjectSummaries()) {
			registroArquivo = new ArquivosDTO();

			registroArquivo.setNomeOriginal(objeto.getKey());
			registroArquivo.setTamanho(objeto.getSize());
			registroArquivo.setUsuarioCriacao(objeto.getOwner().getDisplayName());

			System.out.println(" - " + objeto.getKey() + "  " + "(size = "
					+ objeto.getSize() + ")");
			listaArquivos.add(registroArquivo);
		}

		return listaArquivos;
	}
	
	public S3ObjectInputStream baixaArquivo(String nomeArquivo) {
		String bucketName = "";
		
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketNamePrefix));
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

}
