package com.mkyong.web.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.JsonSyntaxException;
import com.mkyong.web.service.UserService;

@Controller
public class HelloController {
	
	private UserService service;

	public HelloController (UserService service) {
		this.service = service;
	}

	@RequestMapping(value = "/model", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {

		model.addAttribute("message", "Spring 3 MVC Hello World");
		return "model";

	}

	@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name) {

		ModelAndView model = new ModelAndView();
		model.setViewName("model");
		model.addObject("msg", name);
		TokenAndPath tokenAndPath = new TokenAndPath();
		tokenAndPath.setPath("/sai/path");
		tokenAndPath.setToken("12345token");
		model.addObject("token", tokenAndPath);
		
		return model;
		
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String connect(ModelMap model) {

		
		return "hello";

	}
		@RequestMapping(value = "/token", method = RequestMethod.GET)
		public String  authorizeAndGetUserToken() throws URISyntaxException {
			return service.authorizeAndGetUserToken();
		}

		@RequestMapping(value="/redirect",method = RequestMethod.GET )
		public String  readToken( @RequestParam(value = "code", required = false) String code, HttpServletRequest request) throws URISyntaxException {
//			System.out.println(request.get;
//			String path =request.getPathInfo();
			System.out.println(request.getParameter("param1"));
//			HttpSession session = request.getSession();
//			session.setAttribute("token", request.getParameter("param1"));
//			System.out.println("saiiiiii"+"   "+path);
			
//			request.getParameterMap();
			
			return "test";
		}

		@RequestMapping(method = RequestMethod.POST, value="download")
	    public String   finaldownload(TokenAndPath tokenAndPath ) throws URISyntaxException, IOException, JsonSyntaxException, IllegalStateException, InterruptedException, NumberFormatException, OpenXML4JException, XmlException {
			return service.finaldownload(tokenAndPath);
		}
		
		@RequestMapping(method = RequestMethod.POST, value="path1")
	    public String getTokenAndPath(HttpServletRequest request ) throws URISyntaxException, IOException, JsonSyntaxException, IllegalStateException, InterruptedException, NumberFormatException, OpenXML4JException, XmlException {
//			System.out.println(request.getParameter("param1"));
			System.out.println(request.getParameter("param2"));
			HttpSession session = request.getSession();
			System.out.println(session.getAttribute("token"));
			TokenAndPath tokenAndPath=new TokenAndPath();
			tokenAndPath.setToken((String)session.getAttribute("token"));
			tokenAndPath.setPath(request.getParameter("param2"));
			return service.finaldownload(tokenAndPath);
			
		}
		
		@RequestMapping(method = RequestMethod.POST, value="path")
	    public String getTokenAndPath1(HttpServletRequest request ) throws URISyntaxException, IOException, JsonSyntaxException, IllegalStateException, InterruptedException, NumberFormatException, OpenXML4JException, XmlException {
			HttpSession session = request.getSession();
			session.setAttribute("token", request.getParameter("param1"));
			System.out.println(request.getParameter("param1"));
			System.out.println(session.getAttribute("token"));
			return "test1";
			//return "displayPath";
		}
		
}