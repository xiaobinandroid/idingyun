package com.iiding.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ImageHelper {
	 /**
     * ��ͼƬ����ѹ����ȥ��͸���ȣ�
     *
     * @param bitmapOrg
     */
    public static Bitmap compressPic(Bitmap bitmap) {
        // ��ȡ���ͼƬ�Ŀ�͸�
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // ָ��������Ŀ�Ⱥ͸߶�
        int newWidth = 240;
        int newHeight = 240;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmap, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }
    
    
    /**
     * �Ҷ�ͼƬ�ڰ׻�����ɫ��1����ɫ��0
     *
     * @param x   ������
     * @param y   ������
     * @param bit λͼ
     * @return
     */
    public static byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // ȡ����λ
            int green = (pixel & 0x0000ff00) >> 8; // ȡ����λ
            int blue = pixel & 0x000000ff; // ȡ����λ
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    /**
     * ͼƬ�Ҷȵ�ת��
     */
    private static int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);  //�Ҷ�ת����ʽ
        return gray;
    }
    
    
    
    
    
    
    
    
    /*************************************************************************
     * ����һ��240*240��ͼƬ���ֱ�����Ϊ24, ����10�д�ӡ
     * ÿһ��,��һ�� 240*24 �ĵ���, ÿһ����24����,�洢��3��byte���档
     * ÿ��byte�洢8�����ص���Ϣ����Ϊֻ�кڰ���ɫ�����Զ�ӦΪ1��λ�Ǻ�ɫ����ӦΪ0��λ�ǰ�ɫ
     **************************************************************************/
    /**
     * ��һ��BitmapͼƬת��Ϊ��ӡ�����Դ�ӡ���ֽ���
     *
     * @param bmp
     * @return
     */
    public static byte[] draw2PxPoint(Bitmap bmp) {
    	//bmp=compressPic(bmp);
        //�����洢ת����� bitmap ���ݡ�ΪʲôҪ�ټ�1000������Ϊ��Ӧ�Ե�ͼƬ�߶��޷�      
        //����24ʱ�����������bitmap �ֱ���Ϊ 240 * 250��ռ�� 7500 byte��
        //����ʵ����Ҫ�洢11�����ݣ�ÿһ����Ҫ 24 * 240 / 8 =720byte �Ŀռ䡣�ټ���һЩָ��洢�Ŀ�����
        //���Զ����� 1000byte �Ŀռ������׵ģ���Ȼ����ʱ���׳��������Խ����쳣��
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 10000;
        byte[] data = new byte[size];
        int k = 0;
        //�����о�Ϊ0��ָ��
        data[k++] = 0x1B;
        data[k++] = 0x33;
        data[k++] = 0x00;
        // ���д�ӡ
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            //��ӡͼƬ��ָ��
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33; 
            data[k++] = (byte) (bmp.getWidth() % 256); //nL
            data[k++] = (byte) (bmp.getWidth() / 256); //nH
            //����ÿһ�У����д�ӡ
            for (int i = 0; i < bmp.getWidth(); i++) {
                //ÿһ��24�����ص㣬��Ϊ3���ֽڴ洢
                for (int m = 0; m < 3; m++) {
                    //ÿ���ֽڱ�ʾ8�����ص㣬0��ʾ��ɫ��1��ʾ��ɫ
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            data[k++] = 10;//����
        }
        return data;
    }
}
