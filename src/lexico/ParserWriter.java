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

public class ParserWriter {

private String fileName = "parser.cup";
private boolean readed = false;
	public ParserWriter(HashMap<String, String> tokens, String path) throws IOException{
		fileName=path+"doc"+File.separator+"specs"+File.separator+fileName;
		Date date = new Date(System.currentTimeMillis());
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
				String cadena;
			    StringBuilder reader = new StringBuilder();
					FileReader f = new FileReader(fileName);
				    BufferedReader b = new BufferedReader(f);
					while((cadena = b.readLine())!=null) {
							if(cadena.startsWith("terminal")){
								if(!readed){
									reader.append(getTokens(tokens));
									readed = true;
								}
							}
							else if(cadena.startsWith("precedence") || cadena.startsWith("//precedence")){
								reader.append("//"+cadena+"\n");
								while(cadena.endsWith(",")){
									cadena=b.readLine();
									reader.append("//"+cadena+"\n");
								}
							}
							else{
								if(cadena.trim().equals("// Para la entrega de Junio deberán descomentarse y usarse.")){
									String string = cadena.split("P")[0];
									cadena +=".\n"+string+"Produced by MV tools " + date.toString() + " "+ hourFormat.format(date)+"\n";
								}
								reader.append(cadena+"\n");
								}	
					
					}b.close();

	        try {
	            FileWriter fileWriter = new FileWriter(fileName);
	            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	            bufferedWriter.write(reader.toString());
	            bufferedWriter.close();
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error writing to file '"
	                + fileName + "'");
	        }
	    }
	
	
	private String getTokens(HashMap<String, String> lista){
		StringBuilder token = new StringBuilder();
		lista.forEach((k,v)-> token.append("terminal Token "+v.toUpperCase()+";\n"));
		return token.toString();
	}
}
