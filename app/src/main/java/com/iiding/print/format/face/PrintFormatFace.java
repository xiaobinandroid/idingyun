package com.iiding.print.format.face;

public interface PrintFormatFace {
	String SPACE=" ";//
	String FULL_SPACE="　";//全角空格
	String SEP=";;;;;";
	String SEP2=",,,,,";

	
	String RIGHT="<right>";
	String LEFT="<left>";
	String CENTER="<center>";
	String VERTICAL_DOUBLE="<vertical_double>";
	String VD="<vd>";
	
	String DOUBLE="<double>";
	String D="<d>";
	String D2="<d2>";
	String O="<o>";
	String RED="<red>";
	String SP="<sp>";
	String BR="<br>";
//	String BACK="<back>";
	
//	String _RIGHT="</right>";
//	String _LEFT="</left>";
//	String _CENTER="</center>";
//	String _VERTICAL_DOUBLE="</vertical_double>";
//	String _VD="</vd>";
//	String _DOUBLE="</double>";
//	String _D="</d>";
//	String _SP="</sp>";
//	String _BR="</br>";
//	String VD_="<vd_>";//<vd>字体但是不被<f_>使用
	String BR_="<br_>";//换行并继续使用前面的字体
	String USE_PRE_FONT_="<f_>";//使用之前的字体大小
	String OPEN="<open>";

	String FULL_CUT="<full_cut>";
	String FC="<fc>";
	String PARTIAL_CUT="<partial_cut>";
	String PC="<pc>";
	String BARCODE="<barcode>";
	String BARCODE2="<barcode2>";
	String _BARCODE="</barcode>";
	String BOLD="<b>";
	
	// 49单价 50总计 51收款 52找零 
	String CD_UNIT_PRICE="<cd_unit_price>";
	String CD_TOTAL="<cd_total>";
	String CD_CHARGE="<cd_charge>";
	String CD_CHANGE="<cd_change>";

	//String BARCODE_DOUBLE="<bh2>";
	
}
