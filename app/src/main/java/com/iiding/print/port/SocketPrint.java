package com.iiding.print.port;

import com.iiding.print.driver.PrintConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class SocketPrint extends DefaultPrintPort {

	public SocketPrint(PrintConfig port)   {
	
		super(port);
			 
	}
	private Socket client;
	public void init() throws PrintPortException {
		PrintConfig pcfg=this.getPcfg();
		client=new java.net.Socket();
		int timeout=1000;
		try {
			client.connect(new InetSocketAddress(pcfg.getIp(), pcfg.getPort()), timeout);
			
			this.setOut(client.getOutputStream());
		
		} catch (IOException e2) {
			
			//JOptionPane.showMessageDialog(null,port.getIp()+"("+port.getPrintName()+")"+"连接超时，请确认对应端口是否已连接打印机","提示信息",JOptionPane.OK_OPTION);
		 //	e2.printStackTrace();
			throw new PrintPortException(getPcfg().getIp()+" Print Failed!");
		 	//throw new PrintPortException("打印机连接失败！");
		 	//System.out.println(pcfg.getIp()+"("+pcfg.getPrintName()+")"+"连接超时，请确认对应端口是否已连接打印机，  当前超时设置"+timeout/1000+"秒");
		}
		
	}
	
	public void close(){
		try {
			if(client!=null)client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}