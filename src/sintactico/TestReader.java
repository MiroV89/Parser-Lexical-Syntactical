package sintactico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

public class TestReader {
	private ArrayList<Token> result=new ArrayList<Token>();
	public TestReader(String text,ArrayList<Token>tokens,HashMap<String,String>expresiones,HashMap<String,String>errores,HashMap<Integer,String>comentarios) {
		String token="";
		System.out.println("1:"+text);
		//text=text.replaceAll("["+comentarios.get(0).replaceAll("*", "\\*")+"].*["+comentarios.get(1).replaceAll("*", "\\*")+"]", "");
		text=text+" ";
		String aux="";
		int count=0;
		for(int i=0;i<text.length();i++) {
			if(text.charAt(i)=='(' && text.charAt(i+1)=='*') {count++;i++;}
			else if(text.charAt(i)=='*' && text.charAt(i+1)==')') {count--;i++;}
			else if(count==0)aux+=text.charAt(i);
		}
		System.out.println("2:"+aux); text=aux;
		System.out.println(comentarios.get(0) + " "+comentarios.get(1));
		String[] partes=text.split("\\s+");
		for(int i=0; i<partes.length;i++) {
			if(partes[i].trim().length()>0) {
				add(partes[i],tokens,expresiones);
				//System.out.println(partes[i]);
			}
		}
		for(Token t: result) {
			System.out.println("Token: -"+t.getName());
		}
	}



	private void add(String token,ArrayList<Token>tokens,HashMap<String,String>expresiones) {
		System.out.println("Se procesa - "+token);
		if(token.trim().length()==0)return;
		if(token.contains("\""))token=token.replaceAll("\"","\\\"");
		boolean found=false;
		for(Token t: tokens) {
			if(t.getValue().equalsIgnoreCase(token)) {
				if(token.equals(";")) {System.out.println("comatok ");}
				result.add(t);found=true;break;				
			}
			else if(token.contains(t.getValue())) {
				System.out.println("exptok "+ token+".");
				System.out.println("exptok "+ t.getValue()+".");
				System.out.println("exptok "+ token.replace(t.getValue(), "")+".");
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
					result.add(new Token(token,s));
					System.out.println("keys -> "+s+":"+token);
					found=true;break;
				}
				else if(token.matches(expresiones.get(s)+".*")) {
					String aux=token.replace(expresiones.get(s)+".*", "");
				}
				}catch(PatternSyntaxException pse) {
					System.out.println("Pattern no valido : "+expresiones.get(s));
				}
			}
		}
		if(!found)System.out.println("No encontrado"+token);
	}
}
