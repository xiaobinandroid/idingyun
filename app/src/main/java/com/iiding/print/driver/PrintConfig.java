package com.iiding.print.driver;

public class PrintConfig {

private String ip;
private String connectPort;
private int port;
private PortType portType;
 
public PortType getPortType() {
	return portType;
}
public void setPortType(PortType portType) {
	this.portType = portType;
}
public String getIp() {
	return ip;
}
public void setIp(String ip) {
	this.ip = ip;
}
public String getConnectPort() {
	return connectPort;
}
public void setConnectPort(String connectPort) {
	this.connectPort = connectPort;
}
public int getPort() {
	return port;
}
public void setPort(int port) {
	this.port = port;
}
@Override
public String toString() {
	return "PrintConfig [connectPort=" + connectPort + ", ip=" + ip + ", port="
			+ port + ", portType=" + portType + "]";
}


}
