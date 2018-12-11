package tests;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import compiler.lexical.Token;
import java_cup.runtime.Symbol;


public class MyParserWriter {

    public MyParserWriter(String ruta) throws Exception {
    	ruta=ruta.replace("\\", "\\\\");
    	
    	String msg="package compiler.syntax;\r\n" + 
    			"\r\n" + 
    			"import java.io.BufferedWriter;\r\n" + 
    			"import java.io.ByteArrayOutputStream;\r\n" + 
    			"import java.io.File;\r\n" + 
    			"import java.io.FileReader;\r\n" + 
    			"import java.io.FileWriter;\r\n" + 
    			"import java.io.PrintStream;\r\n" + 
    			"import java.io.Reader;\r\n" + 
    			"import java.lang.reflect.Constructor;\r\n" + 
    			"import java.nio.charset.StandardCharsets;\r\n" + 
    			"import java.util.ArrayList;\r\n" + 
    			"\r\n" + 
    			"import es.uned.lsi.compiler.lexical.ScannerIF;\r\n" + 
    			"\r\n" + 
    			"public class subparser extends parser{\r\n" + 
    			"	//Insertar aqui la extensión de los ficheros de tests\r\n" + 
    			"	public static String extension=\".muned\";\r\n" + 
    			"	\r\n" + 
    			"	public subparser(ScannerIF s) {super(s);}\r\n" + 
    			"	String msg=\"\";\r\n" + 
    			"	  public void debug_message(String mess)\r\n" + 
    			"	  {\r\n" + 
    			"			try {\r\n" + 
    			"				ByteArrayOutputStream baos = new ByteArrayOutputStream();\r\n" + 
    			"				PrintStream ps = new PrintStream(baos, true, \"utf-8\");\r\n" + 
    			"			    PrintStream err= System.err;\r\n" + 
    			"			    PrintStream out= System.out;\r\n" + 
    			"			    System.setErr(ps);\r\n" + 
    			"			    System.setOut(ps);\r\n" + 
    			"				System.out.println(mess);\r\n" + 
    			"				System.setErr(err);\r\n" + 
    			"			    System.setOut(out);\r\n" + 
    			"				msg+=new String(baos.toByteArray(), StandardCharsets.UTF_8);\r\n" + 
    			"			    \r\n" + 
    			"			} catch (Exception e) {\r\n" + 
    			"				e.printStackTrace();\r\n" + 
    			"			}\r\n" + 
    			"	  }\r\n" + 
    			"	  public String getMSG() {\r\n" + 
    			"		  return msg;\r\n" + 
    			"	  }\n"+ 
    			"	  public void syntax_error(Symbol symbol)\r\n" + 
    			"	  { \r\n" + 
    			"		  Token token = (Token) symbol.value;\r\n" + 
    			"		  msg+=token+\" Error sintactico\\n\";	    \r\n" + 
    			"	  }\r\n" + 
    			"			\r\n" + 
    			"	  public void unrecovered_syntax_error(java_cup.runtime.Symbol symbol)\r\n" + 
    			"	  {	\r\n" + 
    			"		  Token token = (Token) symbol.value;\r\n" + 
    			"		  msg+=token+\" Error fatal\\n\";	\r\n" + 
    			"	  }"+
    			"	  public static void main(String[] args) throws Exception {\r\n" + 
    			"		  String input=\""+ruta+"doc\"+File.separator+\"test\"+File.separator;\r\n" + 
    			"		  String output=input+\"debug\"+File.separator;\r\n" +
    			"		  File carpeta = new File(output);\r\n"+
    			"		  carpeta.mkdir();\r\n"+
    			"		  File folder = new File(input);\r\n" + 
    			"		  File[] listofFiles = folder.listFiles();\r\n" + 
    			"		  for(int i=0; i<listofFiles.length; i++) {\r\n" + 
    			"			  String fileName=listofFiles[i].getName();\r\n" + 
    			"			  if(fileName.endsWith(extension)) {\r\n" + 
    			"				  String fileDebug=fileName.replace(extension,\".debug\");\r\n" + 
    			"				  FileReader aFileReader = new FileReader (input+fileName);\r\n" + 
    			"			      Class scannerClass = Class.forName (\"compiler.lexical.Scanner\"); \r\n" + 
    			"			      Constructor scannerConstructor = scannerClass.getConstructor(Reader.class);\r\n" + 
    			"			      ScannerIF aScanner = (ScannerIF) scannerConstructor.newInstance(aFileReader);\r\n" + 
    			"				  subparser s=new subparser(aScanner);\r\n" + 
    			"				  s.debug_parse();\r\n" + 
    			"				  System.out.println(listofFiles[i].getName());\r\n" + 
    			"				  FileWriter fileWriter = new FileWriter(output+fileDebug);\r\n" + 
    			"		          BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);\r\n" + 
    			"		          bufferedWriter.write(s.getMSG());\r\n" + 
    			"		          bufferedWriter.close();\r\n" + 
    			"			  }\r\n" + 
    			"		  }\r\n" + 
    			"	  }\r\n" + 
    			"}";
    	File fpar = new File(ruta+File.separator+"src"+File.separator+"compiler"+File.separator+"syntax"+File.separator+"subparser.java");
    	if(!fpar.exists()) {
	        FileWriter fileWriter = new FileWriter(fpar.getAbsolutePath());
	        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	        bufferedWriter.write(msg);
	        bufferedWriter.close();
    	}
    }


}