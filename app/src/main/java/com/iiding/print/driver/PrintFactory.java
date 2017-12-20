package com.iiding.print.driver;

import com.iiding.print.port.PrintPortFace;
import com.iiding.print.port.SocketPrint;
import com.iiding.print.type.PrintTypeFace;

import java.util.regex.Matcher;


public class PrintFactory implements PrintTypeFace {
	

	public static PrintPortFace getPrintPort(String port)  {
		PrintConfig	pcfg=getPortInfo(port);
		if(pcfg!=null){
		  if(pcfg.getPortType()==PortType.IP){
				return new SocketPrint(pcfg);
			} 
		}
		return null;
	}

	
	
	private static PrintConfig getPortInfo(String port){
		//System.out.println( ">>port>>>>>"+port);
		//Matcher matcherIP = REGEX_IP.matcher(port);
    	Matcher matcherCOM = REGEX_COM.matcher(port);
    	Matcher matcherLPT = REGEX_LPT.matcher(port);
    	PrintConfig pcfg = new PrintConfig();
    	if(matcherCOM.matches()){
    		pcfg.setConnectPort(port);
    		
    		pcfg.setPortType(PortType.COM);
    	}else if(matcherLPT.matches()){
    		pcfg.setConnectPort(port);
    		pcfg.setPortType(PortType.LPT);
    	}else{
    		//JOptionPane.showMessageDialog(null,"打印机端口（com，lpt或ip地址）输入有误！","ERROR",JOptionPane.OK_OPTION); 
    		//System.out.println(port+", Not Exist!");
    		//return null;
    		
    		String ip;
    		int pt;
    		if(port.indexOf(":")==-1){
    			ip=port;
    			pt=9100;
    		}else{
    			String[] p=port.split(":");
    			ip=p[0];
    			pt= Integer.valueOf(p[1]);
    		}
    		
    		
    		 //System.out.println(ip+">>>>>>>"+pt);
    		pcfg.setIp(ip);
    		pcfg.setPort(pt);
    	
    		pcfg.setPortType(PortType.IP);
    		
//    		
//    		pcfg.setConnectPort(port);
//        	
//    		pcfg.setPortType(PortType.USB);
    	}
		return pcfg;
	}
}
