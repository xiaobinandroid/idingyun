package com.iiding.print.format.face;

import java.io.IOException;


public interface PrintService {
	void write(byte[] b) throws IOException;
	
	void init() throws IOException;
	void print(String content) throws IOException;
}
