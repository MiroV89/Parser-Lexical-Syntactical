package lexico;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import common.Mensaje;

public class ScannerWriter {
private String fileName = "scanner.flex";

private boolean expresion_readed = false;
private boolean token_readed = false;
private boolean errores_readed = false;
private boolean isComment=false;
private HashMap<String, String> tokens;
private HashMap<String, String> expresiones;
private HashMap<String, String> errores;
private HashMap<Integer, String> comentarios;


public ScannerWriter(String path,HashMap<String, String> tokens,HashMap<String, String> expresiones,HashMap<String, String> errores,HashMap<Integer, String> comentarios) throws IOException{
	this.tokens=tokens;
	this.expresiones=expresiones;
	this.errores=errores;
	this.comentarios=comentarios;
	fileName=path+"doc"+File.separator+"specs"+File.separator+fileName;
	String cadena;
    StringBuilder reader = new StringBuilder();
		FileReader f = new FileReader(fileName);
	    BufferedReader b = new BufferedReader(f);
		while((cadena = b.readLine())!=null) {
			try{
				if(!expresion_readed && cadena.trim().startsWith("%}")){
					cadena = b.readLine();
					while(cadena.trim().isEmpty()) cadena = b.readLine();
					while(!cadena.trim().startsWith("%%")){
						if(!cadena.isEmpty() && cadena.contains("=") && cadena.trim().length()!=0 && !cadena.startsWith("//") && !cadena.trim().equals("%%")){
							String key = "";
							String value="";
							key = cadena.split("=")[0].trim();
							value =cadena.split("=")[1].trim();
							expresiones.put(key,value);
						}
						cadena = b.readLine();
					}
					expresion_readed=true;
				}
				else if(expresion_readed && !token_readed && cadena.trim().startsWith("<YY")){
					while((cadena = b.readLine())!=null && !token_readed){
						if(!cadena.isEmpty() && cadena.contains("sym.")){
							String key = "";
							String value="";
							if(cadena.contains("\""))
								key = cadena.split("\"")[1].replace("\"","").trim();
							else 
								key = cadena.trim().split(" ")[0].trim();
							value =cadena.split("sym.")[1].split(";")[0].trim();
							value = value.substring(0, value.length()-1);
							tokens.put(key,value);
						}
						if(cadena.trim().startsWith("{fin} {}")){token_readed=true;}
					}
				}
				else if(token_readed && !errores_readed ){
					while((cadena = b.readLine())!=null){
						if(!cadena.isEmpty() && cadena.trim().startsWith("{")){
							String key = cadena.split("}")[0].trim().substring(1);
							String value = "";
							if(cadena.contains("\"")){
								value = cadena.split("\"")[1].replace("\"","").trim();
								key = key.trim().replaceAll("\"", "");
							}
							while((cadena = b.readLine())!=null && !cadena.contains("}") && !cadena.startsWith("<")){
								if(cadena.contains("lexicalError")) {
									value=cadena.split("lexicalError")[2].split(";")[0].substring(1).trim();
									if(value.startsWith("(\""))value=value.trim().substring(2,value.length()-1);
									if(!value.endsWith("\"")) value += "+\"";
								}
								else value += cadena+"\n";
							}
							errores.put(key,value);
						}
						else if(!cadena.isEmpty() && cadena.trim().startsWith("[")){
							String key = cadena.split(" ")[0].trim();
							String value =""; 
							if(cadena.contains("lexicalError")) {
								value=cadena.split(" ")[1].trim();
								value = value.substring(1);
							}
							while((cadena = b.readLine())!=null && !cadena.contains("}") && !cadena.startsWith("<")){
								if(cadena.contains("lexicalError")) {
									value=cadena.split("lexicalError")[2].split(";")[0].substring(1).trim();
								}
								value += cadena+"\n";
							}
							errores.put(key, value);
						}
						else if(cadena.startsWith("<")){
							errores_readed=true;
							int i=0;
							while((cadena=b.readLine()) != null) {
								if(cadena.contains("\"") && i<2) {
									String cad1=cadena.split("\"")[1].split("\"")[0];
									comentarios.put(i, cad1);
									i++;
								}
							}
							}
					}
				}
				else{reader.append(cadena+"\n");}
			}catch (ArrayIndexOutOfBoundsException aioobe){
				aioobe.printStackTrace();
			}
		}
		b.close();
}

public void write(HashMap<String, String> tokens,HashMap<String, String> expresiones,HashMap<String, String> errores, HashMap<Integer, String> comentarios) throws IOException{
		if(comentarios.size()>0){
			if(!comentarios.get(0).isEmpty())
			isComment=true;
			this.comentarios = comentarios;
		}
		try {
	            FileWriter fileWriter = new FileWriter(fileName);

	            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

	            bufferedWriter.write(getInicio());
	            bufferedWriter.write(getExpresiones(expresiones));	            
	            bufferedWriter.write(getTokens(tokens,expresiones));
	            bufferedWriter.write(getErrores(errores));
	            bufferedWriter.write("\n }\n");
	            if(isComment){
	            	bufferedWriter.write("<COMMENT>{\n"
	            			+ "\t\t\""+comentarios.get(0)+"\" {commentCount++;\n"
	            			+ "\t\tlinecom = yyline+1;\n"
	            			+ "\t\tcolumncom = yycolumn+1;}\n"
	            			+ "\t\t.|{ESPACIO_BLANCO} {}\n"
	            			+ "\t\t\""+comentarios.get(1)+"\" { commentCount--;\n"
	            			+ "\t\tif(commentCount == 0 ) {yybegin(YYINITIAL);}\n"
	            			+ "\t}\n"
	            			+ "}\n");
	            }
	            bufferedWriter.close();
	        }
	        catch(IOException ex) {
	        	Mensaje.print("ERROR", "Se ha producido un error en la escritura del fichero scanner");
	        }
	    
	}
			
	
	private String getInicio(){
		Date date = new Date(System.currentTimeMillis());
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
		String inicio = "package compiler.lexical;\n"
				+ "import compiler.syntax.sym;\n"
				+ "import compiler.lexical.Token;\n"
				+ "import es.uned.lsi.compiler.lexical.ScannerIF;\n"
				+ "import es.uned.lsi.compiler.lexical.LexicalError;\n"
				+ "import es.uned.lsi.compiler.lexical.LexicalErrorManager;\n\n"	
				+ "//Produced by MV tools " + date.toString() + " "+ hourFormat.format(date)+"\n"
				+ "// incluir aqui, si es necesario otras importaciones\n\n"		
				+ "%%\n\n"		 
				+ "%public\n"
				+ "%class Scanner\n"
				+ "%char\n"
				+ "%line\n"
				+ "%column\n"
				+ "%cup\n"
				+ "%ignorecase\n\n\n"				
				+ "%implements ScannerIF\n"
				+ "%scanerror LexicalError\n\n"		
				+ "// incluir aqui, si es necesario otras directivas\n"		
				+ "%{\n "
				+ "LexicalErrorManager lexicalErrorManager = new LexicalErrorManager ();\n "
				+ "private int commentCount = 0;\n "
				+ "int linecom=0;\n "
				+ "int columncom=0;\n "
				+ "int contadorstring=0;\n\n\n "
				+ "//Funcion para crear tokens\n\n "
				+ "Token createToken (int x)	{\n "
				+ "\tToken token = new Token (x);\n "
				+ "\t token.setLine (yyline + 1);\n "
				+ "\t token.setColumn (yycolumn + 1);\n "
				+ "\t token.setLexema (yytext ());\n "
				+ "\t return token;\n}\n\n "
				+ "LexicalError createError(String mensaje){\n"
				+ "\tLexicalError error = new LexicalError (mensaje);\n"
				+ "\terror.setLine (yyline + 1);\n"
				+ "\terror.setColumn (yycolumn + 1);\n"
				+ "\terror.setLexema (yytext ());\n"
				+ "\tlexicalErrorManager.lexicalError (mensaje);\n"
				+ "\t return error;\n}\n"
				+ "%}  \n\n";
		return inicio;
	}
	private String getExpresiones(HashMap<String, String> lista){
		StringBuilder expresion = new StringBuilder();
		expresion.append("\n//Declaracion de expresiones:\n\n");
		SortedSet<String> keys = new TreeSet<String>(lista.keySet());
		for(String s:keys){
			expresion.append(s+" = "+lista.get(s)+"\n");
		}
		expresion.append("\n//FIN Declaracion de expresiones:\n\n");
		if(isComment){
			expresion.append("%x COMMENT\n\n");
		}
		expresion.append("\n\n");
		return expresion.toString();
	}
	private String getTokens(HashMap<String, String> lista,HashMap<String, String> expresiones){
		StringBuilder token = new StringBuilder();
		token.append("%%\n\n<YYINITIAL> \n{\n\n");
		token.append("\n//Declaracion de tokens:\n\n");
		SortedSet<String> keys = new TreeSet<String>(lista.keySet());
		for(String s:keys){
			token.append("\t"+generateToken(s)+"  \t\t{return createToken (sym."+lista.get(s).toUpperCase()+");}\n");
		}
		SortedSet<String> expkeys = new TreeSet<String>(expresiones.keySet());
		for(String s:keys){
			if(!s.equals("ESPACIO_BLANCO") && !s.equals("fin"))
			token.append("\t{"+s+"}  \t\t{return createToken (sym."+s+");}\n");
		}
		token.append("\n//FIN Declaracion de tokens:\n\n");

		if(isComment){
			token.append("	\""+comentarios.get(0)+"\"	           { \n"
					+ "commentCount++;\n"
					+ "linecom=yyline+1; \n"
					+ "columncom=yycolumn+1; \n"
					+ "yybegin(COMMENT); \n"
					+ " }\n\n");
		}
		token.append("\n\n{ESPACIO_BLANCO}	{}\n\n{fin} {}\n\n\n\n");
		return token.toString();
	}
	
	private String generateToken(String key){
		if(key.startsWith("{"))return key;
		return  "\""+key+"\"";
	}
	
	private String getErrores(HashMap<String, String> lista){

		StringBuilder errores = new StringBuilder();
		errores.append("\n//Declaracion de errores:\n\n");
		if(lista.containsKey("[^]")){lista.remove("[^]");}
		SortedSet<String> keys = new TreeSet<String>(lista.keySet());
		for(String s:keys){
			errores.append("\t{"+s+"}  \t\t{lexicalErrorManager.lexicalError(createError(\""+lista.get(s)+"\"));}\n");
		}
		errores.append("[^] {\n");
		errores.append("\t\tLexicalError error = new LexicalError ();\n");
		errores.append("\t\terror.setLine (yyline + 1);\n");
		errores.append("\t\terror.setColumn (yycolumn + 1);\n");
		errores.append("\t\terror.setLexema (yytext ());\n");
		errores.append("\t\tlexicalErrorManager.lexicalError (error);\n");
		errores.append("\t}\n");
		errores.append("\n//FIN Declaracion de errores:\n\n");
		errores.append("\n\n");
		return errores.toString();
	}

	public HashMap<String, String> getTokens() {
		return tokens;
	}

	public void setTokens(HashMap<String, String> tokens) {
		this.tokens = tokens;
	}

	public HashMap<String, String> getExpresiones() {
		return expresiones;
	}

	public void setExpresiones(HashMap<String, String> expresiones) {
		this.expresiones = expresiones;
	}

	public HashMap<String, String> getErrores() {
		return errores;
	}

	public void setErrores(HashMap<String, String> errores) {
		this.errores = errores;
	}

}
