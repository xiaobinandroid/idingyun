package com.iiding.print.service;

import com.iiding.print.cons.PrinterStatus;
import com.iiding.print.format.face.PrintFormatFace;
import com.iiding.print.to.PrintTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PrinterControlService {
	
	
	
	private  final Map<String, PrintTO> pc = new ConcurrentHashMap<String, PrintTO>();
	
	private final Set<String> errPrinter =new HashSet<String>();
	
	public void addErrPrinter(String port){
		errPrinter.add(port);
	}
	public void clear(){
		errPrinter.clear();
	}
	public Set<String> getErrPrinter(){
		return errPrinter ;
	}
	
	
	public    void add(PrintTO to){
		add(to.getPrinterName(), to.getContent());
	}
	public    void add(String printer, String content){
		PrintTO c = pc.get(printer);
		if(c==null){
			c=new PrintTO(printer,content);
			pc.put(printer, c);
		}
		else{
			c.setContent(c.getContent()+ PrintFormatFace.BR+content);
		}
		
	}
	public    void add(List<PrintTO> list){
		for (PrintTO to : list) {
			add(to);
		}
		
	}
	
	
	public void change(String printer, PrinterStatus printerStatus){
		if(1==1)return ;
		PrintTO c = pc.get(printer);
		if(c!=null){
			c.setPrinterStatus(printerStatus);
			 
		}
	}
 
	public    void add(String content){
		
		content=content.replaceAll("\r", "");
		 content=content.replaceAll("\n", "");
		 synchronized(pc){
			 String[] k=content.split(PrintFormatFace.SEP);
			 for (String s : k) {
				 String[] p =s.split(PrintFormatFace.SEP2);
				 if(p.length<=1){
					 System.out.println("Format ERR £¡"+s);
					 continue;
				 }
				 p[0]=p[0].trim();
				 if(p[0].indexOf(",")!=-1){
					String[] printer=p[0].split(",");
					for (String pp : printer) {
						String p0=pp.trim();
						if(!p0.equals("")){
						 add(p0,p[1]);
						}
					}
				 }
				 else{
					 add(p[0],p[1]);
				 }
				
			}
		 }
	}
	
	public List<PrintTO> getPrintTO(PrinterStatus printerStatus){
		List<PrintTO> rlist=new ArrayList<PrintTO>();
		synchronized(pc){
			Collection<PrintTO> list=pc.values();
			for (PrintTO to : list) {
				if(to.getContent()!=null&&!to.getContent().equals(""))
				{
					//if(to.getPrinterStatus()==printerStatus){
						PrintTO n=(PrintTO)to.clone();
						to.setContent("");
						rlist.add(n);
					//}
				}
			}
			 
		}
 
		return rlist;
		
		
	}
 
	
	public List<PrintTO> getPrintTO(){
		List<PrintTO> rlist=new ArrayList<PrintTO>();
 
			Collection<PrintTO> list=pc.values();
			for (PrintTO to : list) {
			 
					PrintTO n=(PrintTO)to.clone();
					n.setContent("content-length="+n.getContent().length());
					rlist.add(n);
				 
			}
			 
 
 
		return rlist;
		
		
	}
	public String toString(){
		StringBuilder sb= new StringBuilder();
		Collection<PrintTO> c= pc.values();
		for (PrintTO to : c) {
			 sb.append(to.toString());
		}
		return sb.toString();
	}
	
	public void print(){
		Collection<PrintTO> c= pc.values();
		for (PrintTO to : c) {
			System.out.println(to);
		}
		
	}
	

}
