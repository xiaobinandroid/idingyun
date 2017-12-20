package com.iiding.print.service;

import com.iiding.print.cons.PrinterStatus;
import com.iiding.print.format.face.PrintFormatFactory;
import com.iiding.print.to.PrintTO;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

public class PrintPoolManager {
// private static int timeoutMinutes;
 
 private static int interval=60*1000*1;
 private static long lastTime= System.currentTimeMillis();
 
 private static boolean runing=false;
 


private static final int FAILED_TIMES_TO_BAK_PRINTER=5;
 
 /**
  * 1、考虑  所有订单信息获取从这里打印
  * 2、增加指令集控制客户端行为
  */
 private static void sendStatus(){
	 if(runing){
		 System.out.println("runing> return");
		 return;
	 }
	 runing=true;
	//  System.out.println(">>>>111>>>>");
	 long now= System.currentTimeMillis();
	 if(now-lastTime>interval){
		 
		// PrinterControlFactory.getPrintService().print();
		 
		 try{
			 
			 lastTime=now;
	//		String s= PrintFormatFactory.tryCreateImage();
			
	
	//		 
	//		//System.out.println(param);
	// 
//			AppletService.init();
//			if(!AppletService.isMonitor()){
//	//			 String param="cmd=log_status&userid="+AppletService.getUserid()
//	//					 +"&domain="   +  AppletHelper.getUrl()+"&msg="+s+"&printTOList="+ 
//	//					 JsonUtil.listToJson(PrinterControlFactory.getPrintService().getPrintTO());
//	//			 String cmd = HttpGet.getHTML("http://b.iidingyun.com/status.vm?"+param, AppletHelper.getCookie(),50);
//	// 
//	//			 if(cmd.indexOf("print")!=-1){
//	//				 System.out.println(cmd);
//	//			 }
//			}else{
//				String cmd = DeviceManager.commitDeviceStatus();
//				//System.out.println(">>>"+cmd);
//			}
			//发命令控制客户端
			
		 }catch (Exception e) {
			System.out.println("net err!");
		}
		
	 }
	 runing=false;
 }
 
 
	  static  class HeartBeatTask extends TimerTask {
 
		  public void run() {
			 //  System.out.println("runing1>>>>>>>>>>>>>>");
			 
			  //synchronized (list) {
				 // System.out.println(">size>>"+list.size());
//				  BarcodeReadFactory barcodeRead=BarcodeFactory.getBarcodeRead();
//				  if(barcodeRead!=null)
//					  barcodeRead.getBarcodeService().checkCommit();
			  List<PrintTO> list = PrinterControlFactory.getPrintService().getPrintTO(PrinterStatus.dead);
			  Iterator<PrintTO> it = list.listIterator();
			  
	 
			  while (it.hasNext()) {
			
				  
				PrintTO to = (PrintTO) it.next();
 
				// System.out.println("Try to print -2----..."+to.getPrinterName()+" > ");
					boolean b=  PrintManager.printInternal(to.getPrinterName()	, to.getContent() );

					
					//System.out.println("Try to print ----3-..."+to.getPrinterName()+" > "+b);
					if(b){
					//System.out.println("PrintMonitor >>>> "+to.g);
						it.remove();
						
					}else{
						 
						 
						//System.out.println("Try to print aaaa..."+to.getPrinterName()+" > "+b);
						 
//							if(to.isChangePrinter(FAILED_TIMES_TO_BAK_PRINTER)){
//	 
//								 
//								String printer=getBakPrinter(to.getPrinterName());
//								if(printer!=null){ 
//									System.out.println("PrintMonitor "+to.getPrinterName()+">>>> try bak printer="+printer+">>>>> >>>>>>>");
//								    b=PrintManager.printInternal(printer, to.getContent() );
//								   if(b){
//									   it.remove();
//								   }
//								   
//								}
//							}
					}

			}
			  PrinterControlFactory.getPrintService().add(list);
			  PrinterControlFactory.getPrintService().clear();
			  for (PrintTO printTO : list) {
				 
					 PrinterControlFactory.getPrintService().addErrPrinter(printTO.getPrinterName());
					 
			}
		//	 System.out.println("========getBadPrinter====="+getBadPrinter());
				String s= PrintFormatFactory.tryCreateImage();
			    sendStatus();
			  
			 
		  } 
		  
		}
	
	
	//private  static List<PrintTO> list = Collections.synchronizedList(new ArrayList<PrintTO>());
	
	public static  void add(PrintTO to ){
		//synchronized(list){
		if(t1==null)start(10);
			//System.out.println("PrintMonitor add monitor >>>> "+to.getPrinterName());
			 PrinterControlFactory.getPrintService().add(to);
			// PrinterControlFactory.getPrintService().change(to.getPrinterName(), PrinterStatus.dead);
	 	}
//		
//	}

	public static String getBakPrinter(String printer){
//		Object o=null;
//		try{
//		   o=JSObject.getWindow(AppletService.getApplet()).eval("getBakPrinter('"+printer+"')");
//		   return (String)o;
//		}catch (Exception e) {
//			System.out.println(e.getLocalizedMessage());
//		}
		 return null;
	}
	
 

	private static java.util.Timer t1;
	/**
	 * Applet reinit must call this   method to restart a thread to restart Print Monitor.
	 * Warning: This method must new a timer thread, Don't cache the timer object, it's can not be cached.
	 * @param intervalSeconds
	 * @param timeoutMinutes
	 */
	public static void start(int intervalSeconds){
		if(t1!=null){
			return;
		}
		t1 = new java.util.Timer();
		//PrintPoolManager.timeoutMinutes=timeoutMinutes;
		 
		  HeartBeatTask tt = new HeartBeatTask();
		  t1.schedule(tt, 0, intervalSeconds*1000); 
		  
		  // System.out.println("Print Pool Start  Ok!  timeoutMinutes="+timeoutMinutes+"Minutes");
	}
	
	
	public static String getBadPrinter(){
	//	List<PrintTO> list = PrinterControlFactory.getPrintService().getPrintTO(PrinterStatus.dead);
		Set<String> list= PrinterControlFactory.getPrintService().getErrPrinter();
		if(list==null||list.size()==0)return "";
		 
		StringBuilder sb = new StringBuilder();
		for (String printer : list) {
			 
				sb.append(printer).append(",");
			 
		 
			 
		}
		
		  sb.deleteCharAt(sb.length()-1);

		  return sb.toString();
		  
		
	}
	
	
 
	
}
