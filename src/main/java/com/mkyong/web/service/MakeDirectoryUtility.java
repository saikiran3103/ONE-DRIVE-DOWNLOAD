package com.mkyong.web.service;

import java.io.File;

public class MakeDirectoryUtility {
	
	private MakeDirectoryUtility(){
		
	}
	
	
	public String makeDirectory( String directoryPath ){
	File dir = new File(directoryPath);
	dir.mkdirs();
	
	System.out.println(directoryPath+dir.getPath());
	return dir.getPath();
	}
}
