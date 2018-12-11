package sintactico;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import common.Mensaje;

public class ParserDebug {
public static String onlyReduce(String syntaxTest) {
	String[] partes = syntaxTest.split("\n");
	String result="";
	for(int i=0;i<partes.length;i++) {
		if(partes[i].contains("Reduce with prod")) {
			result+=partes[i]+"\n";
		}
	}
	return result;
}

public static String readFile(String testPath) {
	String cadena="";
	String texto="";
	try {
		FileReader fr = new FileReader (testPath);
		BufferedReader b = new BufferedReader(fr);
		while((cadena=b.readLine())!=null){
			texto+=cadena+"\n";
		}
		b.close();
	}catch (IOException ioe) {
    	Mensaje.print("ERROR", "No se ha podido leer el fichero");
	}
	return texto;
}


}
