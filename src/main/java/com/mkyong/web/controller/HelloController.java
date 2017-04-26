package com.mkyong.web.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.mkyong.web.service.UserService;

@Controller
public class HelloController {
	
	private UserService service;

	public HelloController (UserService service) {
		this.service = service;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {

		model.addAttribute("message", "Spring 3 MVC Hello World");
		return "hello";

	}

	@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name) {

		ModelAndView model = new ModelAndView();
		model.setViewName("hello");
		model.addObject("msg", name);

		return model;
		
	}
		@RequestMapping(value = "/token", method = RequestMethod.GET)
		public String  authorizeAndGetUserToken() throws URISyntaxException {
			return service.authorizeAndGetUserToken();
		}

		@RequestMapping(value="/redirect",method = RequestMethod.GET )
		public String  readToken( @RequestParam(value = "code", required = false) String code, HttpServletRequest request) throws URISyntaxException {
//			System.out.println(request.get;
			String path =request.getPathInfo();
			
			System.out.println("saiiiiii"+"   "+path);
			
//			request.getParameterMap();
			
			return "test";
		}

		@RequestMapping(method = RequestMethod.POST, value="download")
	    public String   finaldownload(TokenAndPath tokenAndPath ) throws URISyntaxException, IOException {
			return service.finaldownload(tokenAndPath);
		}
		
		@RequestMapping(method = RequestMethod.POST, value="path")
	    public String getTokenAndPath(HttpServletRequest request ) throws URISyntaxException, IOException {
			System.out.println(request.getParameter("param1"));
			System.out.println(request.getParameter("param2"));
			TokenAndPath tokenAndPath=new TokenAndPath();
			tokenAndPath.setToken(request.getParameter("param1"));
			tokenAndPath.setPath(request.getParameter("param2"));
			return service.finaldownload(tokenAndPath);
			//return "displayPath";
		}
		
}