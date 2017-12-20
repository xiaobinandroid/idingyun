package com.iiding.print.type;

import java.util.regex.Pattern;

public interface PrintTypeFace {
	 Pattern REGEX_COM= Pattern.compile("^(com)|(COM).*$");
	// Pattern REGEX_USB=Pattern.compile("^-.*$");
	  Pattern REGEX_LPT= Pattern.compile("^(lpt)|(LPT).*$");
	  Pattern REGEX_IP = Pattern.compile("^((\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])" +
	  		"\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}" +
	  		"|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\" +
	  		"d|25[0-5]))" +
	  		"|([a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$)$");
	  
	  Pattern REGEX_VIRTUAL_IP = Pattern.compile("^([a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$)$");
		  
	  
	
	
}
