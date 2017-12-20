package com.iiding.applet.helper;

import com.iiding.applet.to.LogTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LogService {
	
	

	private static final List<LogTO> log = new ArrayList<LogTO>();
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
	public static void log(String message){
		//System.out.println(message);
		LogTO to = new LogTO(message, System.currentTimeMillis());
		if(!log.contains(to))
		 log.add(to);
		tryCommitLog();
	}

	
	private static String getLogMessage(){
		 StringBuilder sb = new StringBuilder();
		
		 for (LogTO to : log) {
			sb.append(sdf.format(new Date(to.getTime()))).append(",").append(to.getMessage());
		}
		 return sb.toString();
	}
	
	private static void tryCommitLog(){
		boolean commit=false;
		if(log.size()>10){
			commit=true;
		}else  {
			for (LogTO to : log) {
				if(System.currentTimeMillis()-to.getTime()>1000*60){
					commit=true;
				}
			}
			
		}
		/**
		 * ²»Ìá½»
		 */
		if(!commit)return;
		
		String message=getLogMessage();
		commit(message);
		
	}
	 private static void commit( String content) {
//		 log.clear();
//		 System.out.println(">>>>>>>>>>>>  committing log  >>>>>>>>>");
//		// HttpGet.post("http://service.iiding.com/common/LogServlet", "","userid="+AppletService.getUserid()+"&content="+  content);
//		 try {
//			 if(AppletService.isLog())
//				 HttpGet.post(AppletHelper.getUrl()+"/status.do","op=add&key=log_print&value="+ URLEncoder.encode(content,"utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	 }
	
}
