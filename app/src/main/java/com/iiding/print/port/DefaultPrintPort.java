package com.iiding.print.port;

import com.iiding.print.driver.PrintConfig;
import com.iiding.print.format.face.DefaultPrintService;
import com.iiding.print.format.face.PrintService;

import java.io.OutputStream;


public abstract class DefaultPrintPort extends PrintPortFace{
	
	public abstract void close();
	private OutputStream out ;

	
	public abstract void init() throws PrintPortException ;

	public DefaultPrintPort(PrintConfig pcfg){
		super(pcfg);
	}
  
	public   PrintService getService(){
		PrintService printService = new DefaultPrintService(out);
		return printService;
	}
 

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}

 
	
}
