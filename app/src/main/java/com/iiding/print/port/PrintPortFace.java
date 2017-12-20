package com.iiding.print.port;

import com.iiding.applet.helper.LogService;
import com.iiding.print.driver.PrintConfig;
import com.iiding.print.format.face.PrintService;


public abstract class PrintPortFace {
	
	public abstract void close();
	private PrintConfig pcfg;
	
	public abstract void init() throws PrintPortException ;
	public abstract PrintService getService();
	

	public PrintPortFace(PrintConfig pcfg){
		this.pcfg=pcfg;
	}
	
	public  boolean print(String content){
		boolean b= printPort( content);
		if(!b)
		System.err.println(b);
		return b;
	}
	
	
	protected  boolean printPort(String content){
		try{
			PrintService printService =getService();
			printService.init();
			printService.print(content);
			return true;
		}catch (Exception e) {
			//e.printStackTrace();
			LogService.log("Init Failed...  Check Device Plugin "+pcfg.toString()+e.getLocalizedMessage());
			return false;
		}
	}
	
 
	

	public PrintConfig getPcfg() {
		return pcfg;
	}

	public void setPcfg(PrintConfig pcfg) {
		this.pcfg = pcfg;
	}
	
}
