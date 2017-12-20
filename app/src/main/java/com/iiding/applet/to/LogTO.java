package com.iiding.applet.to;

public class LogTO {
private String message;
public String getMessage() {
	return message;
}
public long getTime() {
	return time;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((message == null) ? 0 : message.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	LogTO other = (LogTO) obj;
	if (message == null) {
		if (other.message != null)
			return false;
	} else if (!message.equals(other.message))
		return false;
	return true;
}
public LogTO(String message, long time) {
	super();
	this.message = message;
	this.time = time;
}
private long time;
}
