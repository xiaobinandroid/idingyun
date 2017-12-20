package com.fuhui.idingyun;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;

@SuppressLint("NewApi")
public class MyWebChromeClient extends WebChromeClient {
	private Context mContext;
	WebView newWebView;
	ValueCallback<Uri> mUploadMessage;
	//private ValueCallback<Uri[]> mUploadCallbackAboveL;  
	
	public  static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
	private static final int REQ_CHOOSE = REQ_CAMERA+1;
	public  static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 =5;  
	public  static ValueCallback<Uri[]> mUploadMessageForAndroid5;

	public MyWebChromeClient(Context context){
		super();
		mContext = context;
	}
	
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
    	
    	if(view.getUrl()==null||view.getUrl().indexOf("normal")!=-1){
    		return false;
    	}
        newWebView = new WebView(view.getContext());
        
        newWebView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
        newWebView.getSettings().setJavaScriptEnabled(true);
        newWebView.getSettings().setDomStorageEnabled(true);
        newWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        newWebView.addJavascriptInterface(new MyObject(view.getContext()), "PRINT_OBJ");
        newWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        newWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        newWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        newWebView.getSettings().setSupportMultipleWindows(true);
        newWebView.getSettings().setUserAgentString("User-Agent");
        //这个setWebViewClient要加上，否则window.open弹出浏览器打开。
        newWebView.setWebViewClient(new WebViewClient());
        newWebView.setWebChromeClient(new MyWebChromeClient(mContext));
        newWebView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (!v.hasFocus()) {
                        v.requestFocus();
                    }
                    break;

                }

                return false;
			}
        	
        });
        view.addView(newWebView,newWebView.getId());
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(newWebView);
        resultMsg.sendToTarget();
        
     //   System.out.println(newWebView.getId()+","+view.getId());
        
      //  map.put(newWebView.getId(), newWebView);
        return true;
    }
  
    
    @Override
    public void onCloseWindow(WebView view) {
     
    	     	WebView newWebView =view;
    	     	
    //	System.out.println("============"+view.getId());
    	if (newWebView != null) {
            newWebView.setVisibility(View.GONE);
            view.removeView(newWebView);
            newWebView=null;
        }
    }
    
    @Override
    public void onProgressChanged(WebView view, int newProgress)
    {  
        super.onProgressChanged(view, newProgress);  
        view.requestFocus();  
    }  

	String compressPath = "";
    private void selectImage() {
    	if (!checkSDcard())
			return;
		chosePic();
		compressPath = Environment
				.getExternalStorageDirectory()
				.getPath()
				+ "/fuiou_wmp/temp";
		new File(compressPath).mkdirs();
		compressPath = compressPath + File.separator + "compress.jpg";
	}

	private boolean checkSDcard() {
		boolean flag = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!flag) {
			Toast.makeText(mContext, "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
		}
		return flag;
	}
	
	private void chosePic() {
		FileUtils.delFile(compressPath);
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
		String IMAGE_UNSPECIFIED = "image/*";
		innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		//mContext.startActivityForResult(wrapperIntent, REQ_CHOOSE);
		((MainActivity)mContext).startActivityForResult(wrapperIntent, REQ_CHOOSE);
		//mContext.startActivities(intents, options);
	}
	
	  // For Android 3.0+
     public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
	        if (mUploadMessage != null) return;
	        mUploadMessage = uploadMsg;   
	        selectImage();
	 }

	// For Android < 3.0
     public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser( uploadMsg, "" );
     }
     // For Android  > 4.1.1
   public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
           openFileChooser(uploadMsg, acceptType);
   }
   
     
   public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
	   //super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
	    mUploadMessageForAndroid5=filePathCallback;
	    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
	    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
	    contentSelectionIntent.setType("image/*");  
	  
	    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
	    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
	    chooserIntent.putExtra(Intent.EXTRA_TITLE, "选择图片");
	    ((MainActivity)mContext).startActivityForResult(chooserIntent,FILECHOOSER_RESULTCODE_FOR_ANDROID_5);  
       return true;  
   }  
}
