package com.iiding.print.type;

import java.util.regex.Matcher;

public class PrintTypeFactory implements PrintTypeFace{
	   public static  boolean  isDriverPort(String port){
	 //    return false;
	    	Matcher matcherIP = REGEX_IP.matcher(port);
	    	Matcher matcherCOM = REGEX_COM.matcher(port);
	    	Matcher matcherLPT = REGEX_LPT.matcher(port);
	    	//Matcher matcherUSB = REGEX_USB.matcher(port);
	    	if(matcherIP.matches()||matcherLPT.matches()||matcherCOM.matches()||isUsbPort(port)){
	    
	        	return false;
	    	} else{
	    		 
	    		return true;
	    		 
	    	}
//	    	 
	    	
	    }
	   public static  boolean  isUsbPort(String port){
		   String[] s=port.split("--");
		   if(s.length>=4)return true;
		   return false;
	   }
	   public static  boolean  isVirtualPort(String port){
		   Matcher matcherIP = REGEX_VIRTUAL_IP.matcher(port);
		   return matcherIP.matches(); 
	   }
//	   private static boolean isDomain(String s){
//		   char[] c =s.toCharArray();
//		   int i=0;
//		   for (char d : c) {
//			if(d=='.'){
//				i++;
//			}
//		}
//		   boolean b= (i==2);
//	 System.out.println(b);
//	  return b;
//	   }
	   
	   public static void main(String[] args) {
		 //  Pattern REGEX_LPT=Pattern.compile("^([a-zA-Z0-9]+.[a-zA-Z0-9]+.[a-zA-Z]{2,3}$)");
		  // Matcher matcherIP =REGEX_LPT .matcher("aa23c.cs");
		System.out.println(!isDriverPort("aaa..c"));
	}
}
