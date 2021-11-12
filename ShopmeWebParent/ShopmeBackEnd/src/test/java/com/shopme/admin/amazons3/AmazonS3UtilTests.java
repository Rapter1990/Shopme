package com.shopme.admin.amazons3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.shopme.admin.util.AmazonS3Util;

public class AmazonS3UtilTests {

	@Test
	public void testListFolder() {
		String folderName = "test-upload";
		List<String> listKeys = AmazonS3Util.listFolder(folderName);
		listKeys.forEach(System.out::println);
	}

	@Test
	public void testUploadFile() throws FileNotFoundException {
		String folderName = "test-upload";
		String fileName = "tokyo_2020.PNG";
		String filePath = "C:\\Users\\Noyan\\Pictures\\" + fileName;
		
		InputStream inputStream = new FileInputStream(filePath);

		AmazonS3Util.uploadFile(folderName, fileName, inputStream);
	}

	@Test
	public void testDeleteFile() {
		String fileName = "test-upload/tokyo_2020.PNG";
		AmazonS3Util.deleteFile(fileName);
	}

	@Test
	public void testRemoveFolder() {
		String folderName = "test-upload";
		AmazonS3Util.removeFolder(folderName);
	}
}
