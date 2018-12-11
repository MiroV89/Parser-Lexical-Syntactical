package compiler.syntax;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import es.uned.lsi.compiler.lexical.ScannerIF;

public class subparser extends parser{
	//Insertar aqui la extensión de los ficheros de tests
	public static String extension=".muned";
	
	public subparser(ScannerIF s) {super(s);}
	String msg="";
	  public void debug_message(String mess)
	  {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos, true, "utf-8");
			    PrintStream err= System.err;
			    PrintStream out= System.out;
			    System.setErr(ps);
			    System.setOut(ps);
				System.out.println(mess);
				System.setErr(err);
			    System.setOut(out);
				msg+=new String(baos.toByteArray(), StandardCharsets.UTF_8);
			    
			} catch (Exception e) {
				e.printStackTrace();
			}
	  }
	  public String getMSG() {
		  return msg;
	  }
	  public static void main(String[] args) throws Exception {
		  ArrayList<String> paths=new ArrayList<String>();
		  File folder = new File("C:\\workspaceTFG\\PL1_Miroslav_Krasimirov_Vladimirov\\doc\\test\\");
		  File[] listofFiles = folder.listFiles();
		  for(int i=0; i<listofFiles.length; i++) {
			  String path=listofFiles[i].getAbsolutePath();
			  if(path.endsWith(extension)) {
				  String pathdeb=path.split(extension)[0]+"debug";
				  FileReader aFileReader = new FileReader (path);
			      Class scannerClass = Class.forName ("compiler.lexical.Scanner"); 
			      Constructor scannerConstructor = scannerClass.getConstructor(Reader.class);
			      ScannerIF aScanner = (ScannerIF) scannerConstructor.newInstance(aFileReader);
				  subparser s=new subparser(aScanner);
				  s.debug_parse();
				  FileWriter fileWriter = new FileWriter(pathdeb);
		          BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		          bufferedWriter.write(s.getMSG());
		          bufferedWriter.close();
			  }
		  }
	  }
}
