package com.mkyong.web.service;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.mkyong.web.controller.TokenAndPath;
@Service
public class UserServiceImpl implements UserService {

	private static final int BUFFER_SIZE = 4096;

	private MakeDirectoryUtility makeDirectoryUtility;
	private String home = System.getProperty("user.home");

	private String saveDir = home;

	@Override
	public String authorizeAndGetUserToken() throws URISyntaxException  {
		// TODO Auto-generated method stub
		String url1="https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=c00a4c26-e64b-459b-91f6-31571b802ae4&scope=files.read.all&response_type=token&redirect_uri=http://localhost:8080/onedrive/redirect";


		try {
			if(Desktop.isDesktopSupported())
			{

				Desktop.getDesktop().browse(new URI(url1));
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (IOException e) {

			e.printStackTrace();

		}

		return null;
	}

	@Override
	public String finaldownload(TokenAndPath tokenAndPath) throws IOException, IllegalStateException, JsonSyntaxException, InterruptedException, NumberFormatException {
		// TODO Auto-generated method stub

		String access_token= tokenAndPath.getToken();

		String tokenheader = "Bearer"+" "+access_token;



		String commonUrl ="https://graph.microsoft.com/beta/me/drive/root:/";

		//	String base_path = "https://myoffice.accenture.com/personal/sai_kiran_akkireddy_accenture_com/Documents/testDownload";
		String base_path = tokenAndPath.getPath();//replaceAll("%20", " ");

		int indexAfterDocuments =base_path.lastIndexOf("Documents")+10;

		String child =":/children";

		String file = base_path.substring(indexAfterDocuments);

		String MakeLocalDirectory =file.replace("%20", " ");


		String completeurl= commonUrl+file+child;

		System.out.println("saiiii"+""+file);


		//making a directory
		File dir = new File(saveDir+"\\"+MakeLocalDirectory);
		dir.mkdirs();


		System.out.println(completeurl);





		//Send the response


		String responseFromAdaptor= UserServiceImpl.doGet(completeurl, tokenheader);
		final Gson gson = new Gson();
		OuterMetaData outerMetaData =gson.fromJson(responseFromAdaptor, OuterMetaData.class);
		System.out.println("json form ");
		System.out.println(outerMetaData);
		List<String> downloadUrls = new ArrayList<String>();
		for (MetaDataForFolder metaDataForFolder:outerMetaData.getValue()){

			if(metaDataForFolder.getFolder()!=null &&
					(Integer.parseInt(metaDataForFolder.getFolder().getChildCount())>=1)){
				readingInnerFolders(tokenheader, commonUrl, child, file, dir, gson, metaDataForFolder);
			}else{
				String Url = metaDataForFolder.getMicrosoft_graph_downloadUrl();
				downloadUrls.add(Url);
			}
		}


		System.out.println(downloadUrls);

		// single threaded application takes more response time		
		//		final long startTime = System.currentTimeMillis();si
		//		for (String downloadUrl:downloadUrls){
		//		System.out.println("saveDir------>"+saveDir);
		//		UserServiceImpl.downloadFile(downloadUrl, dir.getPath());
		//		
		//		}
		//		
		//		 final long endTime = System.currentTimeMillis();
		//		System.out.println("Time taken to get Response in millis:" + ( endTime - startTime ));


		// create the size of the thread pool dynamically
		ExecutorService executor = Executors.newFixedThreadPool(downloadUrls.size());
		final long startTime = System.currentTimeMillis();
		for (String downloadUrl:downloadUrls){

			System.out.println("saveDir------>"+saveDir);			
			// multithreading framework for downloading files
			Runnable download= new MultiDownLoadExecutor(downloadUrl, dir.getPath());
			executor.execute(download);
		}
		executor.shutdown();

		final long endTime = System.currentTimeMillis();
		System.out.println("Time taken to get Response in millis:" + ( endTime - startTime ));
		
		if(  (executor.awaitTermination(20, TimeUnit.SECONDS) )){
			List<File> filesInFolder = Files.walk(Paths.get(dir.getPath()))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
		for(File officefile:filesInFolder){
			officefile.getAbsolutePath();
			System.out.println("Rading file "+officefile.getName());
			officefile.getName();
			System.out.println("officefile.getAbsolutePath();"+officefile.getAbsolutePath());
			 int index = officefile.getAbsolutePath().lastIndexOf(".");
		        //print filename
		        //System.out.println(file.getName().substring(0, index));
		        //print extension
		        //System.out.println(file.getName().substring(index));
		     String textNaming=officefile.getAbsolutePath().substring(0, index);
		     textNaming.concat(".txt");
		    String txtpath=    textNaming.concat(".txt");
		        System.out.println("officefile.getAbsolutePath().substring(index)"+officefile.getAbsolutePath().substring(index));
		        PrintWriter out = new PrintWriter(new FileOutputStream(txtpath));
			if (!officefile.exists()) {
				System.out.println("Sorry does not Exists!");
			}else {
				if (officefile.getName().endsWith(".docx") || officefile.getName().endsWith(".DOCX")|| officefile.getName().endsWith("doc")) {
				out.write(new XWPFWordExtractor(new XWPFDocument(new FileInputStream(officefile))).getText());
					
				} 
				 else if (officefile.getName().endsWith(".xlsx") || officefile.getName().endsWith(".XLSX")|| officefile.getName().endsWith(".xls")) {
					 out.write(new XSSFExcelExtractor(new XSSFWorkbook(new FileInputStream(officefile))).getText());
				}
				 
				 else if (officefile.getName().endsWith(".pdf") || officefile.getName().endsWith(".PDF")) {
									        //use file.renameTo() to rename the file
				     
					
					
					PdfReader pdfReader= new PdfReader(officefile.getAbsolutePath());
					 PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
				
					    TextExtractionStrategy strategy;
					    for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
					        strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
					        System.out.println("strategy.getResultantText()"+strategy.getResultantText());
					        out.write(strategy.getResultantText());
					   
						    out.close();
						    pdfReader.close();
					    }
					    
					    
				
			
			
			}
		}
		}
		}

		return "display";
	
		
		}
	private void readingInnerFolders(String tokenheader, String commonUrl, String child, String file, File dir,
			final Gson gson, MetaDataForFolder metaDataForFolder) throws ClientProtocolException, IOException {
		String insideFoldername=	metaDataForFolder.getName();
		String localinsideFolderName=insideFoldername.replaceAll("%20", " "); 
		String OneDriveinsideFolderUrl =commonUrl+file+"/"+insideFoldername+child;

		File dir1 = new File(dir.getPath()+"\\"+localinsideFolderName);
		dir1.mkdirs();
		String responseFromAdaptor1= UserServiceImpl.doGet(OneDriveinsideFolderUrl, tokenheader);
		OuterMetaData outerMetaData1 =gson.fromJson(responseFromAdaptor1, OuterMetaData.class);
		List<String> downloadUrls1 = new ArrayList<String>();
		for (MetaDataForFolder metaDataForFolder1:outerMetaData1.getValue()){
			String Url1 = metaDataForFolder1.getMicrosoft_graph_downloadUrl();
			downloadUrls1.add(Url1);
		}
		ExecutorService executor1 = Executors.newFixedThreadPool(downloadUrls1.size());
		for (String downloadUrl1:downloadUrls1){

			System.out.println("saveDir------>"+saveDir);			
			// multithreading framework for downloading files
			Runnable download1= new MultiDownLoadExecutor(downloadUrl1, dir1.getPath());
			executor1.execute(download1);
		}
		executor1.shutdown();
	}

	public static void downloadFile(String fileURL, String saveDir)
			throws IOException {
		URL url = new URL(fileURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10,
							disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
						fileURL.length());
			}

			System.out.println("Content-Type = " + contentType);
			System.out.println("Content-Disposition = " + disposition);
			System.out.println("Content-Length = " + contentLength);
			System.out.println("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			String saveFilePath = saveDir + File.separator + fileName;

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();

			System.out.println("File downloaded");
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
	}

	public  static String doGet( final String url,String tokenheader ) throws ClientProtocolException, IOException{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		final HttpGet httpRequest = new HttpGet( url );

		httpRequest.addHeader("Content-Type", "text/plain");
		httpRequest.addHeader("Authorization", tokenheader);

		HttpResponse response = httpClient.execute(httpRequest);
		final org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) response.getEntity();
		final String responseString = EntityUtils.toString( (org.apache.http.HttpEntity) entity, "UTF-8" );
		EntityUtils.consume( entity );
		System.out.println(responseString);
		httpClient.getConnectionManager().shutdown();
		return responseString;

	}
}
