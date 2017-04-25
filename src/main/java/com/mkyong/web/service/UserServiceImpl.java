package com.mkyong.web.service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl implements UserService {

	@Override
	public String authorizeAndGetUserToken() throws URISyntaxException {
		// TODO Auto-generated method stub
		String url1="https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=c00a4c26-e64b-459b-91f6-31571b802ae4&scope=files.read.all&response_type=token&redirect_uri=http://localhost:8080/spring-rest1/api/users/redirect";
		
		
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

}
