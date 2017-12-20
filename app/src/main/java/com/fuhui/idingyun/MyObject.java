package com.fuhui.idingyun;

import android.app.AlertDialog;
import android.content.Context;
import android.webkit.JavascriptInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyObject {
	 private Context context;
	 
	 private static ExecutorService exe= Executors.newCachedThreadPool();
	    public MyObject(Context context) {
	        this.context = context;
	    }

	    //将显示Toast和对话框的方法暴露给JS脚本调用
	    @JavascriptInterface
	    public boolean PRINT_FORMAT(final String content) {
	    	
	    	
	    	//System.out.println(content);
	    	//new ThreadPoolExecutor().getThreadFactory().newThread()
	    	exe.execute(new Runnable() {
				
				@Override
				public void run() {
					 
					com.iiding.print.service.PrintManager.PRINT_FORMAT(content);	
					
				}
			});
	    return true;
	    	//HttpHelper.sendRequestWithHttpURLConnection("https://ssl.iidingyun.com/and_data.vm");
	       // Toast.makeText(context, name+"我是后台追加的", Toast.LENGTH_SHORT).show();
	    }
	    
	    @JavascriptInterface
	    public boolean PRINT_PORT(final String name, final String content) {
	    	
	    	
	       	exe.execute(new Runnable() {
				
	    				@Override
	    				public void run() {
	    					 
	    					com.iiding.print.service.PrintManager.PRINT_PORTS(name,content);
	    				}
	    					
	    			});
	    	return true;
	    	//HttpHelper.sendRequestWithHttpURLConnection("https://ssl.iidingyun.com/and_data.vm");
	       // Toast.makeText(context, name+"我是后台追加的", Toast.LENGTH_SHORT).show();
	    }
	    @JavascriptInterface
	    public boolean PRINT_TEST(final String name, final String content) {
	    	
	    	 
	    	return				com.iiding.print.service.PrintManager.PRINT_PORTS(name,content);
	    		 
	    	 	    	//HttpHelper.sendRequestWithHttpURLConnection("https://ssl.iidingyun.com/and_data.vm");
	       // Toast.makeText(context, name+"我是后台追加的", Toast.LENGTH_SHORT).show();
	    }
	    @JavascriptInterface
	    public String GET_BAD_PRINTER(String name, String content) {
	    	return com.iiding.print.service.PrintPoolManager.getBadPrinter();
	    	//HttpHelper.sendRequestWithHttpURLConnection("https://ssl.iidingyun.com/and_data.vm");
	       // Toast.makeText(context, name+"我是后台追加的", Toast.LENGTH_SHORT).show();
	    }
	    
	    

	    @JavascriptInterface
	    public void showDialog() {
	        new AlertDialog.Builder(context)
	                .setTitle("标题")
	                .setMessage("我是Java不带参").create().show();
	    }
	    
	    
	    @JavascriptInterface
	    public String setConfig(String name, String value){
	    	return SiteConfigFactory.setParam(name, value);
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
}
