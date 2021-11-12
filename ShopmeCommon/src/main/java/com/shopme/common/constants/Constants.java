package com.shopme.common.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	private static final Logger LOGGER = LoggerFactory.getLogger(Constants.class);
	
	public static final String S3_BASE_URI;

	static {
		String bucketName = System.getenv("AWS_BUCKET_NAME");
		String region = System.getenv("AWS_REGION");
		String pattern = "https://%s.s3.%s.amazonaws.com";
		
		LOGGER.info("ShopmeCommon | Constants | bucketName : " + bucketName);
		LOGGER.info("ShopmeCommon | Constants | region : " + region);
		
		S3_BASE_URI = bucketName == null ? "" : String.format(pattern, bucketName, region);
		
		LOGGER.info("ShopmeCommon | S3_BASE_URI | S3_BASE_URI : " + S3_BASE_URI);
	}
	
}
