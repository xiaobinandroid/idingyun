package com.iiding.print.service;

import com.iiding.print.cons.PrinterStatus;
import com.iiding.print.driver.PrintFactory;
import com.iiding.print.format.face.PrintFormatFactory;
import com.iiding.print.port.PrintPortException;
import com.iiding.print.port.PrintPortFace;
import com.iiding.print.to.PrintTO;
import com.iiding.print.type.PrintTypeFactory;

import java.util.List;

public class PrintManager {


	 private  static boolean printByPort(String port, String content) {
		 	PrintPortFace p= PrintFactory.getPrintPort(port);
PrinterStatus t;
				try{
					/**
					 * This line is impossible, because of print by driver.
					 */
					if(p==null){
						System.out.println("Print port name is illegal!");
						return false;
						
					}
					 p.init();
					if(p!=null  ){
						 
						boolean b=	p.print(content);
					
						return b;
					} else{
						System.out.println("We can not find the device, Print port name config err!");
						return false;
					} 
				}catch (PrintPortException e) {
					//e.printStackTrace();
					//LogService.log(e.getLocalizedMessage());
					 return false;
				}finally{
					p.close();
				}
			 
	     	
 
		}
	 
	 
	 
	 
	  static boolean  printInternal(String port, String content){
		  PrintFormatFactory.init(content);
	    	//System.setSecurityManager(null);
		 boolean b;
	    	try{
	    		boolean isDriver= PrintTypeFactory.isDriverPort(port);
	    		//System.out.println("isDriver="+isDriver);
		    	if(isDriver){
		    		
		    		//b= printByDriver(port, content);

		    		b=false;
		    		
		    	} else{
		    		 
		    		 b= printByPort(port, content);
		    		 
		    	}
	    	}catch (Throwable e) {
	    		// LogService.log(e.getLocalizedMessage());
				e.printStackTrace();
				return false;
			}
//	    	if(b){
//	    		 PrinterControlFactory.getPrintService().change(port, PrinterStatus.live);
//	    	}else{
//	    		 PrinterControlFactory.getPrintService().change(port, PrinterStatus.dead);
//	    	}
	    	return b;
	    }

		 public static boolean  print(PrintTO to){
			 boolean b=printInternal(to.getPrinterName(), to.getContent());
			 return b;
		 }
	 public static boolean  printWithMonitor(PrintTO to){
		 
 
		 String content=extractPrinter(to.getContent());
		
		 to.setContent(content);

		 boolean b=false;
		 try{
				   b = doPrintWithMonitor(to.getPrinterName(),to.getContent());
		 
		 }catch (Exception e) {
			// LogService.log(e.getLocalizedMessage());
			e.printStackTrace();
		}
		 return b;
	 }
	private static boolean doPrintWithMonitor(String printer, String content) {
		
	 	if(printer==null||printer.equals("")){
    		System.out.println("No Print Port ！");
    		return false;
    	}
    	if(content==null||content.trim().length()<=1){
    		System.out.println("No Print Content ！");
    		return false;
    	}
		
		
		
		boolean b;
		b=printInternal(printer,content);
		 if(!b){
				 PrintTO p=new PrintTO(printer,content);
				 PrintPoolManager.add(p);
			}
		return b;
	}
	 public static void PRINT_FORMAT(String content) {
		 PrinterControlFactory.getPrintService().add(content);
		 List<PrintTO> list= PrinterControlFactory.getPrintService().getPrintTO( PrinterStatus.live);
		 for (PrintTO to : list) {
			 PRINT_PORT(to.getPrinterName(), to.getContent());
		}
	}
	   public static boolean  PRINT_PORTS(String port, String content){
		   
		   if(port.indexOf(",")==-1){
			   PRINT_PORT(port, content);
			   return true;
			   
		   }
		   
		   	 String[] ports=port.split(",");
		   
			 for (String portName : ports) {
				 PRINT_PORT(portName, content);
			}
	    	 
			 return true;
	    }
	
	   
	   public static boolean  PRINT_PORT(String tag, String port, String content){
		   
		   PrintFormatFactory.log("log={tag='"+tag+"'}");
		   
		   
		   return PRINT_PORT(port, content);
	   }
    public static boolean  PRINT_PORT(String port, String content){
   	 
   
    	
    	//System.out.println(">>>>>>>>>>>>>"+port);
    	final PrintTO to = new PrintTO(port, content);
    	
    	//启动线程防止打印机故障导致的卡顿
		  new Thread(){
			  @Override
			public void run() {
				  printWithMonitor(to);

		
			  } }.start();
			  return true;
    	
    }
	 
	 
	 
	 /**
	  * <ADD_PRINTER=192.168.1.168,192.168.2.110>test</ADD_PRINTER>

	  * @param content
	  * @return
	  */
	 private static String ADD_PRINTER="<ADD_PRINTER=";
	 private static String END_ADD_PRINTER="</ADD_PRINTER>";
	 private static String getAddPrinter(String content){
		 if(content.indexOf(ADD_PRINTER)!=-1)return ADD_PRINTER;
		 if(content.indexOf(ADD_PRINTER.toLowerCase())!=-1)return ADD_PRINTER.toLowerCase();
		 return null;
	 }
	 private static String extractPrinter(String content){
		 
		 
		 String printer;
		 String addTag=getAddPrinter(content);
		
		 if(addTag!=null){
			 String addContent=null;
			 content=content.trim().replaceFirst(addTag, "");
			 printer=content.substring(0,content.indexOf(">"));
			 content=content.substring(content.indexOf(">")+1);
			 
			 if(content.indexOf(END_ADD_PRINTER)!=-1){
				 
				 addContent=content.substring(0,content.indexOf(END_ADD_PRINTER));
				  
				 content=content.substring(content.indexOf(END_ADD_PRINTER));
			 }
			 String[] p= printer.split(",");
			 if(addContent!=null&&!addContent.equals("")){
				 for (String s : p) {
					 
					doPrintWithMonitor(s, addContent);
				}
			 }
			 System.out.println("addPrinter="+printer);
		 }
		 else{
			 return content;
		 }
		 

		 
		 
		 
		 return content;
		 
		 
		 
	 }
	 
	 
	 
	 
	 
//	 public static boolean  printWithMonitor(String shopid,String port,String content){
//		 boolean b=printInternal(port, content);
//		 if(!b){
//				PrintPoolManager.add(new WebPrintTO(shopid,port,content));
//			}
//		 return b;
//	 }
	 
//	 public static boolean netPrint(String port, String content) {
//		int MAXTRY=1;
//		int redo = 1;     //重试次数 
//		while(redo<=MAXTRY){
//			try{
//				PrintPort p=PrintFactory.getPrintPort(port,"");
//				 p.init();
//				if(p!=null && p.getOut()!=null){
//					p.print(content);
//					p.close();
//					return true;
//				}  
//			}catch (PrintPortException e) {
//				 redo++;  
//				System.out.println(e.getLocalizedMessage());
//				 continue;
//			}
//		}
//		
//     	
//     	return false;
//	}
	
	
	
	
	
	

}
