package com.mkyong.web.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ParallelConverter implements Runnable {


	private File officefile;

	private String originalFolderName;

	public ParallelConverter(File officefile, String originalFolderName){
		this.officefile=officefile;
		this.originalFolderName=originalFolderName;
	}

	@Override
	public void run()  {

		ParallelConverter.officeFilesConverter(officefile,  originalFolderName);
	}

	private static void officeFilesConverter(File officefile, String originalFolderName) {
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
		
		 File f = new File(officefile.getPath());
		    String path = f.getParent();
		    String textFolderName =path.substring(path.lastIndexOf("\\")+1,path.length()); 
		    
		File textdirectory= new File(officefile.getParent()+"\\"+textFolderName+" TextFolder\\");
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

				String Content;
				try {
					Content = (new XWPFWordExtractor(new XWPFDocument(new FileInputStream(officefile))).getText());



					try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {



						bw.write(Content);

						// no need to close it.
						//bw.close();

						System.out.println("Done");

					} catch (IOException e) {

						e.printStackTrace();

					}
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//	XWPFDocument doc = new XWPFDocument(in);
				//	XWPFWordExtractor ex = new XWPFWordExtractor(doc);
				//	out.write(ex.getText());
				//out.write(new XWPFWordExtractor(new XWPFDocument(new FileInputStream(officefile))).getText());

			} 


			else if (officefile.getName().endsWith(".doc") || officefile.getName().endsWith(".DOC")) {
				//	FileInputStream in=new FileInputStream(officefile);
				try{	
					WordExtractor wd;

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
				catch (IOException e) {

					e.printStackTrace();

				}
			} 
			else if (officefile.getName().endsWith(".xlsx") || officefile.getName().endsWith(".XLSX")) {
				String Content;
				try {
					Content = new XSSFExcelExtractor(new XSSFWorkbook(new FileInputStream(officefile))).getText();

					try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {



						bw.write(Content);

						// no need to close it.
						//bw.close();

						System.out.println("Done");

					} catch (IOException e) {

						e.printStackTrace();

					}
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			else if(officefile.getName().endsWith(".xls")|| officefile.getName().endsWith(".XLS")){
				ExcelExtractor wd;
				try {
					wd = new ExcelExtractor(new HSSFWorkbook(new FileInputStream(officefile)));

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
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if (officefile.getName().endsWith(".ppt") || officefile.getName().endsWith(".PPT")|| officefile.getName().endsWith(".PPTX")
					|| officefile.getName().endsWith(".pptx")){
				XMLSlideShow ppt;
				try {
					ppt = new XMLSlideShow(new FileInputStream(officefile));


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
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}


			else if (officefile.getName().endsWith(".pdf") || officefile.getName().endsWith(".PDF")) {
				//use file.renameTo() to rename the file



				PdfReader pdfReader;
				try {
					pdfReader = new PdfReader(officefile.getAbsolutePath());

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





				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		

			}
		}
	}


}
