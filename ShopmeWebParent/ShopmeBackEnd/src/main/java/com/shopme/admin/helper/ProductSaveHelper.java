package com.shopme.admin.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.util.AmazonS3Util;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;

public class ProductSaveHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);

	public static void deleteExtraImagesWeredRemovedOnForm(Product product) {
		
		LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm is started");
		
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);
		
		LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm | dirPath  : " + dirPath);
		

		try {
			Files.list(dirPath).forEach(file -> {
				String filename = file.toFile().getName();

				if (!product.containsImageName(filename)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image: " + filename);

					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + filename);
					}
				}

			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	public static void  setExistingExtraImageNames(String[] imageIDs, String[] imageNames, 
			Product product) {
		
		LOGGER.info("ProductSaveHelper | setExistingExtraImageNames is started");
		
		LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm | imageIDs  : " + imageIDs.toString());
		LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm | imageNames  : " + imageNames.toString());
		
		if (imageIDs == null || imageIDs.length == 0) return;

		Set<ProductImage> images = new HashSet<>();

		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];

			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);

	}
	
	public static void  setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		
		LOGGER.info("ProductSaveHelper | setNewExtraImageNames is started");
		
		LOGGER.info("ProductSaveHelper | setNewExtraImageNames | extraImageMultiparts.length : " + extraImageMultiparts.length);
		
		if (extraImageMultiparts.length > 0) {
			
			for (MultipartFile multipartFile : extraImageMultiparts) {
				
				LOGGER.info("ProductSaveHelper | setNewExtraImageNames | !multipartFile.isEmpty() : " + !multipartFile.isEmpty());
				
				if (!multipartFile.isEmpty()) {
					
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					
					LOGGER.info("ProductSaveHelper | setNewExtraImageNames | fileName : " + fileName);
					
					
					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
					
					
				}
			}
		}
		
		LOGGER.info("ProductSaveHelper | setExtraImageNames is completed");
	}

	public static void  setMainImageName(MultipartFile mainImageMultipart, Product product) {
		
		LOGGER.info("ProductSaveHelper | setMainImageName is started");
		
		LOGGER.info("ProductSaveHelper | setMainImageName | !mainImageMultipart.isEmpty() : " + !mainImageMultipart.isEmpty());
		
		if (!mainImageMultipart.isEmpty()) {
			
			
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			
			LOGGER.info("ProductSaveHelper | setMainImageName | fileName : " + fileName);
			
			product.setMainImage(fileName);
				
		}
		
		
		LOGGER.info("ProductSaveHelper | setMainImageName is completed");
	}
	
	public static void  saveUploadedImages(MultipartFile mainImageMultipart, 
			MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
		
		LOGGER.info("ProductSaveHelper | saveUploadedImages is started");
		
		LOGGER.info("ProductSaveHelper | saveUploadedImages | !mainImageMultipart.isEmpty() : " + !mainImageMultipart.isEmpty());
		
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			
			LOGGER.info("ProductSaveHelper | saveUploadedImages | fileName : " + fileName);
			
			String uploadDir = "../product-images/" + savedProduct.getId();
			
			LOGGER.info("ProductSaveHelper | saveUploadedImages | uploadDir : " + uploadDir);

			FileUploadUtil.cleanDir(uploadDir);
			
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}
		
		LOGGER.info("ProductSaveHelper | saveUploadedImages | extraImageMultiparts.length : " + extraImageMultiparts.length);
		
		if (extraImageMultiparts.length > 0) {
			
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
			
			LOGGER.info("ProductSaveHelper | saveUploadedImages | uploadDir : " + uploadDir);

			for (MultipartFile multipartFile : extraImageMultiparts) {
				
				LOGGER.info("ProductController | saveUploadedImages | multipartFile.isEmpty() : " + multipartFile.isEmpty());
				if (multipartFile.isEmpty()) continue;

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				
				LOGGER.info("ProductSaveHelper | saveUploadedImages | fileName : " + fileName);
				
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				
			}
		}
		
		
		LOGGER.info("ProductSaveHelper | saveUploadedImages is completed");
	}
	
	public static void  setProductDetails(String[] detailIDs, String[] detailNames, 
			String[] detailValues, Product product) {
		
		LOGGER.info("ProductSaveHelper | setProductDetails is started");
		
		LOGGER.info("ProductSaveHelper | setProductDetails | detailNames : " + detailNames.toString());
		LOGGER.info("ProductSaveHelper | setProductDetails | detailNames : " + detailValues.toString());
		LOGGER.info("ProductSaveHelper | setProductDetails | product : " + product.toString());
		
		
		if (detailNames == null || detailNames.length == 0) return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);

			if (id != 0) {
				product.addDetail(id, name, value);
			} else if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
		
		LOGGER.info("ProductSaveHelper | setProductDetails | product with its detail : " + product.getDetails().toString());
		
		LOGGER.info("ProductSaveHelper | setProductDetails is completed");
	}
	
	public static void deleteExtraImagesWeredRemovedOnFormForAmazonS3(Product product) {
		
		LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnFormForAmazonS3 is started");
		
		String extraImageDir = "product-images/" + product.getId() + "/extras";
		List<String> listObjectKeys = AmazonS3Util.listFolder(extraImageDir);
		
		LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm | listObjectKeys : " + listObjectKeys.toString());
		
		for (String objectKey : listObjectKeys) {
			int lastIndexOfSlash = objectKey.lastIndexOf("/");
			String fileName = objectKey.substring(lastIndexOfSlash + 1, objectKey.length());
			
			LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm | fileName  : " + fileName);

			if (!product.containsImageName(fileName)) {
				AmazonS3Util.deleteFile(objectKey);
				LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm | Deleted extra image : " + objectKey);
			}
		}
	}
	
	
	public static void  saveUploadedImagesForAmazonS3(MultipartFile mainImageMultipart, 
			MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
		
		LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 is started");
		
		if (!mainImageMultipart.isEmpty()) {
			
			LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 | !mainImageMultipart.isEmpty()");
			
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			
			LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 | fileName : " + fileName);
			
			String uploadDir = "product-images/" + savedProduct.getId();
			
			LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 | uploadDir : " + uploadDir);

			List<String> listObjectKeys = AmazonS3Util.listFolder(uploadDir + "/");
			
			for (String objectKey : listObjectKeys) {
				
				LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 | objectKey : " + objectKey);
				
				if (!objectKey.contains("/extras/")) {
					
					AmazonS3Util.deleteFile(objectKey);
					LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 | Deleted extra image : " + objectKey);
					
				}
			}

			AmazonS3Util.uploadFile(uploadDir, fileName, mainImageMultipart.getInputStream());	
			
			LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 | uploadFile is over");
		}

		if (extraImageMultiparts.length > 0) {
			
			LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 | extraImageMultiparts.length > 0");

			String uploadDir = "product-images/" + savedProduct.getId() + "/extras";
			
			LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 | uploadDir : " + uploadDir);

			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) continue;

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				
				LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 | fileName : " + fileName);

				AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
				
				LOGGER.info("ProductSaveHelper | saveUploadedImagesForAmazonS3 | uploadFile is over");
			}
		}
	}
}
