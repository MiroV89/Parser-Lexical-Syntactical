package sintactico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import common.Mensaje;
import lexico.GuiLexico;

public class ParserReader {
	private String fileName = "parser.cup";
	private ArrayList<Token> tokens = new ArrayList<Token>();
	private ArrayList<NonTerminal> nonterminal = new ArrayList<NonTerminal>();
	private ArrayList<Precedence> precedence = new ArrayList<Precedence>();


	public ParserReader(String path) throws IOException{
		fileName=path+"doc"+File.separator+"specs"+File.separator+fileName;
		String cadena;
			FileReader f = new FileReader(fileName);
		    BufferedReader b = new BufferedReader(f);
		    int precedence_level=0;
			while((cadena = b.readLine())!=null) {
				if(cadena.toLowerCase().startsWith("terminal token")) {
					cadena=cadena.toLowerCase().replace("terminal token", "");
					cadena=cadena.replace(";","");
					cadena=cadena.toUpperCase().trim();
					String[] elementos = cadena.split(",");
					for(String el : elementos) {
						for(Token t: GuiLexico.getTokenArray()) {
							if(t.getName().equalsIgnoreCase(el)) {
								tokens.add(t);
							}
						}
					}
				}
				else if(cadena.toLowerCase().startsWith("non terminal")) {
					cadena=cadena.replace("non terminal", "");
					cadena=cadena.replace(";","");
					String[] elementos = cadena.split(",");
					boolean clase = true;
					String classname="";
					String nonterm="";
					for(String el : elementos) {
						if(clase) {
							el=el.replaceAll("\\s+", " ");
							String[] elem = el.trim().split(" ");
							if(elem.length>1) {
								classname=elem[0];
								nonterm=elem[1];
								nonterminal.add(new NonTerminal(classname,nonterm));
							}
							else {
								nonterminal.add(new NonTerminal(elem[0]));
							}
							clase=false;
						}
						else {
							if(!classname.isEmpty()) {
								nonterminal.add(new NonTerminal(el.trim()));								
							}
							else {
								nonterminal.add(new NonTerminal(el.trim()));
							}
						}
					}
				}
				else if(cadena.toLowerCase().startsWith("precedence ")) {
					cadena=cadena.replace("precedence " , "");
					cadena=cadena.replace(";",""); 
					cadena=cadena.replaceAll("\\s+", " ");
					String[] elementos = cadena.split(",");
					Precedence pre=null;
					boolean direction=true;
					for(String el : elementos) {
						if(direction) {
							String[] elem = el.trim().split("\\s");
							boolean found=false;
							Token token=null;
							for(Token t: tokens) {
								if(t.equals(elem[1].trim())) {
									found=true; token=t;
								}
							}
							if(!found) {
								System.out.println("No se ha declarado el token "+el);
							}
							else {
								pre=new Precedence(precedence_level, elem[0].trim(),token);
								direction=false;
							}
						}
						else {
							boolean found=false;
							for(Token t: tokens) {
								if(t.equals(el.trim())) {
									found=true;
								}
							}
							if(!found) {
								Mensaje.print("ERROR","No se ha declarado el token "+el);
							}
							else {
								pre.addTokens(new Token(el));
							}
						}
					}
					try {
					precedence.add(pre); 
					precedence_level++;
					}
					catch (NullPointerException npe) {System.out.println("FALLO : "+cadena);}
				}
				else if(cadena.toLowerCase().startsWith("start with ")) {
					break;
				}
			}
			//Terminales, NonTerminales y Precedencias leidas, Comenzamos a leer la estructura en si
			//Nodo raiz:
			cadena=cadena.replace(";","");
			cadena=cadena.replace("start with ","").trim();
			for(NonTerminal n : nonterminal) {
				if(n.getName().equals(cadena)) {n.raiz=true; break;}
			}
			
			
			//
			//
			//Empieza la lectura del sintactico
			//
			//
			
			while((cadena = b.readLine())!=null) {
				leerSintactico(cadena,b);
			}			
			b.close();
			NonTerminal epsilon = new NonTerminal("epsilon");
			epsilon.getStructure().add(new Produccion());
			boolean foundEpsilon=false;
			for(NonTerminal nt: nonterminal) {
				if(nt.getName().equals("epsilon")) {
					epsilon=nt;foundEpsilon=true;
				}
			}
			if(!foundEpsilon) {	nonterminal.add(epsilon);}
			for(NonTerminal nt:nonterminal) {
				for(Produccion p: nt.getStructure()) {
					if(p.getStructure().size()==0 && !nt.getName().equals("epsilon")) {
						p.getStructure().add(epsilon);
					}
				}
			}
	}

	public void addtnt(Produccion p, String name) {	
		for(NonTerminal n : nonterminal) {
			if(n.getName().equals(name)) {
				p.add(n);
				return;
			}
		}
		for(Token t : tokens) {
			if(t.getName().equals(name)) {
				p.add(t);				
				return;
			}
		}
		NonTerminal nt = new NonTerminal(name);
		p.add(nt);
		nonterminal.add(nt);
	}
	
	private void leerSintactico(String cadena,BufferedReader b) throws IOException {
		String cadenaAux="";
		String nombreel="";
		String texto="";
		if(cadena.contains("::=")) {
			String[] elem = cadena.split("::=");
			String padre = elem[0].trim();			
				texto=cadena.split("::=")[1].replaceAll("\n","").trim();
				boolean fin=false;
				while(!cadena.contains(";") && !fin) {
					cadena=b.readLine();
					texto+=cadena;
					if(cadena.contains("{:")) {
						while(!cadena.contains(":}")) {
							cadena=b.readLine();
							texto+=cadena;
						}
					}
					//Patron de busqueda {:.*:}
					if(texto.replaceAll("['{:'].*[':}']", "").contains(";")) {
						
						fin=true;
					}
				}
				texto=texto.replaceAll("\\s+", " ");
				
				Produccion p = new Produccion();
				for(int i=0;i<texto.length();i++) {
					//En caso de encontrar espacio generamos un nonterminal
					if(Character.isWhitespace(texto.charAt(i))) {
							if (nombreel.trim().length()!=0)
							addtnt(p, nombreel.trim());
							nombreel="";	
					}
					else if(texto.charAt(i)==':') {		
						addtnt(p, nombreel.trim());
						nombreel="";							
						i=i+1;
						while(texto.charAt(i)!=' ' && texto.charAt(i)!='{') {
							cadenaAux+=texto.charAt(i);
							i++;
						}			
						p.add(new Subname(cadenaAux.trim()));	
						cadenaAux="";					
					}
					//En caso de encontrarse con {:
					else if(texto.charAt(i)=='{' && texto.charAt(i+1)==':') {
						String aux = texto.substring(i+2);
						String aux2 = aux.split(":}")[0];
						i=i+aux2.length()+3;
						p.add(new SemanticProduction(aux2));
					}
					else if(texto.charAt(i)=='|') {
						for(NonTerminal nt : nonterminal) {
							if(nt.equals(padre)) nt.getStructure().add(p);
						}
						p=new Produccion();
						nombreel="";
					}
					else if(texto.charAt(i)==';') {
						if(!nombreel.trim().isEmpty() && nombreel.trim().length()!=0) {
							addtnt(p, nombreel.trim());
						}
					}
					//Leyendo no terminal
					else {
						nombreel+=texto.charAt(i);
					}
				}
				for(NonTerminal nt : nonterminal) {
					if(nt.equals(padre)) nt.getStructure().add(p);
				}
			
		}
	
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}


	public void setTokens(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}


	public ArrayList<NonTerminal> getNonterminal() {
		
		return nonterminal;
	}


	public void setNonterminal(ArrayList<NonTerminal> nonterminal) {
		this.nonterminal = nonterminal;
	}


	public ArrayList<Precedence> getPrecedence() {
		return precedence;
	}


	public void setPrecedence(ArrayList<Precedence> precedence) {
		this.precedence = precedence;
	}
}
