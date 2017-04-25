package com.mkyong.web.service;

import java.net.URISyntaxException;

public interface UserService {
	
	public String authorizeAndGetUserToken() throws URISyntaxException;

}
