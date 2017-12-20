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
 * ��ͼƬת��Ϊ����??
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
	
	 // ��ͼƬ���ˮ??  
    private static Bitmap createBitmap(Bitmap src) {
        Time t = new Time();
        t.setToNow();   
        int w = src.getWidth();  
        int h = src.getHeight();  
        String mstrTitle = t.year + " ?? " + (t.month +1) + " ?? " + t.monthDay+" ??";
        Bitmap bmpTemp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpTemp);
        Paint p = new Paint();
        String familyName = "����";
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
	/**����ͼƬ ��ȡ��ӡ����**/
	public static byte[] getReadBitMapBytes(Bitmap bitmap) {
		/***ͼƬ���ˮӡ**/
	//	bitmap = createBitmap(bitmap);
		bitmap=createContrast(bitmap, 0d);
		byte[] bytes = null;  //��ӡ����
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		System.out.println("width=" + width + ", height=" + height);
		int heightbyte = (height - 1) / 8 + 1;
		int bufsize = width * heightbyte;
		int m1, n1;
        byte[] maparray = new byte[bufsize];
        
        byte[] rgb = new byte[3];
        
        int []pixels = new int[width * height]; //ͨ��λͼ�Ĵ�С�������ص�����
        
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        /**����ͼƬ ��ȡλͼ����**/
		for (int j = 0;j < height; j++) {
			for (int i = 0; i < width; i++) {
				int pixel = pixels[width * j + i]; /**��ȡ�ңǣ�???**/
				int r = Color.red(pixel);
				int g = Color.green(pixel);
				int b = Color.blue(pixel);
				//System.out.println("i=" + i + ",j=" + j + ":(" + r + ","+ g+ "," + b + ")");
				rgb[0] = (byte)r;
				rgb[1] = (byte)g;
				rgb[2] = (byte)b;
				 if (r != 255 || g != 255 || b != 255){//������ǿհ׵Ļ��ú�ɫ��??    �������ͯЬҪ������ɫ�����ﴦ�� 
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
		
		/**��λͼ���ݽ��д�??**/
		for (int i = 0; i < maparray.length; i++) {
			b[j] = maparray[i];
			j++;
			if (j == 322) {  /**  322ͼƬ�Ŀ� **/
				if (line < ((322 - 1) / 8)) {
					byte[] lineByte = new byte[329];
					byte nL = (byte) 322;
					byte nH = (byte) (322 >> 8);
					int index = 5;
					/**��Ӵ�ӡͼƬǰ���ַ�  ÿ��?? ����??8??**/
					lineByte[0] = 0x1B;
					lineByte[1] = 0x2A;
					lineByte[2] = 1;
					lineByte[3] = nL;
					lineByte[4] = nH;
					/**copy ��������**/
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
	 * ���ǵ�������ӡ����RP-POS80S��RP-POS80P��RP-POS80CS��RP-POS80CP��ӡ??
	 * 360*360��ͼƬ��8���ֽڣ�8�����ص㣩��??�������ƣ���������ת��Ϊʮ������??
	 * y�᣺24�����ص�Ϊһ�飬??360����15�飨0-14??
	 * x�᣺360�����ص�??0-359??
	 * �����ÿ??�飨24*360����??8�����ص�Ϊһ�������ƣ���ÿ��??3����3*8=24??
	 **************************************************************************/
	/**
	 * ��һ��BitmapͼƬת��Ϊ��ӡ�����Դ�ӡ��bit(��ͼƬѹ��Ϊ360*360)
	 * Ч�ʺܸߣ����������??
	 * @param bit
	 * @return
	 */
	public static byte[] draw2PxPoint(Bitmap bit) {
		byte[] data = new byte[16290];
		int k = 0;
		for (int j = 0; j < 15; j++) {
			data[k++] = 0x1B;
			data[k++] = 0x2A;
			data[k++] = 33; // m=33ʱ��ѡ��24��˫�ܶȴ�ӡ���ֱ��ʴﵽ200DPI??
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
	 * ��һ��BitmapͼƬת��Ϊ��ӡ�����Դ�ӡ��bit
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
			data[k++] = 33; // m=33ʱ��ѡ��24��˫�ܶȴ�ӡ���ֱ��ʴﵽ200DPI??
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
	 * ͼƬ��???������ɫ��1����ɫ��0
	 * @param x  ����??
	 * @param y	 ����??			
	 * @param bit λͼ
	 * @return
	 */
	public static byte px2Byte(int x, int y, Bitmap bit) {
		byte b;
		int pixel = bit.getPixel(x, y);
		int red = (pixel & 0x00ff0000) >> 16; // ȡ����λ
		int green = (pixel & 0x0000ff00) >> 8; // ȡ����λ
		int blue = pixel & 0x000000ff; // ȡ����λ
		int gray = RGB2Gray(red, green, blue);
		if ( gray < 128 ){
			b = 1;
		} else {
			b = 0;
		}
		return b;
	}
	
	/**
	 * ͼƬ�Ҷȵ�ת??
	 * @param r  
	 * @param g
	 * @param b
	 * @return
	 */
	private static int RGB2Gray(int r, int g, int b){
		int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);  //�Ҷ�ת����ʽ
		return  gray;
	}
	
	/**
	 * ��ͼƬ����ѹ����ȥ��͸���ȣ�
	 * @param bitmapOrg
	 */
	public static Bitmap compressPic(Bitmap bitmapOrg) {
		// ��ȡ���ͼƬ�Ŀ�͸�
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// ����Ԥת���ɵ�ͼƬ�Ŀ�Ⱥ͸�??
		int newWidth = 360;
		int newHeight = 360;
		Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
		Canvas targetCanvas = new Canvas(targetBmp);
		targetCanvas.drawColor(0xffffffff);
		targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
		return targetBmp;
	}
	
	
	/**
	 * ��ͼƬ����ѹ??(��ȥ��???��??)
	 * @param bitmapOrg
	 */
	public static Bitmap compressBitmap(Bitmap bitmapOrg) {
		// ����??Ҫ������ͼƬ��������??��ͼ??
//		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),R.drawable.alipay);
		// ��ȡ���ͼƬ�Ŀ�͸�
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// ����Ԥת���ɵ�ͼƬ�Ŀ�Ⱥ͸�??
		int newWidth = 360;
		int newHeight = 360;
		// ���������ʣ��³ߴ��ԭʼ�ߴ�
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ��������ͼƬ�õ�matrix����
		Matrix matrix = new Matrix();
		// ����ͼƬ����
		matrix.postScale(scaleWidth, scaleHeight);
		// �����µ�ͼƬ
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,height, matrix, true);
		// �����洴����Bitmapת����Drawable����ʹ�������ʹ����ImageView, ImageButton??
//		BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
		return resizedBitmap;
	}
	
	/**
	 * ��[1,0,0,1,0,0,0,1]�����Ķ�����תΪ��ʮ���Ƶ���ֵ��Ч�ʸ���??
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
	 * ��[1,0,0,1,0,0,0,1]�����Ķ�����תΪ��ʮ���Ƶ���??
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
	 * �õ�λͼ��ĳ���������???
	 * @param bitmap
	 * @return
	 */
	public byte[] getPicPx(Bitmap bitmap){
		int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];// ����??�е����ص����飬ͼƬ�����
		bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		for (int i = 0; i < pixels.length; i++) {
			int clr = pixels[i];
			int red = (clr & 0x00ff0000) >> 16; // ȡ����λ
		int green = (clr & 0x0000ff00) >> 8; // ȡ����λ
				int blue = clr & 0x000000ff; // ȡ����λ
				System.out.println("r=" + red + ",g=" + green + ",b=" + blue);
		}
		return null;
	}
	
}