package com.iiding.print.format.face;

import com.iiding.print.format.face.FormatParser.Node;
import com.iiding.print.format.face.FormatParser.NodeType;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class AbstractPrintService implements PrintService,StandardPrintFormatFace{
	

 
 
 @Override
   public  void init() throws IOException {
	         
	        
	            //ESC_INIT 将在清空缓存区的数据
	           write(ESC_INIT);
	            //自定义字体
	            //out.write(ESC_FS_2);
 	            write(ESC_STANDARD);
 	            write(ESC_CANCEL_DEFINE_FONT);
 	           // out.write(ESC_FONTB);
 	           write(ESC_SELECT_CHARACTER);
//	            //进入汉字模式打印
	             write(ESC_CN_FONT);
	            
	            
//	             write(ESC_FONT_B);
//	             write(ESC_FONTA);
 
	    }
	
 
    /**
     * Current command
     */
    private String command;
    /**
     * Current font
     */
    private String font;
 
    private boolean isFontCommand(String c){
    	if(c.equals(PrintFormatFace.D)||c.equals(PrintFormatFace.VD)||c.equals(PrintFormatFace.O)){
    		return true;
    	}
    	return false;
    }
   private static Pattern pattern= Pattern.compile("[\\u4e00-\\u9fa5]");
    public boolean isChinese(String word) {
    	if(word==null||word.equals(""))return false;
    	
    	
    	for (int i = 0; i < word.length(); i++) {
    		Matcher matcher=pattern.matcher(word.charAt(i)+"");
    		if(matcher.matches())return true;
		}
    	  
    	return false;
                                                        //返回标志位结果
    }

//    
    
	public void print( String content) throws IOException {
		
		 content=content.replaceAll(PrintFormatFace.SP.toLowerCase(),PrintFormatFace.SPACE);
		// content=content.replaceAll(PrintFormatFace.SPACE, PrintFormatFace.FULL_SPACE);
		//System.out.println(content);
	 
		
		List<Node> list =new FormatParser().parse(  content );
		for (Node node : list) {
			if(node.nodeType==NodeType.format){
				String command=node.content.toLowerCase();
					
				 
					this.command=command;
	 
					if(isFontCommand(command)){
						this.font=command;
						
					}
					/**
					 * Keep Original Font If use <br_>
					 */
					if(command!=null&&command.equals(PrintFormatFace.BR_)){
						//printCommand(font==null?PrintFormatFace.O:font);
						printCommand(font);
						printCommand(command);
					} 
					/**
					 * Keep Original Font If use <f_>
					 */
					else if(command.equals(PrintFormatFace.USE_PRE_FONT_)){
						printCommand(font);
					}else{
						printCommand(command);
					}
				 
				
			
			}else{
				
				/**
				 * 如果前面是条码命令，接着打印条码并且打印条码结束命令
				 */
				if(command!=null&&(command.equals(PrintFormatFace.BARCODE)||command.equals(PrintFormatFace.BARCODE2))){
					if(node.content.length()>=13){
						write((node.content).substring(0,13).getBytes());
						write(POS_END_BARCODE);
						write((node.content).substring(13).getBytes());
					}else{
						write(POS_END_BARCODE);
						write(("Barcode = "+node.content).getBytes());
					}
				}  else{
					String c=node.content;
				//	c = doubleSpace(c);
					if(isChinese(c)){
						//c=c.replaceAll(PrintFormatFace.SPACE,PrintFormatFace.FULL_SPACE);
						c=c.replaceAll(PrintFormatFace.SPACE,PrintFormatFace.SPACE+PrintFormatFace.SPACE);
					}else{
						//System.out.println(">>>>>>"+c);
					}
					
					//System.out.println("=========>"+c);
					//write((c).getBytes());
					write(c);
					//write((c).getBytes("UTF-8"));
					write(FS_FONT_ALIGN);
			 

				}
				
				 
			}
		}
		/**
		 * 最后换行
		 */
		 write(PrintFormatFactory.getCommand(PrintFormatFace.BR));
 
		
	}
	
	
	private void write(String c) throws IOException {
		write((c).getBytes("GBK"));
	}
	
 
	private void printCommand(String command) throws IOException {
		byte [] c =PrintFormatFactory.getCommand(command);
		//ignore unknown format 
		if(c!=null){
			//System.out.println(command);
			
			write(c);
			if(command.startsWith("<img")){
				   write(ESC_INIT);
			    
			}
		}
		else{
			
			//遇到结束符号，重置字体
			write(FS_FONT_ALIGN);
			//System.out.println(command);
		}
	}
	 
 
}
