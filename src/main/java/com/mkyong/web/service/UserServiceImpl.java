package com.mkyong.web.service;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.apache.poi.POIXMLProperties.*;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

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

	private InnerFoldersReaderUtility innerFoldersReaderUtility;
	private String home = System.getProperty("user.home");

	private String saveDir = home;

	private WordExtractor wd;

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



		String commonUrl ="https://graph.microsoft.com/beta/me/";

		//	String base_path = "https://myoffice.accenture.com/personal/sai_kiran_akkireddy_accenture_com/Documents/testDownload";
		String base_path = tokenAndPath.getPath();//replaceAll("%20", " ");

		// gets the start index after the documents path
		int indexAfterDocuments =base_path.lastIndexOf("Documents")+10;
		

		String file = base_path.substring(indexAfterDocuments);
		
		String child =":/children";

		String MakeLocalDirectory =file.replace("%20", " ");


		String completeurl= commonUrl+"drive/root:/"+file+child;

		System.out.println("saiiii"+""+file);


		//making a directory
		File dir = new File(saveDir+"\\"+MakeLocalDirectory);
		dir.mkdirs();


		System.out.println(completeurl);





		//make a get call to one drive api


		String responseFromAdaptor= UserServiceImpl.doGet(completeurl, tokenheader);

		final Gson gson = new Gson();

		OuterMetaData outerMetaData =gson.fromJson(responseFromAdaptor, OuterMetaData.class);

		System.out.println("json form ");
		System.out.println(outerMetaData);
		List<String> downloadUrls = new ArrayList<String>();
		for (MetaDataForFolder metaDataForFolder:outerMetaData.getValue()){

			if(metaDataForFolder.getFolder()!=null &&
					(Integer.parseInt(metaDataForFolder.getFolder().getChildCount())>=1)){
				readingInnerFolders(tokenheader, commonUrl,base_path, child, file, dir, gson, metaDataForFolder);
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
			ExecutorService converterExecutor = Executors.newFixedThreadPool(filesInFolder.size());
			for(File officefile:filesInFolder){

				//parallel conversion of all files 
				Runnable converter= new ParallelConverter(officefile, file);
				converterExecutor.execute(converter);

			}
			converterExecutor.shutdown();
		}
		
		return "display";


	}

	private void readingInnerFolders(String tokenheader, String commonUrl, String base_path,String child, String file, File dir,
			final Gson gson, MetaDataForFolder metaDataForFolder) throws ClientProtocolException, IOException, InterruptedException {


		
		
        int indexAfterDocuments =base_path.lastIndexOf("Documents")+10;
		

		String file1 = base_path.substring(indexAfterDocuments);
		
		String path= metaDataForFolder.getParentReference().getPath();
		
	
		

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
				InnerFoldersReaderUtility.processAndDownloadSubFolders(tokenheader, commonUrl,base_path, child, file1, innerdir1, gson, metaDataForFolder1);
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
//		if(  (executor1.awaitTermination(20, TimeUnit.SECONDS) )){
//			List<File> filesInFolder = Files.walk(Paths.get(innerdir1.getPath()))
//					.filter(Files::isRegularFile)
//					.map(Path::toFile)
//					.collect(Collectors.toList());
//			ExecutorService converterExecutor = Executors.newFixedThreadPool(filesInFolder.size());
//			for(File officefile:filesInFolder){
//
//				//parallel conversion of all files 
//				Runnable converter= new ParallelConverter(officefile, insideFoldername);
//				converterExecutor.execute(converter);
//
//			}
//			converterExecutor.shutdown();
//		}
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


	// single thread to convert office files to txt format
	private void convertToText(File officefile,String file) throws FileNotFoundException, IOException {
		officefile.getAbsolutePath();
		System.out.println("Rading file "+officefile.getName());
		officefile.getName();

		System.out.println("officefile.getAbsoluteFile().getParentFile()"+officefile.getAbsoluteFile().getParentFile());
		System.out.println("officefile.getAbsolutePath();"+officefile.getAbsolutePath());
		System.out.println("officefile.getAbsoluteFile().getParentFile()"+officefile.getAbsoluteFile().getParent());

		int nameIndex=officefile.getName().lastIndexOf(".");
		String textNaming1=officefile.getName().substring(0, nameIndex);
		textNaming1.concat(".txt");

		System.out.println("testing sai"+officefile.getParent()+"officefile.getPath()");
		File textdirectory= new File(officefile.getParent()+"\\"+file+" TextFolder\\");
		textdirectory.mkdir();
		int index = officefile.getAbsolutePath().lastIndexOf(".");
		String textdirectoryString =textdirectory.getPath()+"\\"+textNaming1;   

		System.out.println("officefile.getAbsolutePath().substring(index)"+officefile.getAbsolutePath().substring(index));

		final String FILENAME = textdirectoryString;
		if (!officefile.exists()) {
			System.out.println("Sorry does not Exists!");
		}
		//else
		{
			if (officefile.getName().endsWith(".docx") || officefile.getName().endsWith(".DOCX")) {
				//	FileInputStream in=new FileInputStream(officefile);

				String Content=(new XWPFWordExtractor(new XWPFDocument(new FileInputStream(officefile))).getText());


				try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {



					bw.write(Content);

					// no need to close it.
					//bw.close();

					System.out.println("Done");

				} catch (IOException e) {

					e.printStackTrace();

				}

				//	XWPFDocument doc = new XWPFDocument(in);
				//	XWPFWordExtractor ex = new XWPFWordExtractor(doc);
				//	out.write(ex.getText());
				//out.write(new XWPFWordExtractor(new XWPFDocument(new FileInputStream(officefile))).getText());

			} 


			else if (officefile.getName().endsWith(".doc") || officefile.getName().endsWith(".DOC")) {
				//	FileInputStream in=new FileInputStream(officefile);

				wd = new WordExtractor(new FileInputStream(officefile));
				String Content = wd.getText();




				try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {



					bw.write(Content);

					// no need to close it.
					//bw.close();

					System.out.println("Done");

				} catch (IOException e) {

					e.printStackTrace();

				}

				//	XWPFDocument doc = new XWPFDocument(in);
				//	XWPFWordExtractor ex = new XWPFWordExtractor(doc);
				//	out.write(ex.getText());
				//out.write(new XWPFWordExtractor(new XWPFDocument(new FileInputStream(officefile))).getText());

			} 
			else if (officefile.getName().endsWith(".xlsx") || officefile.getName().endsWith(".XLSX")) {
				String Content=new XSSFExcelExtractor(new XSSFWorkbook(new FileInputStream(officefile))).getText();
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {



					bw.write(Content);

					// no need to close it.
					//bw.close();

					System.out.println("Done");

				} catch (IOException e) {

					e.printStackTrace();

				}
			}

			else if(officefile.getName().endsWith(".xls")|| officefile.getName().endsWith(".XLS")){
				ExcelExtractor wd = new ExcelExtractor(new HSSFWorkbook(new FileInputStream(officefile)));
				String Content= wd.getText();
				wd.close();
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {



					bw.write(Content);

					// no need to close it.
					//bw.close();

					System.out.println("Done");

				} catch (IOException e) {

					e.printStackTrace();

				}
			}
			else if (officefile.getName().endsWith(".ppt") || officefile.getName().endsWith(".PPT")|| officefile.getName().endsWith(".PPTX")
					|| officefile.getName().endsWith(".pptx")){
				XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(officefile)); 

				CoreProperties props = ppt.getProperties().getCoreProperties();
				String title = props.getTitle();
				System.out.println("Title: " + title);

				for (XSLFSlide slide: ppt.getSlides()) {
					System.out.println("Starting slide...");
					XSLFShape[] shapes = slide.getShapes();

					for (XSLFShape shape: shapes) {
						if (shape instanceof XSLFTextShape) {
							XSLFTextShape textShape = (XSLFTextShape)shape;
							String Content = textShape.getText();
							System.out.println("Text: " + Content);
							try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME,true))) {



								bw.write(Content);

								// no need to close it.
								//bw.close();

								System.out.println("Done");



							} catch (IOException e) {

								e.printStackTrace();

							}

						}
					}

				}
			}


			else if (officefile.getName().endsWith(".pdf") || officefile.getName().endsWith(".PDF")) {
				//use file.renameTo() to rename the file



				PdfReader pdfReader= new PdfReader(officefile.getAbsolutePath());
				PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);

				TextExtractionStrategy strategy;

				for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
					strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
					System.out.println("strategy.getResultantText()"+strategy.getResultantText());
					String Content=(strategy.getResultantText());
					try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME,true))) {



						bw.write(Content);

						// no need to close it.
						//bw.close();

						System.out.println("Done");

					} catch (IOException e) {

						e.printStackTrace();

					}
					pdfReader.close();

				}







			}
		}
	}
}
