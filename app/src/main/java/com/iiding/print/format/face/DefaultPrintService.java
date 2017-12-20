package com.iiding.print.format.face;

import java.io.IOException;
import java.io.OutputStream;

public class DefaultPrintService extends AbstractPrintService implements PrintService,StandardPrintFormatFace {
	
	
	
	public DefaultPrintService(OutputStream out){
		this.out=out;
	}

    private OutputStream out;
    
	@Override
	public void write(byte[] b) throws IOException {
		 

 
			out.write(b);
	
	}

 
}
