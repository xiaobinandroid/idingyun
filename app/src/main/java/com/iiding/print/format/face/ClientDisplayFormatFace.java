package com.iiding.print.format.face;


public interface ClientDisplayFormatFace  {
	//49单价 50总计 51收款 52找零

		public static final byte[] UNIT_PRICE = new byte[]{ 0x1B, 's',49};;
		public static final byte[] TOTAL = new byte[]{ 0x1B, 's',50};;
		public static final byte[] CHARGE = new byte[]{ 0x1B, 's',51};;
		public static final byte[] CHANGE = new byte[]{ 0x1B, 's',52};
		
		
		
		

}
