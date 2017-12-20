package com.iiding.print.service;

public class PrinterControlFactory {
	
 
	
	private static  PrinterControlService service ;
//	private static  PrinterControlService fy;
//	
//	public static PrinterControlService getFyService(){
//		if(fy == null)fy = new PrinterControlService();
//		return fy;
//
//	}
	
	public static PrinterControlService getPrintService(){
		if(service == null)service = new PrinterControlService();
		return service;
 
		
	}
}
