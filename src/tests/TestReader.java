package tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;

import common.Mensaje;
import sintactico.Token;

public class TestReader {
	private ArrayList<Token> result=new ArrayList<Token>();
	public TestReader(String text,ArrayList<Token>tokens,HashMap<String,String>expresiones,HashMap<String,String>errores,HashMap<Integer,String>comentarios) {
		text=text+" ";
		@SuppressWarnings("unused")
		String aux="";
		int count=0;
		for(int i=0;i<text.length();i++) {
			if(text.charAt(i)=='(' && text.charAt(i+1)=='*') {count++;i++;}
			else if(text.charAt(i)=='*' && text.charAt(i+1)==')') {count--;i++;}
			else if(count==0)aux+=text.charAt(i);
		}
		String[] partes=text.split("\\s+");
		for(int i=0; i<partes.length;i++) {
			if(partes[i].trim().length()>0) {
				add(partes[i],tokens,expresiones);
			}
		}
	}



	private void add(String token,ArrayList<Token>tokens,HashMap<String,String>expresiones) {
		if(token.trim().length()==0)return;
		if(token.contains("\""))token=token.replaceAll("\"","\\\"");
		boolean found=false;
		for(Token t: tokens) {
			if(t.getValue().equalsIgnoreCase(token)) {
				result.add(t);found=true;break;				
			}
			else if(token.contains(t.getValue())) {
				add(token.split("["+t.getValue()+"]", 2)[0],tokens,expresiones);
				result.add(t);
				add(token.split("["+t.getValue()+"]", 2)[1],tokens,expresiones);
				found=true;break;	
			}
		}
		if(!found) {
			SortedSet<String> keys = new TreeSet<String>(expresiones.keySet());
			for(String s:keys){
				try {
				if(token.matches(expresiones.get(s))) {	
					for(Token t: tokens) {
						String value=t.getValue().trim();
						if(value.length()>3) {
							value=value.substring(1,(t.getValue().length()-1));
						}
						if(value.equalsIgnoreCase(s)) {
							result.add(t);
						}
					}
					found=true;break;
				}
				else if(token.matches(expresiones.get(s)+".*")) {
					@SuppressWarnings("unused")
					String aux=token.replace(expresiones.get(s)+".*", "");
				}
				}catch(PatternSyntaxException pse) {
					Mensaje.print("ERROR","Pattern no valido : "+expresiones.get(s));
				}
			}
		}
		if(!found)Mensaje.print("ERROR","No encontrado: "+token);
	}



	public ArrayList<Token> getResult() {
		return result;
	}



	public void setResult(ArrayList<Token> result) {
		this.result = result;
	}
}
