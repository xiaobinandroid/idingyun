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
			
			//JOptionPane.showMessageDialog(null,port.getIp()+"("+port.getPrintName()+")"+"���ӳ�ʱ����ȷ�϶�Ӧ�˿��Ƿ������Ӵ�ӡ��","��ʾ��Ϣ",JOptionPane.OK_OPTION);
		 //	e2.printStackTrace();
			throw new PrintPortException(getPcfg().getIp()+" Print Failed!");
		 	//throw new PrintPortException("��ӡ������ʧ�ܣ�");
		 	//System.out.println(pcfg.getIp()+"("+pcfg.getPrintName()+")"+"���ӳ�ʱ����ȷ�϶�Ӧ�˿��Ƿ������Ӵ�ӡ����  ��ǰ��ʱ����"+timeout/1000+"��");
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