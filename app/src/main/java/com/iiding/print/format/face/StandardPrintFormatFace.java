package com.iiding.print.format.face;


public interface StandardPrintFormatFace  {
 
		public static final byte HT = 0x9;
		public static final byte LF = 0x0A;
		public static final byte CR = 0x0D;
		public static final byte ESC = 0x1B;
		public static final byte DLE = 0x10;
		public static final byte GS = 0x1D;
		public static final byte FS = 0x1C;
		public static final byte STX = 0x02;
		public static final byte US = 0x1F;
		public static final byte CAN = 0x18;
		public static final byte CLR = 0x0C;
		public static final byte EOT = 0x04;
		
		//public static final byte[] ESC_LF = new byte[] { ESC, 'e',2};
		/* 初始化打印机 */
		public static final byte[] ESC_INIT = new byte[] { ESC, '@' };
		/* 设置标准模式 */
		public static final byte[] ESC_STANDARD = new byte[] { ESC, 'S' };
		/* 设置汉字打印模式 */
		public static final byte[] ESC_CN_FONT = new byte[] { FS, '&' };
		/* 选择字符集 */
		public static final byte[] ESC_SELECT_CHARACTER = new byte[] { ESC, 'R', 9 };
		/* 设置用户自定义汉字字体 焗7118 */
		public static final byte[] ESC_FS_2 = new byte[] { FS, 0x32, 0x71, 0x18 };
		/* 取消用户自定义字体 */
		//public static final byte[] ESC_CANCEL_DEFINE_FONT = new byte[] { ESC, '%',0 };
		public static final byte[] ESC_CANCEL_DEFINE_FONT = new byte[] { ESC,  0 };
		/* 打开钱箱指令 */
		public static final byte[] ESC_OPEN_DRAWER = new byte[] { ESC, 'p', 0x00,
				0x10, (byte) 0xff };
		/* 切纸指令GS V m   
		 * m  0,48 Executes a full cut(cuts the paper completely)
		 *    1,49 Excutes a partilal cut(one point left uncut)
		 */
		public static final byte[] POS_CUT_MODE_FULL = new byte[] { GS, 'V', 0x00 };
		public static final byte[] POS_CUT_MODE_PARTIAL = new byte[] { GS, 'V',0x01 };
		/* 西文字符 （半宽）字体A (6 ×12)，汉字字符 （全宽）字体A （12×12） */
		public static final byte[] ESC_FONT_A = new byte[] { ESC, '!', 0 };
		/* 西文字符 （半宽）字体B (8×16)，汉字字符 （全宽）字体B （16×16） */
		public static final byte[] ESC_FONT_B = new byte[] { ESC, '!', 1 };
		/* 12*24   0/48*/
		public static final byte[] ESC_FONTA = new byte[] { ESC, 'M', 48 };
		/* 9*17    1/49*/
		public static final byte[] ESC_FONTB = new byte[] { ESC, 'M', 1 };
		/* 默认颜色字体指令 */
		public static final byte[] ESC_FONT_COLOR_DEFAULT = new byte[] { ESC, 'r',
				0x00 };
		/* 红色字体指令 */
		public static final byte[] ESC_FONT_COLOR_RED = new byte[] { ESC, 'r', 0x01 };
		/* 标准大小 */
		public static final byte[] FS_FONT_ALIGN = new byte[] { FS, 0x21, 1, ESC,
				0x21, 1 };
		/* 横向放大一倍 */
		public static final byte[] FS_FONT_ALIGN_DOUBLE = new byte[] { FS, 0x21, 4,
				ESC, 0x21, 4 };
		/* 纵向放大一倍 */
		public static final byte[] FS_FONT_VERTICAL_DOUBLE = new byte[] { FS, 0x21,
				8, ESC, 0x21, 8, GS, '!', 0x01 };
		/* 横向纵向都放大一倍 */
		public static final byte[] FS_FONT_DOUBLE = new byte[] { FS, 0x21, 12, ESC,
				0x21, 48 };
		/* 横向纵向都放大一倍 */
		public static final byte[] FS_FONT_DOUBLE2 = new byte[] { FS, 0x21, 12, ESC,
				0x21, 48 };
		
 
		
		/* 靠左打印命令 */
		public static final byte[] ESC_ALIGN_LEFT = new byte[] { 0x1b, 'a', 0x00 };
		/* 居中打印命令 */
		public static final byte[] ESC_ALIGN_CENTER = new byte[] { 0x1b, 'a', 0x01 };
		/* 靠右打印命令 */
		public static final byte[] ESC_ALIGN_RIGHT = new byte[] { 0x1b, 'a', 0x02 };
		/* 字体加粗 */
		public static final byte[] ESC_SETTING_BOLD = new byte[] { ESC, 0x45, 1 };
		/* 取消字体加粗 */
		public static final byte[] ESC_CANCEL_BOLD = new byte[] { ESC, 0x45, 0 };
		//DLE EOT n 实时状态传送
	//	public static final byte[] BARCODE_HEIGHT_DOUBLE = new byte[] { GS, 'h',12};
		public static final byte[] POS_BARCODE = new byte[] {0x0A,0x1d, 0x68, 100,0x1d,0x48,0x01,0x1d,0x6B,0x02 };
		public static final byte[] POS_BARCODE2 = new byte[] {0x0A,0x1d, 0x68, 127,0x1d,0x48,0x01,0x1d,0x6B,0x02 };
		
		public static final byte[] POS_END_BARCODE = new byte[] {0x00 };
		//DLE EOT n 实时状态传送	
		
		
		
		
		

}
