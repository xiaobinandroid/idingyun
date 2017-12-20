package com.iiding.print.to;

import com.iiding.print.cons.PrinterStatus;

import java.io.Serializable;

public class PrintTO implements Serializable,Cloneable {
 /**
	 * 
	 */
	private static final long serialVersionUID = -76646206012484460L;
private PrintTO(String shopid, String printerName, String content) {
		super();
		 this.shopid=shopid;
		this.printerName = printerName;
		this.content = content; 
		this.createTime= System.currentTimeMillis();
		this.printerStatus= PrinterStatus.live;
	}
 public long getLastSuccessTime() {
	return lastSuccessTime;
}
public long getLastFailTime() {
	return lastFailTime;
}
public long getLastTryTime() {
	return lastSuccessTime;
}
public PrintTO(String printerName, String content) {
	 this(null, printerName, content);
 }
 
 
 private String printerName;
 private String content;
 private long createTime;
 private long lastSuccessTime;
 private long lastFailTime;
 
 
 private int successTimes;
 private int failedTimes;

 
 public long getCreateTime() {
	return createTime;
}


private PrinterStatus printerStatus;
 public PrinterStatus getPrinterStatus() {
	return printerStatus;
}
public void setPrinterStatus(PrinterStatus printerStatus) {
	this.printerStatus = printerStatus;
	
	
	if(printerStatus== PrinterStatus.live){
		lastSuccessTime= System.currentTimeMillis();
		successTimes++;
		 
	}else{
		lastFailTime= System.currentTimeMillis();
		failedTimes++;
	}
}

 

private String shopid;
 
@Override
	public Object clone()   {
		 
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			 
			e.printStackTrace();
		}
		return null;
	}

public String getShopid() {
	return shopid;
}
public void setShopid(String shopid) {
	this.shopid = shopid;
}
public String getPrinterName() {
	return printerName;
}
public void setPrinterName(String printerName) {
	this.printerName = printerName;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
@Override
public String toString() {
	return "PrintTO [ createTime=" + createTime
			+ ", failedTimes=" + failedTimes + ", lastFailTime=" + lastFailTime
			+ ", lastSuccessTime=" + lastSuccessTime + ", printerName="
			+ printerName + ", printerStatus=" + printerStatus + ", shopid="
			+ shopid + ", successTimes=" + successTimes + "]";
}
public boolean isChangePrinter(int times){
	 
	
	
	// System.out.println(times+"===="+failedTimes);
	if(failedTimes !=0 && failedTimes%times==0){
 
		return true; 
	}
	return false;
}
 
}
