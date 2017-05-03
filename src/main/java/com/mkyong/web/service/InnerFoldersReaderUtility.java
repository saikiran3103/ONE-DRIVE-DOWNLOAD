package com.mkyong.web.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class InnerFoldersReaderUtility {

	
	


public static void processAndDownloadSubFolders(String tokenheader, String commonUrl,String base_path, String child, String file, File dir,
		final Gson gson, MetaDataForFolder metaDataForFolder) throws ClientProtocolException, IOException, InterruptedException {

	
	String path= metaDataForFolder.getParentReference().getPath();
	
	
	 int indexAfterDocuments =base_path.lastIndexOf("Documents")+10;
		

		String file1 = base_path.substring(indexAfterDocuments);
		
	// get the name of inside folder
	String insideFoldername=	metaDataForFolder.getName();

	// form the url to get the children files for inside folder

	String OneDriveinsideFolderUrl =commonUrl+path+"/"+insideFoldername+child;


	// make a local directory with the folder structure

	String localinsideFolderName=insideFoldername.replaceAll("%20", " ");  
	File innerdir1 = new File(dir.getPath()+"\\"+localinsideFolderName);
	innerdir1.mkdirs();

	// make a call 
	String responseFromAdaptor1= UserServiceImpl.doGet(OneDriveinsideFolderUrl, tokenheader);

	OuterMetaData outerMetaData1 =gson.fromJson(responseFromAdaptor1, OuterMetaData.class);

	List<String> downloadUrls1 = new ArrayList<String>();

	for (MetaDataForFolder metaDataForFolder1:outerMetaData1.getValue()){
	if(metaDataForFolder1.getFolder()!=null &&
			(Integer.parseInt(metaDataForFolder1.getFolder().getChildCount())>=1)){
		InnerFoldersReaderUtility.processAndDownloadSubFolders(tokenheader, commonUrl,base_path, child, insideFoldername, innerdir1, gson, metaDataForFolder1);
	}
	else{
			String Url1 = metaDataForFolder1.getMicrosoft_graph_downloadUrl();
			downloadUrls1.add(Url1);
	}
	}
	ExecutorService executor1 = Executors.newFixedThreadPool(downloadUrls1.size());
	for (String downloadUrl1:downloadUrls1){

				
		// multithreading framework for downloading files
		Runnable download1= new MultiDownLoadExecutor(downloadUrl1, innerdir1.getPath());
		executor1.execute(download1);
	}
	executor1.shutdown();

}
}
