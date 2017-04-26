package com.mkyong.web.service;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mkyong.web.controller.TokenAndPath;
@Service
public class UserServiceImpl implements UserService {

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
	public String finaldownload(TokenAndPath tokenAndPath) throws IOException, IllegalStateException, JsonSyntaxException {
		// TODO Auto-generated method stub
		
		String access_token= tokenAndPath.getToken();

		String tokenheader = "Bearer"+" "+access_token;
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		String commonUrl ="https://graph.microsoft.com/beta/me/drive/root:/";
		
	//	String base_path = "https://myoffice.accenture.com/personal/sai_kiran_akkireddy_accenture_com/Documents/testDownload";
		String base_path = tokenAndPath.getPath();
		
		int i =base_path.lastIndexOf("Documents")+10;
		String child =":/children";
		
		String file = base_path.substring(i);
		String completeurl= commonUrl+file+child;
		System.out.println("saiiii"+""+file);
		
		
		System.out.println(completeurl);
		HttpGet request = new HttpGet(completeurl);

		
		request.addHeader("Content-Type", "text/plain");
		request.addHeader("Authorization", tokenheader);
		
		
		
		HttpResponse response = httpClient.execute(request);
		final org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) response.getEntity();
		final String responseString = EntityUtils.toString( (org.apache.http.HttpEntity) entity, "UTF-8" );
		EntityUtils.consume( entity );
		System.out.println(responseString);
		final Gson gson = new Gson();
		OuterMetaData outerMetaData =gson.fromJson(responseString, OuterMetaData.class);
		System.out.println("json form ");
		System.out.println(outerMetaData);
		List<String> downloadUrls = new ArrayList<String>();
		for (MetaDataForFolder metaDataForFolder:outerMetaData.getValue()){
			String Url = metaDataForFolder.getMicrosoft_graph_downloadUrl();
			downloadUrls.add(Url);
		}
		
		System.out.println(downloadUrls);
		
		
		
		httpClient.getConnectionManager().shutdown();
		
	
		return "display";
	}

}
