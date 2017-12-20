package com.fuhui.idingyun;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private WebView wView;
    public WebView newWebView = null;
    private long mExitTime;
    private ValueCallback<Uri> mUploadMessage;


    public static final int FILECHOOSER_RESULTCODE = 1;
    private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
    private static final int REQ_CHOOSE = REQ_CAMERA+1;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wView = (WebView) findViewById(R.id.wView);
        //wView.loadUrl("http://pad.iidingyun.com/pad");
        //wView.loadUrl("http://cn.iidingyun.com/login.vm");
        wView.loadUrl("http://pad.iidingyun.com/pad/act_login.vm");
        //wView.loadUrl("http://cn.iidingyun.com/merc_regist.vm");

        wView.requestFocus();
        wView.requestFocusFromTouch();

        //①设置WebView允许调用js
        wView.getSettings().setJavaScriptEnabled(true);
        wView.getSettings().setDomStorageEnabled(true);
        wView.getSettings().setDefaultTextEncodingName("UTF-8");
        //②将object对象暴露给Js,调用addjavascriptInterface
        wView.addJavascriptInterface(new MyObject(MainActivity.this), "PRINT_OBJ");
        //   wView.setWebChromeClient(new WebChromeClient());
        wView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wView.getSettings().setAllowFileAccessFromFileURLs(true);
        wView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wView.getSettings().setSupportMultipleWindows(true);
        wView.getSettings().setUserAgentString("User-Agent");
        wView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                if(url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }
        });
        wView.setWebChromeClient(new MyWebChromeClient(this));

	        /*wView.setWebViewClient(new WebViewClient(){
	        	@Override
	        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        		if(url!="")
	        		{
	        			wView.loadUrl(url);
	        		}
	        		return true;
	        	}


	        });*/

    }

    String imagePaths;
    Uri  cameraUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
        Uri uri = null;
        if(resultCode == RESULT_CANCELED){
            uri = Uri.EMPTY;
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;


            }
            if (MyWebChromeClient.mUploadMessageForAndroid5 !=null){
                MyWebChromeClient.mUploadMessageForAndroid5.onReceiveValue(new Uri[] {});
                MyWebChromeClient.mUploadMessageForAndroid5 = null;
            }
        }
        if(requestCode == REQ_CAMERA ){
            afterOpenCamera();
            uri = cameraUri;
        }else if(requestCode == REQ_CHOOSE){
            uri = afterChosePic(intent);
        }else if (requestCode ==MyWebChromeClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null ==MyWebChromeClient.mUploadMessageForAndroid5)
                return;
            //Uri result = (intent == null || resultCode != RESULT_OK) ? null  : intent.getData();
            uri= afterChosePic(intent);
            if (uri != null) {
                MyWebChromeClient.mUploadMessageForAndroid5.onReceiveValue(new Uri[] { uri });
            } else {
                MyWebChromeClient.mUploadMessageForAndroid5.onReceiveValue(new Uri[] {});
            }
            MyWebChromeClient.mUploadMessageForAndroid5 = null;
        }

        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(uri);
            mUploadMessage = null;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    String compressPath = "";
    private Uri afterChosePic(Intent data) {
        compressPath = Environment
                .getExternalStorageDirectory()
                .getPath()
                + "/fuiou_wmp/temp";
        new File(compressPath).mkdirs();
        compressPath = compressPath + File.separator + "compress.jpg";

        // 获取图片的路径：
        String[] proj = { MediaStore.Images.Media.DATA };
        // 好像是android多媒体数据库的封装接口，具体的看Android文档
        //Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
        Uri uri=data.getData();

        String path=getImageAbsolutePath(MainActivity.this, uri);
        //Toast.makeText(this, path,Toast.LENGTH_SHORT).show();
        if(path != null && (path.endsWith(".png")||path.endsWith(".PNG")||path.endsWith(".jpg")||path.endsWith(".JPG"))){
            File newFile = FileUtils.compressFile(path, compressPath);
            return Uri.fromFile(newFile);
        }else{
            Toast.makeText(this, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * 拍照结束后
     */
    private void afterOpenCamera() {
        File f = new File(imagePaths);
        addImageGallery(f);
        File newFile = FileUtils.compressFile(f.getPath(), compressPath);
    }

    /** 解决拍照后在相册中找不到的问题 */
    private void addImageGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String url=wView.getUrl();
        if (keyCode == KeyEvent.KEYCODE_BACK && wView.canGoBack() && url.indexOf("/main.vm")==-1 && url.indexOf("/login.vm")==-1) {
            wView.goBack();// 返回前一个页面
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && (url.indexOf("/main.vm")>-1 || url.indexOf("/login.vm")>-1)) {
            exit();
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            //System.exit(0);
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
