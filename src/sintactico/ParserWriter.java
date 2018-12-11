package sintactico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import common.Mensaje;

public class ParserWriter {
	private String fileName = "parser.cup";



	public ParserWriter(String path,ArrayList<NonTerminal> nonterminal) throws IOException{
		fileName=path+"doc"+File.separator+"specs"+File.separator+fileName;
		String finaltext="";
		String cadena;
		FileReader f = new FileReader(fileName);
		BufferedReader b = new BufferedReader(f);
		boolean nonterminalAdded=false;
		ArrayList<String> listaAparecidos=new ArrayList<String>();
		while((cadena = b.readLine())!=null) {
			if(cadena.startsWith("non terminal")) {
				if(!nonterminalAdded) {
					finaltext+="\nnon terminal  			program;\r\n" + 
							"non terminal Axiom		axiom;\r\n";
					for(NonTerminal nt:nonterminal) {
						if(!listaAparecidos.contains(nt.getName()))
						if(!nt.getName().equals("program") && !nt.getName().equals("axiom") && !nt.getName().equals("error"))
							finaltext+="non terminal  			"+nt.getName()+";\n";
						listaAparecidos.add(nt.getName());
					}
					nonterminalAdded=true;
				}
			}
			else {
				finaltext+="\n"+cadena;
				if(cadena.contains("start with ")) {
					finaltext+="\n"+generateParserContent(nonterminal);
					break;
				}		
			}
		}
		b.close();
		write(finaltext);		
	}



	private String generateParserContent(ArrayList<NonTerminal> nonterminal) {
		String text="";
		for(NonTerminal nt:nonterminal) {
			if(!nt.getName().equals("error")) {
				text+=nt.getName()+" ::=  ";
				for(Produccion p:nt.getStructure()) {
					for(Element e:p.getStructure()) {
						if(e instanceof Token){
							text+=" "+e.getName().toUpperCase();
						}
						else if(e instanceof NonTerminal) {
							text+=" "+e.getName();
						}
						else if(e instanceof Subname) {
							text+=":"+e.getName()+" ";
						}
						else if(e instanceof SemanticProduction) {
							text+=" {: "+((SemanticProduction)e).getContent()+ " :} ";
						}
					}
					text+="\n\t\t| ";
				}
				text=text.substring(0, text.length()-2).trim();
				text+=";\n\n";	
			}
		}		
		return text;
	}
	
	public void write(String text) throws IOException{
		try {
	            FileWriter fileWriter = new FileWriter(fileName);

	            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

	            bufferedWriter.write(text);
	            bufferedWriter.close();
	        }
	        catch(IOException ex) {
	            Mensaje.print("ERROR","Error al escribir el fichero '"+ fileName + "'");
	        }
	    
	}
}
