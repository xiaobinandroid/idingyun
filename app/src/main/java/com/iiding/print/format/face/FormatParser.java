package com.iiding.print.format.face;

import java.util.ArrayList;
import java.util.List;

public class FormatParser {


		
	public	enum NodeType{
			format,content;
		}
	public	class Node{
		public	NodeType nodeType;
		public String content;
		}
		
		
		
		
		
		public List<Node> parse(String content ){
			List<Node> list = new ArrayList<Node>();
			char[] cs = content.toCharArray();
			String pc="";
			Node node=new Node();
			boolean firstChar=true;
			for (char c : cs) {
				
				
				if(firstChar){
					if(c=='<'){
						
						node.nodeType= NodeType.format;
					
					}else{
						node.nodeType= NodeType.content;
						
					}
					pc+=c;
					firstChar=false;
					 
				}else{
					/**
					 * ½áÊø
					 */
					if(c=='>'){
						firstChar=true;
						if(node.nodeType== NodeType.format)
						pc+=c;
						node.content=pc;
						list.add(node);
						node=new Node();
						pc="";
				 
					
					}
					/**
					 * ¿ªÊ¼
					 */
					else if(c=='<'){
						firstChar=false;
				 
						node.content=pc;
						list.add(node);
						node=new Node();
						node.nodeType= NodeType.format;
						
						
						pc="";
						pc+=c;
					
					}else{
						pc+=c;
					}
					 
				}
				
				
			}
			
			if(node.nodeType== NodeType.content){
				node.content=pc;
				list.add(node);
			}
			
			return list;
			
			
		}
	 
		
		public  void printFormat(String content) {
//			DefaultPrintService f = new DefaultPrintService();
//			String content="<right>1111<left><left>2222<left>";
			List<Node> list = parse(content);
			for (Node node : list) {
				System.out.println(node.content+",type="+node.nodeType);
			}
			 
		}
		
}
