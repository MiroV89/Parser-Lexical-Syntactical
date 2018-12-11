package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import common.Mensaje;
import lexico.GuiLexico;
import lexico.ScannerWriter;
import sintactico.*;

public class TreeBuilder {
	String ruta="";
	String rutaparser="";
	String rutasym="";
	String treetext="";
	ArrayList<String> expresiones = new ArrayList<String>();
	ArrayList<String> listaTokens = new ArrayList<String>();
	ArrayList<String> tokensFiltroNom = new ArrayList<String>();
	ArrayList<String> tokensFiltroSim = new ArrayList<String>();
	
	public static void main(String[] args) {
		String path="C:\\workspaceTFG\\PL1_Miroslav_Krasimirov_Vladimirov\\doc\\test\\testCase03.muned";
		String path2="C:\\workspaceTFG\\PL1_Miroslav_Krasimirov_Vladimirov\\";
		String red=ParserDebug.onlyReduce(ParserDebug.readFile(path));
		
		new TreeBuilder(path2,red,null);
	}
	public TreeBuilder(String path,String debug,Tree tree) {
		// String path,HashMap<String, String> tokens,HashMap<String, String> expresiones,HashMap<String, String> errores,HashMap<Integer, String> comentarios
		ScannerWriter sw;
		HashMap<String,String> tokens = new HashMap<String,String>();
		HashMap<String,String> exp = new HashMap<String,String>();
		System.out.println("{{{\n"+debug+"\n}}}");
		try {
			sw =new ScannerWriter(path,tokens,exp,new HashMap<String,String>(),new HashMap<Integer,String>());
	    	SortedSet<String> keys = new TreeSet<String>(exp.keySet());
			for(String s:keys){
		    	SortedSet<String> tokeys = new TreeSet<String>(tokens.keySet());
				for(String t:tokeys){
					if(t.contains(s)) {
						System.out.println("Tenemos  coincidencia: "+t+"=="+s);
						expresiones.add(tokens.get(t));
					}
				}
			}
			
	    	SortedSet<String> tokeys = new TreeSet<String>(tokens.keySet());
	    	for(String s:tokeys) {
	    		tokensFiltroSim.add(s);
	    	}
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
		
		this.ruta=path+File.separator+"src"+File.separator+"compiler"+File.separator+"syntax"+File.separator;
		rutaparser=ruta+"parser.java";
		rutasym=ruta+"sym.java";
		HashMap<Integer,NonTerminal> casos=new HashMap<Integer,NonTerminal>();
		casos= generateCases(rutaparser);
		HashMap<Integer,String> syms=new HashMap<Integer,String>();
		syms=generateSym(rutasym);		
		NTTree arbol=buildTree(debug,casos,syms);
		if(tree!=null) draw(tree,arbol);
		treetext=textDraw(arbol,0);		
	}
	
	public void exportToFile(File file) throws IOException {
		FileWriter fr = new FileWriter(file+".tree");
		fr.write(treetext);
		fr.close();
	}
	
	public NTTree buildTree(String debug,HashMap<Integer,NonTerminal> nonterm,HashMap<Integer,String> syms) {

		ArrayList<Integer> red = filter(debug);
		ArrayList<Nodo> noreducidos = new ArrayList<Nodo>();
		for(Integer i : red) {
			NonTerminal nt = nonterm.get(i);
			NTTree tree=new NTTree(nt);
			for(Element e:nt.getProduccion().getStructure()) {
				if(e instanceof Token) {
					tree.addChild(new NTTree(e,true));
				}
				else if(e instanceof NonTerminal) {
					for(int index=noreducidos.size()-1;index>=0;index--) {
						if(noreducidos.get(index).getName().equals(e.getName())) {
							noreducidos.get(index).getNttree().setFather(tree);
							noreducidos.remove(index);index=-1;
						}
					}
				}
			}
			noreducidos.add(new Nodo(tree,nt.getName()));		
		}
		return noreducidos.get(0).getNttree();
	}
	
	private ArrayList<Integer> filter(String debug) {
		ArrayList<Integer> reducciones = new ArrayList<Integer>();
		String[] partes = debug.split("\n");
		for(int i=0;i<partes.length;i++) {
			if(partes[i].contains("Reduce with prod")) {
				System.out.println("REDUCE");
				int num =Integer.parseInt(partes[i].split("#")[2].split(" ")[0]);
				if(num!=1 && num!=0)reducciones.add(num);
			}
			else if(partes[i].contains("Current token is Token")) {
				System.out.println("CURRENT");
				String s=partes[i].split("lexema = '")[1].split("',")[0];
				System.out.println(s);
				boolean found=false;
				for(String tok:tokensFiltroSim) {
					if(tok.equalsIgnoreCase(s)) found=true;
				}
				if(!found) {
					System.out.println("Se añade a listaTokens: " +s);
					listaTokens.add(s);
				}
			}
		}
		return reducciones;
	}
	
	
	private void draw(TreeItem root,NTTree arbol) {
		if(arbol!=null){
			TreeItem ti;	
			String name=arbol.getRoot().getName();
			if(arbol.isToken()) {
				for(String s:expresiones){
					if(s.equalsIgnoreCase(name)) {
						if(!listaTokens.isEmpty()) {
							name += " -> "+ listaTokens.get(0);
							listaTokens.remove(0);
						}
					}
				}
				System.out.println(name);
				ti=new TreeItem(root,SWT.BOLD);
			}
			else {ti = new TreeItem(root,SWT.NONE);}
			ti.setText(name);
			for(NTTree ntt:arbol.getChildren()) {
				draw(ti,ntt);
			}
		}
	}
	private void draw(Tree root, NTTree arbol) {
		TreeItem ti=new TreeItem(root,SWT.BOLD);
		ti.setText(arbol.getRoot().getName());
		for(NTTree ntt:arbol.getChildren()) {
			draw(ti,ntt);
		}
	}

	private String textDraw(NTTree arbol, int level) {
		String s="";
		for(int i=0;i<level;i++)
		s+=(" ");
		s+=("|"+arbol.getRoot().getName()+"\n");
		level++;
		for(NTTree nt:arbol.getChildren()) {
			s+=textDraw(nt,level);
		}
		return s;
	}
	private HashMap<Integer, String> generateSym(String rutasym2) {
		HashMap<Integer,String> lista = new HashMap<Integer,String>();
		File f=new File(rutasym2);
		try {
			FileReader fr = new FileReader(f);
		    BufferedReader b = new BufferedReader(fr);
		    String cadena="";
		    while((cadena=b.readLine())!=null) {
		    	if(cadena.contains("public static final int ")) {
		    		cadena=cadena.replace("public static final int ", "").replace(";","").trim();		    		
		    		String name= cadena.split("=")[0].trim();
		    		int num=Integer.parseInt(cadena.split("=")[1].trim());
		    		lista.put(num, name);
		    	}
		    }
		    b.close();
		    return lista;
		    
		} catch (Exception e) {
			Mensaje.print("ERROR", "No se encuentra el fichero sym.java, reconstruya con build.xml");
		}
		return null;
	}


	private HashMap<Integer,NonTerminal> generateCases(String rutaparser2) {
		File f=new File(rutaparser2);
		HashMap<Integer,NonTerminal> lista=new HashMap<Integer,NonTerminal>();
		try {
			FileReader fr = new FileReader(f);
		    BufferedReader b = new BufferedReader(fr);
		    String cadena="";
		    while((cadena=b.readLine())!=null) {
		    	if(cadena.contains("case")) {
		    		String numero = cadena.split("//")[0].split("case")[1].replace(":", "").trim();
		    		int num=Integer.parseInt(numero);
		    		String prod = cadena.split("//")[1].trim();
		    		Produccion p=new Produccion();
		    		NonTerminal nt=new NonTerminal(prod.split("::=")[0].trim());
		    		if(prod.split("::=").length!=1) {
			    		String[] elementos=prod.split("::=")[1].trim().split(" ");
			    		for(int i=0;i<prod.split("::=")[1].trim().split(" ").length;i++) {
			    			String elem=elementos[i].trim();
			    			if(!elem.equals("NT$0"))
			    			if(isUpperCase(elem)) {
			    				p.add(new Token(elem));
			    			}
			    			else {
			    				p.add(new NonTerminal(elem));
			    			}
			    		}
		    		}
		    		nt.addProduccion(p);
		    		lista.put(num, nt);
		    	}
		    }
		    b.close();
		    return lista;
		    
		}catch(FileNotFoundException fnfe) {
			Mensaje.print("ERROR", "No se encuentra el fichero parser.java, reconstruya con build.xml");
		} 
		catch (IOException e) {
			Mensaje.print("ERROR", "Reconstruya con build.xml");
		}
		return null;		
	}


	private boolean isUpperCase(String texto) {
		char[] caracteres=texto.toCharArray();
		for(int i=0;i<texto.length();i++) {
			if(Character.isLowerCase(caracteres[i])) return false;
		}
		return true;
	}
	
	
}
