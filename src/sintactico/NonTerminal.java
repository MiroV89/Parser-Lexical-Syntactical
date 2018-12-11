package sintactico;

import java.util.ArrayList;

public class NonTerminal extends Element{
	public boolean raiz=false;
	private String clase="";
	private ArrayList<Produccion> structure = new ArrayList<Produccion>();
	private String semantico="";
	private String semanticName="";
	public int i=0;
	public int prodSize=0;

	public boolean reduced() {
		if(i<prodSize && prodSize!=0) {
			i++;
			return false;
		}
		else return true;
	}
	public int geti() {return i;}
	public int getsize() {return prodSize;}
	public String getSemanticName() {
		return semanticName;
	}
	public void setSemanticName(String semanticName) {
		this.semanticName = semanticName;
	}
	public String getSemantico() {
		return semantico;
	}
	public void setSemantico(String semantico) {
		this.semantico = semantico;
	}
	public NonTerminal(String name) {
		super();
		this.setName(name);
	}
	public NonTerminal(String clase, String name) {
		super();
		this.clase = clase;
		this.setName(name);
	}
	
	
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public ArrayList<Produccion> getStructure() {
		return structure;
	}
	public void addProduccion(Produccion p) {
		structure.add(p);
	}
	public Produccion getProduccion() {
		if(structure.isEmpty())return null;
		int numnoterminales=0;
		for(Element e:structure.get(0).getStructure())
			if(e instanceof NonTerminal)numnoterminales++;
		prodSize=numnoterminales;
		return structure.get(0);
	}
	public void setStructure(ArrayList<Produccion> structure) {
		this.structure = structure;
	}
	public void deleteProduction(Produccion p) {
		structure.remove(p);
		
	}
	
}
