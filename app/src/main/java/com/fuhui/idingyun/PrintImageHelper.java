package com.fuhui.idingyun;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.format.Time;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 将图片转化为二进??
 * @author nsz
 * 2015??1??30??
 */
public class PrintImageHelper {
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static Bitmap createContrast(Bitmap src, double value) {
	    // image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	    // color information
	    int A, R, G, B;
	    int pixel;
	    // get contrast value
	    double contrast = Math.pow((100 + value) / 100, 2);

	    // scan through all pixels
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            // apply filter contrast for every channel R, G, B
	            R = Color.red(pixel);
	            R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
	            if(R < 0) { R = 0; }
	            else if(R > 255) { R = 255; }

	            G = Color.red(pixel);
	            G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
	            if(G < 0) { G = 0; }
	            else if(G > 255) { G = 255; }

	            B = Color.red(pixel);
	            B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
	            if(B < 0) { B = 0; }
	            else if(B > 255) { B = 255; }

	            // set new pixel color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	    return bmOut;

	}
	
	 // 给图片添加水??  
    private static Bitmap createBitmap(Bitmap src) {
        Time t = new Time();
        t.setToNow();   
        int w = src.getWidth();  
        int h = src.getHeight();  
        String mstrTitle = t.year + " ?? " + (t.month +1) + " ?? " + t.monthDay+" ??";
        Bitmap bmpTemp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpTemp);
        Paint p = new Paint();
        String familyName = "宋体";
        Typeface font = Typeface.create(familyName, Typeface.BOLD);
        p.setColor(Color.BLACK);
        p.setTypeface(font);  
        p.setTextSize(33);  
        canvas.drawBitmap(src, 0, 0, p);  
        canvas.drawText(mstrTitle, 20, 310, p);  
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();  
        return bmpTemp;  
    } 
	/**解析图片 获取打印数据**/
	public static byte[] getReadBitMapBytes(Bitmap bitmap) {
		/***图片添加水印**/
	//	bitmap = createBitmap(bitmap);
		bitmap=createContrast(bitmap, 0d);
		byte[] bytes = null;  //打印数据
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		System.out.println("width=" + width + ", height=" + height);
		int heightbyte = (height - 1) / 8 + 1;
		int bufsize = width * heightbyte;
		int m1, n1;
        byte[] maparray = new byte[bufsize];
        
        byte[] rgb = new byte[3];
        
        int []pixels = new int[width * height]; //通过位图的大小创建像素点数组
        
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        /**解析图片 获取位图数据**/
		for (int j = 0;j < height; j++) {
			for (int i = 0; i < width; i++) {
				int pixel = pixels[width * j + i]; /**获取ＲＧＢ???**/
				int r = Color.red(pixel);
				int g = Color.green(pixel);
				int b = Color.blue(pixel);
				//System.out.println("i=" + i + ",j=" + j + ":(" + r + ","+ g+ "," + b + ")");
				rgb[0] = (byte)r;
				rgb[1] = (byte)g;
				rgb[2] = (byte)b;
				 if (r != 255 || g != 255 || b != 255){//如果不是空白的话用黑色填??    这里如果童鞋要过滤颜色在这里处理 
                     m1 = (j / 8) * width + i;
                     n1 = j - (j / 8) * 8;
                     maparray[m1] |= (byte)(1 << 7 - ((byte)n1));
                 }
			}
		}
		byte[] b = new byte[322];
		int line = 0;
		int j = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		/**对位图数据进行处??**/
		for (int i = 0; i < maparray.length; i++) {
			b[j] = maparray[i];
			j++;
			if (j == 322) {  /**  322图片的宽 **/
				if (line < ((322 - 1) / 8)) {
					byte[] lineByte = new byte[329];
					byte nL = (byte) 322;
					byte nH = (byte) (322 >> 8);
					int index = 5;
					/**添加打印图片前导字符  每行?? 这里??8??**/
					lineByte[0] = 0x1B;
					lineByte[1] = 0x2A;
					lineByte[2] = 1;
					lineByte[3] = nL;
					lineByte[4] = nH;
					/**copy 数组数据**/
					System.arraycopy(b, 0, lineByte, index, b.length);

					lineByte[lineByte.length - 2] = 0x0D;
					lineByte[lineByte.length - 1] = 0x0A;
					baos.write(lineByte, 0, lineByte.length);
					try {
						baos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					line++;
				}
				j = 0;
			}
		}
		bytes = baos.toByteArray();
		return bytes;
	}

	public void init(){
//		Gray = 0.29900 * R + 0.58700 * G + 0.11400 * B
	}
	
	/*************************************************************************
	 * 我们的热敏打印机是RP-POS80S或RP-POS80P或RP-POS80CS或RP-POS80CP打印??
	 * 360*360的图片，8个字节（8个像素点）是??个二进制，将二进制转化为十进制数??
	 * y轴：24个像素点为一组，??360就是15组（0-14??
	 * x轴：360个像素点??0-359??
	 * 里面的每??组（24*360），??8个像素点为一个二进制，（每组??3个，3*8=24??
	 **************************************************************************/
	/**
	 * 把一张Bitmap图片转化为打印机可以打印的bit(将图片压缩为360*360)
	 * 效率很高（相对于下面??
	 * @param bit
	 * @return
	 */
	public static byte[] draw2PxPoint(Bitmap bit) {
		byte[] data = new byte[16290];
		int k = 0;
		for (int j = 0; j < 15; j++) {
			data[k++] = 0x1B;
			data[k++] = 0x2A;
			data[k++] = 33; // m=33时，选择24点双密度打印，分辨率达到200DPI??
			data[k++] = 0x68;
			data[k++] = 0x01;
			for (int i = 0; i < 360; i++) {
				for (int m = 0; m < 3; m++) {
					for (int n = 0; n < 8; n++) {
						byte b = px2Byte(i, j * 24 + m * 8 + n, bit);
						data[k] += data[k] + b;
					}
					k++;
				}
			}
			data[k++] = 10;
		}
		return data;
	}
	
	/**
	 * 把一张Bitmap图片转化为打印机可以打印的bit
	 * @param bit
	 * @return
	 */
	public static byte[] pic2PxPoint(Bitmap bit){
		long start = System.currentTimeMillis();
		byte[] data = new byte[16290];
		int k = 0;
		for (int i = 0; i < 15; i++) {
			data[k++] = 0x1B;
			data[k++] = 0x2A;
			data[k++] = 33; // m=33时，选择24点双密度打印，分辨率达到200DPI??
			data[k++] = 0x68;
			data[k++] = 0x01;
			for (int x = 0; x < 360; x++) {
				for (int m = 0; m < 3; m++) {
					byte[]  by = new byte[8];
					for (int n = 0; n < 8; n++) {
						byte b = px2Byte(x, i * 24 + m * 8 +7-n, bit);
						by[n] = b;
					}
					data[k] = (byte) changePointPx1(by);
					k++;
				}
			}
			data[k++] = 10;
		}
		long end = System.currentTimeMillis();
		long str = end - start;
		Log.i("TAG", "str:" + str);
		return data;
	}
	
	/**
	 * 图片二???化，黑色是1，白色是0
	 * @param x  横坐??
	 * @param y	 纵坐??			
	 * @param bit 位图
	 * @return
	 */
	public static byte px2Byte(int x, int y, Bitmap bit) {
		byte b;
		int pixel = bit.getPixel(x, y);
		int red = (pixel & 0x00ff0000) >> 16; // 取高两位
		int green = (pixel & 0x0000ff00) >> 8; // 取中两位
		int blue = pixel & 0x000000ff; // 取低两位
		int gray = RGB2Gray(red, green, blue);
		if ( gray < 128 ){
			b = 1;
		} else {
			b = 0;
		}
		return b;
	}
	
	/**
	 * 图片灰度的转??
	 * @param r  
	 * @param g
	 * @param b
	 * @return
	 */
	private static int RGB2Gray(int r, int g, int b){
		int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);  //灰度转化公式
		return  gray;
	}
	
	/**
	 * 对图片进行压缩（去除透明度）
	 * @param bitmapOrg
	 */
	public static Bitmap compressPic(Bitmap bitmapOrg) {
		// 获取这个图片的宽和高
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// 定义预转换成的图片的宽度和高??
		int newWidth = 360;
		int newHeight = 360;
		Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
		Canvas targetCanvas = new Canvas(targetBmp);
		targetCanvas.drawColor(0xffffffff);
		targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
		return targetBmp;
	}
	
	
	/**
	 * 对图片进行压??(不去除???明??)
	 * @param bitmapOrg
	 */
	public static Bitmap compressBitmap(Bitmap bitmapOrg) {
		// 加载??要操作的图片，这里是??张图??
//		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),R.drawable.alipay);
		// 获取这个图片的宽和高
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// 定义预转换成的图片的宽度和高??
		int newWidth = 360;
		int newHeight = 360;
		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,height, matrix, true);
		// 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton??
//		BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
		return resizedBitmap;
	}
	
	/**
	 * 将[1,0,0,1,0,0,0,1]这样的二进制转为化十进制的数值（效率更高??
	 * @param arry
	 * @return
	 */
	public static int changePointPx1(byte[] arry){
		int v = 0;
		for (int j = 0; j <arry.length; j++) {
			if( arry[j] == 1) {
				v = v | 1 << j;
			}
		}
		return v;
	}
	
	/**
	 * 将[1,0,0,1,0,0,0,1]这样的二进制转为化十进制的数??
	 * @param arry
	 * @return
	 */
	public byte changePointPx(byte[] arry){
		byte v = 0;
		for (int i = 0; i < 8; i++) {
			v += v + arry[i];
		}
		return v;
	}
	
	/**
	 * 得到位图的某个点的像素???
	 * @param bitmap
	 * @return
	 */
	public byte[] getPicPx(Bitmap bitmap){
		int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];// 保存??有的像素的数组，图片宽×高
		bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		for (int i = 0; i < pixels.length; i++) {
			int clr = pixels[i];
			int red = (clr & 0x00ff0000) >> 16; // 取高两位
		int green = (clr & 0x0000ff00) >> 8; // 取中两位
				int blue = clr & 0x000000ff; // 取低两位
				System.out.println("r=" + red + ",g=" + green + ",b=" + blue);
		}
		return null;
	}
	
}