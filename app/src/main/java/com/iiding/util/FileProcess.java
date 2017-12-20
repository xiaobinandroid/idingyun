package com.iiding.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileProcess {
	  public static String readFile(String filePath)  {
		    return readFile(filePath, "\r");
		  }
	  public static String readFile(String filePath, String sep)   {
		    String line = "";
		    StringBuffer fileContent = new StringBuffer(300);
		    BufferedReader br = null;
		    try {
		      File f = new File(filePath);
		      if(!f.exists())f.createNewFile();
		      FileReader in = new FileReader(f);
		      br = new BufferedReader(in);
		      while ( (line = br.readLine()) != null) {
		        fileContent.append(line).append(sep);
		      }
		    }catch (Exception e) {
				e.printStackTrace();
			}
		    finally {
		      if (br != null) {
		        try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      }
		    }
		    return fileContent.toString();
		  }
		public static void appendFile(String filePath, String fileContent)  
		 {
			String s=readFile(filePath,"");
		
			fileContent=s+fileContent;
			saveFile(filePath, fileContent);
		}
		
		public static void saveFile(String filePath, String fileContent)  
		 {
			
		
			FileOutputStream fouts = null;
			try {
				 
				 
				fouts = new FileOutputStream(filePath);
				fouts.write(fileContent.getBytes("UTF-8"));
			}catch (FileNotFoundException e) {

				e.printStackTrace();
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fouts != null)
					try {
						fouts.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}

				 }
		
}
