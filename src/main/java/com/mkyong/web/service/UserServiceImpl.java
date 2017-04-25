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
	public List<String> finaldownload(TokenAndPath tokenAndPath) throws IOException, IllegalStateException, JsonSyntaxException {
		// TODO Auto-generated method stub
		
		

		String tokenheader = "Bearer"+" "+"eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFCbmZpRy1tQTZOVGFlN0NkV1c3UWZkVkxuNTFUbXhWUEp2eTA0bml6MTV4cnZDSi0yZ3JyMktMRkhmcWxTOEF5SHFmR1VwbTlBLVdaSHZ4YkY2LVF2QWxIT0tubEN6am8zd2VscTlsS0tFZ3lBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiYTNRTjBCWlM3czRuTi1CZHJqYkYwWV9MZE1NIiwia2lkIjoiYTNRTjBCWlM3czRuTi1CZHJqYkYwWV9MZE1NIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9lMDc5M2QzOS0wOTM5LTQ5NmQtYjEyOS0xOThlZGQ5MTZmZWIvIiwiaWF0IjoxNDkzMTU4MTE2LCJuYmYiOjE0OTMxNTgxMTYsImV4cCI6MTQ5MzE2MjAxNiwiYWNyIjoiMSIsImFpbyI6IlkyWmdZTkJhOHY1VmR2ajhTVmRaK1ZyMWoxaVYvYmxnK2tzbGJFOVYrTW4wejVrbFBjc0EiLCJhbXIiOlsid2lhIiwibWZhIl0sImFwcF9kaXNwbGF5bmFtZSI6InRlc3QgQXBwbGljYXRpb24gMiIsImFwcGlkIjoiYzAwYTRjMjYtZTY0Yi00NTliLTkxZjYtMzE1NzFiODAyYWU0IiwiYXBwaWRhY3IiOiIwIiwiZmFtaWx5X25hbWUiOiJBa2tpcmVkZHkiLCJnaXZlbl9uYW1lIjoiU2FpIEtpcmFuIiwiaXBhZGRyIjoiNjUuMjIyLjI1NS4yIiwibmFtZSI6IkFra2lyZWRkeSwgU2FpIEtpcmFuIiwib2lkIjoiMGM5ODViZDgtZmIyMS00YTcyLWFiMGYtNGMzMGEzNjJmMTc5Iiwib25wcmVtX3NpZCI6IlMtMS01LTIxLTMyOTA2ODE1Mi0xNDU0NDcxMTY1LTE0MTcwMDEzMzMtNTg1MTAzOSIsInBsYXRmIjoiMyIsInB1aWQiOiIxMDAzMDAwMEEwOTUxNzkxIiwic2NwIjoiRmlsZXMuUmVhZC5BbGwgRmlsZXMuUmVhZFdyaXRlIEZpbGVzLlJlYWRXcml0ZS5BbGwiLCJzdWIiOiJ3bGI1VEFqSlpJbURHS3g4WDlxSExUN2N5Sl9JcjlIc2dFYlhFcTlsejhVIiwidGlkIjoiZTA3OTNkMzktMDkzOS00OTZkLWIxMjktMTk4ZWRkOTE2ZmViIiwidW5pcXVlX25hbWUiOiJzYWkua2lyYW4uYWtraXJlZGR5QGFjY2VudHVyZS5jb20iLCJ1cG4iOiJzYWkua2lyYW4uYWtraXJlZGR5QGFjY2VudHVyZS5jb20iLCJ1dGkiOiJWYVM5bHdPNkNVR2FOc0N0eldvdEFBIiwidmVyIjoiMS4wIn0.Fi8iazzoNofEikQqhxGlM6j3mi_SBMr8Q_LUhAeT2PwHjGXp6uOOltzcR8-J8lYarlQuHSF4GGpvOVecwkI8AZ6fh9FncyY-4-XOnEpIyG5sXLB5lRkYF_VH6LA6OI5D1EHO20YqWzZXvcobs3D4onyg3WoAzShOTj63P_zKh1eOTh1gbencBZJSxiFgXNqxaAFklJ87kZx-O1SN8fF634wsK0PP4oSYYAkD_UAh3rPK4fK12OFyd2JQq_SNl7AhbThyeBU_iyjCbJ_ghj-hDVmT4kykCViL0Bn8G0In9RMpstV3-Y04JwUj1Jh5MQadDYmtj8dgNlIWPdHBzygKFg";
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		String commonUrl ="https://graph.microsoft.com/beta/me/drive/root:/";
		
		String base_path = "https://myoffice.accenture.com/personal/sai_kiran_akkireddy_accenture_com/Documents/testDownload";
		
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
		
		System.out.println("outerMetaData"+outerMetaData);
		
		httpClient.getConnectionManager().shutdown();
		
	
		return downloadUrls;
	}

}
