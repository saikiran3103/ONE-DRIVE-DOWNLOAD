package com.mkyong.web.service;

import java.io.IOException;
import java.net.URISyntaxException;

import com.google.gson.JsonSyntaxException;
import com.mkyong.web.controller.TokenAndPath;

public interface UserService {
	
	public String authorizeAndGetUserToken() throws URISyntaxException;

	public String  finaldownload(TokenAndPath tokenAndPath) throws IOException, IllegalStateException, JsonSyntaxException;
}
