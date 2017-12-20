package com.iiding.barcode.to;

public class BarcodeTO {
private String barcode;
private int height;
public BarcodeTO(String barcode, int height) {
	super();
	this.barcode = barcode;
	this.height = height;
}
public String getBarcode() {
	return barcode;
}
public void setBarcode(String barcode) {
	this.barcode = barcode;
}
public int getHeight() {
	return height;
}
public void setHeight(int height) {
	this.height = height;
}
}
