package com.iiding.print.format.face;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fuhui.idingyun.PrintImageHelper;
import com.iiding.image.ImageHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PrintFormatFactory implements StandardPrintFormatFace,ClientDisplayFormatFace, PrintFormatFace{
 
	private static byte[] b;
	private static final Map<String, byte[]> map = new HashMap<String, byte[]>();
	static{
		
		
		
		map.put(RIGHT, ESC_ALIGN_RIGHT);
		map.put(LEFT, ESC_ALIGN_LEFT);
		map.put(CENTER, ESC_ALIGN_CENTER);
		
		
		
		map.put(VERTICAL_DOUBLE, FS_FONT_VERTICAL_DOUBLE);
		map.put(VD, FS_FONT_VERTICAL_DOUBLE);
		//map.put(VD_, FS_FONT_VERTICAL_DOUBLE);
		
		
		map.put(O, FS_FONT_ALIGN);
		map.put(DOUBLE, FS_FONT_DOUBLE);
		map.put(D, FS_FONT_DOUBLE);
		map.put(D2, FS_FONT_DOUBLE2);
		map.put(RED, ESC_FONT_COLOR_RED);
		
		map.put(OPEN, ESC_OPEN_DRAWER);
		
		//map.put(BACK, ESC_LF);
 
		map.put(BR, "\n".getBytes());
		map.put(BR_, "\n".getBytes());
		map.put(SP, " ".getBytes());
		
		
		
	 
		map.put(FULL_CUT, POS_CUT_MODE_FULL);
		map.put(FC, POS_CUT_MODE_FULL);
		
		
		
		map.put(PARTIAL_CUT, POS_CUT_MODE_PARTIAL);
		map.put(PC, POS_CUT_MODE_PARTIAL);
		map.put(BARCODE, POS_BARCODE);
		map.put(_BARCODE, POS_END_BARCODE);
 
		map.put(CD_CHANGE, CHANGE);
		map.put(CD_CHARGE, CHARGE);
		map.put(CD_TOTAL, TOTAL);
		map.put(CD_UNIT_PRICE ,UNIT_PRICE);
		map.put(BARCODE2, POS_BARCODE2);
		//map.put(BARCODE_DOUBLE ,BARCODE_HEIGHT_DOUBLE);
		
//		map.put("<code>", POS_BARCODE);
		
		map.put(BOLD, ESC_SETTING_BOLD);
		//initLogo();
		init((byte)10);
//		
// 		map.put("</p>",ESC_CANCEL_BOLD );
	}
	
	
	private static final int MAX_TRY_TIMES=3;
	private     static final Pattern p = Pattern.compile("<img\\s*=\\s*\"?(.*?)(\"|>|\\s*)>");//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
	private static final Map<String, Integer> TAG_TIMES=new HashMap<String, Integer>();
	
	
	
	
	
	


	    /**
	     * 从网络上获取图片,并返回输入流
	     * 
	     * @param path
	     *            图片的完整地址
	     * @return InputStream
	     * @throws Exception
	     */
	    public static InputStream getImageStream(String path)   {
	    	 HttpURLConnection conn=null;
	    	try{
		        URL url = new URL(path);
		          conn = (HttpURLConnection)url.openConnection();
		        conn.setReadTimeout(10 * 1000);
		        conn.setConnectTimeout(10 * 1000);
		        conn.setRequestMethod("GET");
		        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		            return conn.getInputStream();
		        }
	    	}catch (Exception e) {
				e.printStackTrace();
			}finally{
//				if(conn!=null)
//				conn.disconnect();
			}
	        return null;
	    }

	
	
	
	public static void init(String s){
		//s="=======  <img =http://cn.bing.com/sa/simg/CN_Logo_Gray.png>   8888888"+s;
		
		// System.out.println(s+"  length+>");
		        Matcher m = p.matcher(s);

		        while(m.find()){
		        	String tag=m.group();
		        	if(map.containsKey(tag))continue;
		        	
		        	String path=m.group(1);
		            //	Bitmap bmp=BitmapFactory.decodeStream(is)
		            	InputStream in=getImageStream(path);
		            try {
		            	
			            Bitmap bmp =  BitmapFactory.decodeStream(in);
			            	 
			            bmp= PrintImageHelper.createContrast(bmp, 1);
						//byte[] b = Utils.decodeBitmap(bmp);
						//byte[] b = PrintImageHelper.getReadBitMapBytes(bmp);
						//byte[] b = PrintImageHelper.draw2PxPoint(bmp);
					
		            	// System.out.println(tag+"  length+>"+m.group(1));
		            	///ImageWrapper img=new ImageWrapper(m.group(1));
		            	//b=	img.getImageCommand();
			            
			            b=ImageHelper.draw2PxPoint(bmp);
						//b = ImagePixelUtils.imagePixelToPosByte_24(m.group(1));
		            	
						map.put(tag, b);
						 
					} catch (Exception e) {
						
						//log("{img:'"+tag+"',ex:'"+e.getLocalizedMessage()+"'}");
					//	HttpGet.post("/print_log.vm",  "img="+tag);
						
						Integer i=TAG_TIMES.get(tag);
						if(i==null){
							i=1;
						}else{
							i++;
						}
						TAG_TIMES.put(tag, i);
//							System.out.println(">>>>>>>>>>>"+i);
						if(i!=null  &&  i>MAX_TRY_TIMES){
							 map.put(tag, "".getBytes());	
						}
					
						
						log("{i:'"+i+ "',img:'"+tag.replaceAll("'", "")+"',ex:'"+e.getLocalizedMessage().replaceAll("'", "")+"'}");
						e.printStackTrace();
					}finally{
						if(in!=null){
							try {
								in.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
		        }

	   
	}
	
	
	public static void log(String param){
		
		
		//HttpGet.post("/print_log.vm","log="+ param);
		
		
	}
	
	
	
	/**
	 * 重复获取数据，预警
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static String tryCreateImage() {
			StringBuilder sb= new StringBuilder();
			Iterator<Entry<String, Integer>> it=TAG_TIMES.entrySet().iterator();
			  while (it.hasNext()) {
				  Entry<String, Integer> e=it.next();
				  //if(e.getValue()<=MAX_TRY_TIMES){
					  
					//  System.out.println("tryCreateImage==>"+e.getKey());
					  
					 try{
						 //
						 sb.append(e.getKey());
						 Matcher m = p.matcher(e.getKey());
				        while(m.find()){
				        	 
				           	String tag=m.group();
				        	if(map.containsKey(tag))continue;
//				        	ImageWrapper img=new ImageWrapper(m.group(1));
//				        	b=	img.getImageCommand();
				        	map.put(tag, b);
				        	TAG_TIMES.remove(e.getKey());
				        }
				        
						}catch (Exception ee) {
							ee.printStackTrace();
							TAG_TIMES.put(e.getKey(), e.getValue()+1);
						}
				  }
			//  }
						return sb.toString();
		
	}
	
	
	private static void  init(byte x){
		
		for(byte i=1; i < x; i++){
 
			map.put("<r"+ i +">", new byte[] { ESC, 'e',i});
			
		}
		
	}
	
	
	
	public static byte[] getCommand(String key){
		
//		 if(key.equals("<r2>"))
//		System.out.println(">>>"+map.get(key));
		return map.get(key);
		
	}

	
 
 
	
}
