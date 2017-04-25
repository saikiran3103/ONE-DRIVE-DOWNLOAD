package com.mkyong.web.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.google.gson.JsonSyntaxException;
import com.mkyong.web.controller.TokenAndPath;

public interface UserService {
	
	public String authorizeAndGetUserToken() throws URISyntaxException;

	public List<String>  finaldownload(TokenAndPath tokenAndPath) throws IOException, IllegalStateException, JsonSyntaxException;
}
