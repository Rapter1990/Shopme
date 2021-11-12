package com.shopme.admin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

public class AmazonS3Util {

	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3Util.class);
	
	private static final String BUCKET_NAME, ACCESS_KEY_ID, SECRET_ACCESS_KEY;

	static {
		
		BUCKET_NAME = System.getenv("AWS_BUCKET_NAME");
		ACCESS_KEY_ID = System.getenv("AWS_ACCESS_KEY_ID");
		SECRET_ACCESS_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");
		
		LOGGER.info("AmazonS3Util | BUCKET_NAME : " + BUCKET_NAME);
	}
	
	public static S3Client createAmazomS3() {
		
		AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
				   ACCESS_KEY_ID,
				   SECRET_ACCESS_KEY);
		
		S3Client client = S3Client.builder()
				.credentialsProvider(StaticCredentialsProvider.create(awsCreds))
				.build();
					
		
		return client;	
	}
	
	public static ListObjectsRequest listAllFilesInAmazonS3(String folderName) {
		ListObjectsRequest listRequest = ListObjectsRequest.builder()
				.bucket(BUCKET_NAME).prefix(folderName).build();
		
		return listRequest;
	}
	
	public static ListObjectsRequest listAllFilesInAmazonS3ForRemove(String folderName) {
		ListObjectsRequest listRequest = ListObjectsRequest.builder()
				.bucket(BUCKET_NAME).prefix(folderName + "/").build();
		
		return listRequest;
	}
	
	public static PutObjectRequest putFileInAmazonS3(String folderName, String fileName, InputStream inputStream) {
		PutObjectRequest request = PutObjectRequest.builder().bucket(BUCKET_NAME)
				.key(folderName + "/" + fileName).acl("public-read").build();
		
		return request;
	}
	
	public static DeleteObjectRequest deleteFileFromAmazonS3(String fileName) {
		DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(BUCKET_NAME)
				.key(fileName).build();
		
		return request;
	}
	
	public static List<String> listFolder(String folderName) {
		
		LOGGER.info("AmazonS3Util | listFolder is started");
		
		S3Client client = createAmazomS3();
		
		LOGGER.info("AmazonS3Util | listFolder | client : " + client.toString());
		
		ListObjectsRequest listRequest = listAllFilesInAmazonS3(folderName);
		
		LOGGER.info("AmazonS3Util | listFolder | listRequest : " + client.toString());
		
		ListObjectsResponse response = client.listObjects(listRequest);
		
		LOGGER.info("AmazonS3Util | listFolder | response : " + response.toString());

		List<S3Object> contents = response.contents();
		
		LOGGER.info("AmazonS3Util | listFolder | contents : " + contents.toString());

		ListIterator<S3Object> listIterator = contents.listIterator();
		
		LOGGER.info("AmazonS3Util | listFolder | listIterator : " + listIterator.toString());
		
		List<String> listKeys = new ArrayList<>();

		while (listIterator.hasNext()) {
			S3Object object = listIterator.next();
			listKeys.add(object.key());
		}
		
		LOGGER.info("AmazonS3Util | listFolder | listKeys : " + listKeys.toString());

		return listKeys;
		
	}
	
	public static void uploadFile(String folderName, String fileName, InputStream inputStream) {
		
		LOGGER.info("AmazonS3Util | uploadFile is started");
		
		S3Client client = createAmazomS3();
		
		LOGGER.info("AmazonS3Util | uploadFile | client : " + client.toString());
		
		PutObjectRequest request = putFileInAmazonS3(folderName, fileName, inputStream);
		
		LOGGER.info("AmazonS3Util | uploadFile | request : " + request.toString());
		
		LOGGER.info("AmazonS3Util | uploadFile | inputStream : " + inputStream.toString());
		
		try (inputStream) {
			
			int contentLength = inputStream.available();
			
			LOGGER.info("AmazonS3Util | uploadFile | inputStream contentLength : " + contentLength);
			
			client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
			
			LOGGER.info("AmazonS3Util | uploadFile is successfully over");
		} catch (IOException ex) {
			LOGGER.error("AmazonS3Util | uploadFile | Could not upload file to Amazon S3", ex);
		}
	}
	
	public static void deleteFile(String fileName) {
		
		LOGGER.info("AmazonS3Util | deleteFile is started");
		
		LOGGER.info("AmazonS3Util | deleteFile | fileName : " + fileName);
		
		S3Client client = createAmazomS3();
		
		LOGGER.info("AmazonS3Util | deleteFile | client : " + client.toString());
		
		DeleteObjectRequest request = deleteFileFromAmazonS3(fileName);
		
		LOGGER.info("AmazonS3Util | deleteFile | request : " + request.toString());
		
		client.deleteObject(request);
		
		LOGGER.info("AmazonS3Util | deleteFile is successfully over");

	}
	
	public static void removeFolder(String folderName) {
		
		LOGGER.info("AmazonS3Util | removeFolder is started");
		
		S3Client client = createAmazomS3();
		
		LOGGER.info("AmazonS3Util | removeFolder | client : " + client.toString());
		
		ListObjectsRequest listRequest = listAllFilesInAmazonS3ForRemove(folderName);
		
		LOGGER.info("AmazonS3Util | removeFolder | listRequest : " + client.toString());
		
		ListObjectsResponse response = client.listObjects(listRequest);
		
		LOGGER.info("AmazonS3Util | removeFolder | response : " + response.toString());

		List<S3Object> contents = response.contents();
		
		LOGGER.info("AmazonS3Util | removeFolder | contents : " + contents.toString());

		ListIterator<S3Object> listIterator = contents.listIterator();
		
		LOGGER.info("AmazonS3Util | removeFolder | listIterator : " + listIterator.toString());

		while (listIterator.hasNext()) {
			
			S3Object object = listIterator.next();
			
			DeleteObjectRequest request = deleteFileFromAmazonS3(object.key());
			
			LOGGER.info("AmazonS3Util | removeFolder | request : " + request.toString());
			
			client.deleteObject(request);
			
			LOGGER.info("AmazonS3Util | removeFolder | deleteObject is successfully over");
			
			LOGGER.info("AmazonS3Util | removeFolder | Deleted : " + object.key());
			
		}
	}
}
