package control;

import service.BaseApiService;

public class Link {

	public static String FilePHP	= "http://softchrist.com/yopem/nearby/php/";
	public static String FileImage	= "http://softchrist.com/yopem/nearby/image/";
	public static String FileUpload	= "http://softchrist.com/yopem/nearby/uploadGambar.php";
	public static String FilePHP2	= "http://softchrist.com";

	public static BaseApiService getAPIService(){
		return RetrofitClient.getClient(FilePHP).create(BaseApiService.class);
	}

	public static BaseApiService getImageService(){
		return RetrofitClient.getClient(FileImage).create(BaseApiService.class);
	}

}
